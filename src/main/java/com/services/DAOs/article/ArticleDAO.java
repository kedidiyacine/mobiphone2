package com.services.DAOs.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.utils.Constants;
import com.utils.StringUtils;
import com.models.BaseArticle;
import com.models.LigneTelephonique;
import com.models.TelephoneMobile;

public class ArticleDAO<T extends BaseArticle<T>> implements ArticleRepertoire<T> {
    private final Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T enregistrer(T entity) {
        String tableName = entity.getType().replace(" ", "_");
        List<String> columns = null;

        // Use a Map to store table names and their corresponding columns
        Map<String, List<String>> tableColumnsMap = new HashMap<>();
        tableColumnsMap.put(Constants.TELEPHONE_TABLE_NAME, Constants.TELEPHONE_COLUMNS);
        tableColumnsMap.put(Constants.LIGNE_TELEPHONIQUE_TABLE_NAME, Constants.LIGNE_TELEPHONIQUE_COLUMNS);
        // Add more types as needed

        // Retrieve columns based on the table name
        columns = tableColumnsMap.get(tableName);

        if (columns == null) {
            // Handle unsupported table name or throw an exception
            throw new IllegalArgumentException("Unsupported table name: " + tableName);
        }

        String insertTypeSQL = StringUtils.buildInsertStatement(tableName, columns);

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementArticle = connection.prepareStatement(
                    Constants.INSERT_ARTICLE_SQL, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement preparedStatementType = connection.prepareStatement(insertTypeSQL)) {

                prepareAndExecuteArticleInsert(entity, preparedStatementArticle, preparedStatementType);

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the exception or rethrowing a custom exception
        }

        return entity;
    }

    @Override
    public T trouver_par_id(Long id, String tablename) {
        String sql = StringUtils.buildSQLSelectJoinStatement(Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_PRIMARY_KEY, Constants.SUB_ARTICLE_PRIMARY_KEY,
                Constants.ARTICLE_TABLE_NAME + "." + Constants.ARTICLE_PRIMARY_KEY);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // add more types here on this method
                    return mapResultSetToArticle(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void supprimer_par_id(Long id) {
        String sql = StringUtils.buildSQLDeleteStatement(Constants.ARTICLE_TABLE_NAME, Constants.ARTICLE_PRIMARY_KEY);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (log it or rethrow)
        }

    }

    public int count(String tableName) {
        String sql = StringUtils.buildSQLSelectCountStatement(tableName);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<T> trouver_par_page(int page, int items_count, String tablename) {
        List<T> articles = new ArrayList<>();
        String sql = String.format(Constants.SELECT_ARTICLES_BY_PAGE_SQL, Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME);
        System.out.println(sql);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, items_count);
            preparedStatement.setInt(2, (page - 1) * items_count);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // support for other types by changing this method implementation...
                articles.add(mapResultSetToArticle(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private Map<String, Object> filterUpdatesPerColumns(List<String> columns, Map<String, Object> updates) {
        return updates.entrySet().stream()
                .filter(entry -> columns.contains(entry.getKey()) && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public T modifier(Long id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("Updates map cannot be null or empty");
        }

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            if (Constants.ARTICLE_COLUMNS.stream().anyMatch(updates::containsKey)) {
                // Update article table
                updateTable(Constants.ARTICLE_TABLE_NAME, id,
                        filterUpdatesPerColumns(Constants.ARTICLE_COLUMNS, updates),
                        Constants.ARTICLE_PRIMARY_KEY);
            }

            if (Constants.TELEPHONE_COLUMNS.stream().anyMatch(updates::containsKey)) {
                // Update telephone_mobile table
                updateTable(Constants.TELEPHONE_TABLE_NAME, id,
                        filterUpdatesPerColumns(Constants.TELEPHONE_COLUMNS, updates),
                        Constants.SUB_ARTICLE_PRIMARY_KEY);
            } else if (Constants.LIGNE_TELEPHONIQUE_COLUMNS.stream().anyMatch(updates::containsKey)) {
                updateTable(Constants.LIGNE_TELEPHONIQUE_TABLE_NAME, id,
                        filterUpdatesPerColumns(Constants.LIGNE_TELEPHONIQUE_COLUMNS, updates),
                        Constants.SUB_ARTICLE_PRIMARY_KEY);
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            try {
                // Rollback the transaction in case of an exception
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
                // Handle the rollback exception appropriately (log it or rethrow)
            }

            e.printStackTrace();
            // Handle the exception appropriately (log it or rethrow)
        } finally {
            try {
                // Reset auto-commit to true to resume normal behavior
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitException) {
                autoCommitException.printStackTrace();
                // Handle the auto-commit exception appropriately (log it or rethrow)
            }
        }

        // Retrieve the updated entity after the update
        // return trouver_par_id(id);
        return null;
    }

    private void updateTable(String tableName, Long id, Map<String, Object> updates, String where) {
        String sql = StringUtils.buildSqlUpdateStatementFromMap(updates, tableName, where);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            prepareArticleStatementsFromMap(id, preparedStatement, updates);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (log it or rethrow)
        }
    }

    @Override
    public T trouver_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_id'");
    }

    public List<T> trouver_tout(String tablename) {
        List<T> articles = new ArrayList<>();
        String sql = String.format(Constants.SELECT_ARTICLES, Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // again here need support for other types
                articles.add(mapResultSetToArticle(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public List<T> trouver_par_page(int page, int items_count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_page'");
    }

    @Override
    public List<T> trouver_par_type(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_type'");
    }

    @Override
    public List<T> trouver_par_libelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_libelle'");
    }

    @Override
    public List<T> trouver_par_prix_vente(Double prix_vente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_prix_vente'");
    }

    private void prepareAndExecuteArticleInsert(T entity, PreparedStatement preparedStatementArticle,
            PreparedStatement preparedStatementType)
            throws SQLException {
        prepareArticleStatements(preparedStatementArticle, entity);

        int affectedRowsArticle = preparedStatementArticle.executeUpdate();

        if (affectedRowsArticle == 0) {
            throw new SQLException("Creating article failed, no rows affected.");
        }

        try (ResultSet generatedKeys = preparedStatementArticle.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);

                entity.setId(generatedId);

                executeTypeInsert(entity, preparedStatementType);
            } else {
                throw new SQLException("Creating article failed, no ID obtained.");
            }
        }
    }

    private void executeTypeInsert(T entity, PreparedStatement preparedStatementType) throws SQLException {
        if (entity.getType().equals(Constants.TELEPHONE_MOBILE_TYPE)) {
            prepareTelephoneMobileStatements(preparedStatementType, (TelephoneMobile) entity);
        }
        // add other types here
        else if (entity.getType().equals(Constants.LIGNE_TELEPHONIQUE_TYPE)) {
            prepareLigneTelephoniqueStatements(preparedStatementType, (LigneTelephonique) entity);
        } else {
            throw new IllegalArgumentException("Unsupported article type: " + entity.getType());
        }
        preparedStatementType.executeUpdate();

    }

    @SuppressWarnings("unchecked")
    private T mapResultSetToArticle(ResultSet resultSet) throws SQLException {

        String articleType = resultSet.getString("type");

        if ("telephone mobile".equals(articleType)) {
            return (T) new TelephoneMobile(
                    resultSet.getLong("id"),
                    resultSet.getString("libelle"),
                    resultSet.getDouble("prix_vente"),
                    resultSet.getInt("qt_stock"),
                    resultSet.getString("reference"),
                    resultSet.getString("marque"),
                    resultSet.getString("modele"),
                    resultSet.getTimestamp("date_creation").toLocalDateTime(),
                    resultSet.getTimestamp("date_maj").toLocalDateTime());
        } else if ("ligne telephonique".equals(articleType)) {
            return (T) new LigneTelephonique(
                    resultSet.getLong("id"),
                    resultSet.getString("libelle"),
                    resultSet.getDouble("prix_vente"),
                    resultSet.getInt("qt_stock"),
                    resultSet.getString("numero"),
                    resultSet.getString("operateur"),
                    resultSet.getDouble("montant_min_consommation"),
                    resultSet.getTimestamp("date_creation").toLocalDateTime(),
                    resultSet.getTimestamp("date_maj").toLocalDateTime());

        }

        else {
            // Handle other types of articles or throw an exception
            throw new IllegalArgumentException("Unsupported article type: " + articleType);
        }
    }

    private void prepareArticleStatements(PreparedStatement preparedStatement, BaseArticle<T> entity)
            throws SQLException {
        preparedStatement.setString(1, entity.getType());
        preparedStatement.setString(2, entity.getLibelle());
        preparedStatement.setInt(3, entity.getQt_stock());
        preparedStatement.setDouble(4, entity.getPrix_vente());

    }

    private void prepareTelephoneMobileStatements(PreparedStatement preparedStatement, TelephoneMobile telephone)
            throws SQLException {
        preparedStatement.setLong(1, telephone.getId());
        preparedStatement.setString(2, telephone.getReference());
        preparedStatement.setString(3, telephone.getMarque());
        preparedStatement.setString(4, telephone.getModele());
    }

    private void prepareLigneTelephoniqueStatements(PreparedStatement preparedStatement, LigneTelephonique ligne)
            throws SQLException {
        preparedStatement.setLong(1, ligne.getId());
        preparedStatement.setString(2, ligne.getNumero());
        preparedStatement.setString(3, ligne.getOperateur());
        preparedStatement.setDouble(4, ligne.getMontant_min_consommation());
    }

    private void prepareArticleStatementsFromMap(Long id, PreparedStatement preparedStatement,
            Map<String, Object> updates)
            throws SQLException {
        int index = 1;
        for (Object value : updates.values()) {
            preparedStatement.setObject(index++, value);
        }
        preparedStatement.setObject(index, id);
    }

    @Override
    public int count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public List<T> trouver_tout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_tout'");
    }

}
