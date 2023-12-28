package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.db.DatabaseUtil;
import com.models.Client;
import com.services.DAOs.client.ClientDAO;

public class ClientService implements DataService<Client>, AutoCloseable {
    private ClientDAO clientDAO;
    Connection connection;

    public ClientService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        this.clientDAO = new ClientDAO(connection);
    }

    public List<Client> getAllByPage(int page, int count) {
        return clientDAO.trouver_par_page(page, count);
    }

    public Client modifier(Long id, Map<String, Object> updates) {
        return clientDAO.modifier(id, updates);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

}
