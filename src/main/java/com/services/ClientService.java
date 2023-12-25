package com.services;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.models.Client;
import com.services.DAOs.client.ClientDAO;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    public List<Client> getAllByPage(int page, int count) {
        return clientDAO.trouver_par_page(page, count);
    }

    public Client modifier(Long id, Map<String, Object> updates) {
        return clientDAO.modifier(id, updates);
    }

}
