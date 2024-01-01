package com.services.DAOs.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.utils.Constants;
import com.utils.StringUtils;
import com.models.BaseArticle;
import com.models.TelephoneMobile;

public class ArticleDAO<T extends BaseArticle<T>> implements ArticleRepertoire<T> {
    private final Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T enregistrer(T entity) {
        String insertTypeSQL = String.format(Constants.INSERT_TYPE_SQL, entity.getType().replace(' ', '_'));

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementArticle = connection.prepareStatement(Constants.INSERT_ARTICLE_SQL,
                    Statement.RETURN_GENERATED_KEYS);
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
        }

        return entity;
    }

    @Override
    public T trouver_par_id(Long id, String tablename) {
        String sql = String.format(Constants.SELECT_ARTICLE_BY_ID_SQL, Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToArticle(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> trouver_par_page(int page, int items_count, String tablename) {
        List<T> articles = new ArrayList<>();
        String sql = String.format(Constants.SELECT_ARTICLES_BY_PAGE_SQL, Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME, tablename,
                Constants.ARTICLE_TABLE_NAME);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, items_count);
            preparedStatement.setInt(2, (page - 1) * items_count);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                articles.add(mapResultSetToArticle(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private Map<String, Object> filterUpdatesPerColumns(List<String> columns, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("Updates map cannot be null or empty");
        }

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

            // Update telephone_mobile table
            updateTable(Constants.TELEPHONE_TABLE_NAME, id,
                    filterUpdatesPerColumns(Constants.TELEPHONE_COLUMNS, updates));

            // Update article table
            updateTable(Constants.ARTICLE_TABLE_NAME, id,
                    filterUpdatesPerColumns(Constants.ARTICLE_COLUMNS, updates));

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

    private void updateTable(String tableName, Long id, Map<String, Object> updates) {
        String sql = StringUtils.buildSqlUpdateStatementFromMap(updates, tableName);

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

    @Override
    public List<T> trouver_tout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_tout'");
    }

    @Override
    public List<T> trouver_par_page(int page, int items_count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_page'");
    }

    @Override
    public void supprimer_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer_par_id'");
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
                LocalDateTime generatedDateCreation = generatedKeys.getTimestamp(2).toLocalDateTime();
                LocalDateTime generatedDateMaj = generatedKeys.getTimestamp(3).toLocalDateTime();

                entity.setId(generatedId);
                entity.setDate_creation(generatedDateCreation);
                entity.setDate_maj(generatedDateMaj);

                executeTypeInsert(entity, preparedStatementType);
            } else {
                throw new SQLException("Creating article failed, no ID obtained.");
            }
        }
    }

    private void executeTypeInsert(T entity, PreparedStatement preparedStatementType) throws SQLException {
        if (entity.getType().equals(Constants.TELEPHONE_MOBILE_TYPE)) {
            prepareTelephoneMobileStatements(preparedStatementType, (TelephoneMobile) entity);
            preparedStatementType.executeUpdate();
        } else {
            throw new IllegalArgumentException("Unsupported article type: " + entity.getType());
        }
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
        } else {
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

    private void prepareArticleStatementsFromMap(Long id, PreparedStatement preparedStatement,
            Map<String, Object> updates)
            throws SQLException {
        int index = 1;
        for (Object value : updates.values()) {
            preparedStatement.setObject(index++, value);
        }
        preparedStatement.setObject(index, id);
    }

}
