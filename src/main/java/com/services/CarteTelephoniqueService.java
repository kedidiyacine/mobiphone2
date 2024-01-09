package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.db.DatabaseUtil;
import com.models.CarteTelephonique;
import com.services.DAOs.article.CarteTelephoniqueDAO;

public class CarteTelephoniqueService implements DataService<CarteTelephonique>, AutoCloseable {
    Connection connection;
    CarteTelephoniqueDAO carteTelephoniqueDAO;

    public CarteTelephoniqueService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        carteTelephoniqueDAO = new CarteTelephoniqueDAO(connection);

    }

    @Override
    public List<CarteTelephonique> getAllByPage(int page, int count) {
        return carteTelephoniqueDAO.trouver_par_page(page, count);
    }

    @Override
    public CarteTelephonique enregistrer(CarteTelephonique entity) {
        return carteTelephoniqueDAO.enregistrer(entity);
    }

    @Override
    public CarteTelephonique modifier(Long id, Map<String, Object> updates) {
        return carteTelephoniqueDAO.modifier(id, updates);
    }

    @Override
    public void supprimer_par_id(Long id) {
        carteTelephoniqueDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return carteTelephoniqueDAO.count();
    }

    @Override
    public List<CarteTelephonique> trouver_tout() {
        return carteTelephoniqueDAO.trouver_tout();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }

}
