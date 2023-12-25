package com.services.DAOs.compte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.models.Compte;

public class CompteDAO implements CompteRepertoire {

    private final Connection connection;

    public CompteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Compte trouver_par_login(String login) {
        try {
            // Example: Query the database to find a user by login
            String sql = "SELECT * FROM compte WHERE login = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, login);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Map the result to a Compte object and return
                        Compte compte = new Compte(resultSet.getLong("id"),
                                resultSet.getString("login"),
                                resultSet.getString(("mot_de_passe")), resultSet.getString(("role")));
                        return compte;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    @Override
    public void supprimer_par_id(Long id) {
        try {
            // Exemple: Supprimer un comptre par id
            String sql = "DELETE FROM compte WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public List<Compte> trouver_tout() {
        List<Compte> comptes = new ArrayList<>();
        try {
            // Example: Retrieve all users from the database
            String sql = "SELECT * FROM compte";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    comptes.add(mapResultSetToCompte(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return comptes;
    }

    @Override
    public Compte trouver_par_id(Long id) {
        try {
            // Example: Query the database to find a user by ID
            String sql = "SELECT * FROM compte WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCompte(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    @Override
    public Compte enregistrer(Compte compte) {
        try {
            // Example: Insert a new compte into the database
            String sql = "INSERT INTO compte (login, mot_de_passe, role) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                prepareCompteStatements(preparedStatement, compte);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return compte;
    }

    private Compte mapResultSetToCompte(ResultSet resultSet) throws SQLException {
        return new Compte(resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString(("mot_de_passe")), resultSet.getString(("role")));
    }

    private void prepareCompteStatements(PreparedStatement preparedStatement, Compte compte) throws SQLException {
        preparedStatement.setString(1, compte.getLogin());
        preparedStatement.setString(2, compte.getMot_de_passe());
        preparedStatement.setString(3, compte.getRole());
    }

    @Override
    public List<Compte> trouver_par_page(int page, int items_count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_page'");
    }

    @Override
    public Compte modifier(Long id, Map<String, Object> updates) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifier'");
    }

}
