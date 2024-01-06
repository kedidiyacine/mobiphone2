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

    public Client enregistrer(Client client) {
        return clientDAO.enregistrer(client);
    }

    public List<Client> getAllByPage(int page, int count) {
        return clientDAO.trouver_par_page(page, count);
    }

    public List<Client> trouver_tout() {
        return clientDAO.trouver_tout();
    }

    public Client modifier(Long id, Map<String, Object> updates) {
        return clientDAO.modifier(id, updates);
    }

    public void supprimer_par_id(Long id) {
        clientDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return clientDAO.count();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed())
            connection.close();
    }

}
