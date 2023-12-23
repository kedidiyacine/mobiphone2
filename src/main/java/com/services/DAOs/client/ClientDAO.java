package com.services.DAOs.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.models.Client;

public class ClientDAO implements ClientRepertoire {
    private final Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client enregistrer(Client client) {
        String sql = "INSERT INTO client (cin, nom, prenom, adresse_de_livraison, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            prepareClientStatements(preparedStatement, client);
            preparedStatement.executeUpdate(); // Execute the insert query
        } catch (SQLException e) {
            // Handle the exception appropriately (log it or rethrow)
            e.printStackTrace(); // This is just for illustration; consider using a logging framework
        }

        return client;
    }

    @Override
    public Client trouver_par_id(Long id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToClient(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> trouver_tout() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                clients.add(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public List<Client> trouver_par_page(int page, int items_count) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY id LIMIT ? OFFSET ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, items_count);
            preparedStatement.setInt(2, (page - 1) * items_count);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clients.add(mapResultSetToClient(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public void supprimer_par_id(Long id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate(); // Execute the delete query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client trouver_par_cin(String cin) {
        String sql = "SELECT * FROM client WHERE cin = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cin);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToClient(resultSet);
                }
            }
        } catch (SQLException e) {
            // Handle the exception appropriately (log it or rethrow)
            e.printStackTrace(); // This is just for illustration; consider using a logging framework
        }

        return null;
    }

    private Client mapResultSetToClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getLong("id"),
                resultSet.getString("cin"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("adresse_de_livraison"),
                resultSet.getString("email"));
    }

    private void prepareClientStatements(PreparedStatement preparedStatement, Client client) throws SQLException {
        preparedStatement.setString(1, client.getCin());
        preparedStatement.setString(2, client.getNom());
        preparedStatement.setString(3, client.getPrenom());
        preparedStatement.setString(4, client.getAdresse_de_livraison());
        preparedStatement.setString(5, client.getEmail());
    }

}
