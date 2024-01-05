package com.services.DAOs.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.models.Session;

public class SessionDAO implements SessionRepertoire {

    private final Connection connection;
    // private final Compte compte;

    public SessionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Session enregistrer(Session session) {
        try {
            // Example: Insert a new session into the database
            String sql = "INSERT INTO session (compte_id) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                prepareSessionStatements(preparedStatement, session);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return session;
    }

    @Override
    public Session trouver_par_id(Long id) {
        try {
            // Example: Query the database to find a session by ID
            String sql = "SELECT * FROM session WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToSession(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    @Override
    public List<Session> trouver_tout() {
        List<Session> sessions = new ArrayList<>();
        try {
            // Example: Retrieve all sessions from the database
            String sql = "SELECT * FROM session";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapResultSetToSession(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return sessions;
    }

    @Override
    public void supprimer_par_id(Long id) {
        try {
            // Exemple: Supprimer une session par id
            String sql = "DELETE FROM session WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public Session trouver_par_compte_id(Long compte_id) {
        try {
            // Example: Query the database to find a session by ID
            String sql = "SELECT * FROM session WHERE compte_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, compte_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToSession(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    // Refresh the session duration in the database
    public Session refreshSession(Long accountId) {
        try {
            String sql = "UPDATE session SET date_debut = CURRENT_TIMESTAMP WHERE compte_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, accountId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }

        return trouver_par_compte_id(accountId);
    }

    private Session mapResultSetToSession(ResultSet resultSet) throws SQLException {
        return new Session(
                resultSet.getLong("id"),
                resultSet.getLong("compte_id"),
                resultSet.getTimestamp("date_debut"));
    }

    private void prepareSessionStatements(PreparedStatement preparedStatement, Session session) throws SQLException {
        preparedStatement.setLong(1, session.getCompte_id());
    }

    @Override
    public List<Session> trouver_par_page(int page, int items_count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_page'");
    }

    @Override
    public Session modifier(Long id, Map<String, Object> updates) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifier'");
    }

}
