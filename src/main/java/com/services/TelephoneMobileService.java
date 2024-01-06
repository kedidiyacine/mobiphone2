package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.db.DatabaseUtil;
import com.models.TelephoneMobile;
import com.services.DAOs.article.TelephoneMobileDAO;

public class TelephoneMobileService implements DataService<TelephoneMobile>, AutoCloseable {
    private TelephoneMobileDAO telephoneMobileDAO;
    Connection connection;

    public TelephoneMobileService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        telephoneMobileDAO = new TelephoneMobileDAO(connection);
    }

    public TelephoneMobile enregistrer(TelephoneMobile telephoneMobile) {
        return telephoneMobileDAO.enregistrer(telephoneMobile);
    }

    @Override
    public List<TelephoneMobile> getAllByPage(int page, int count) {
        return telephoneMobileDAO.trouver_par_page(page, count);
    }

    public TelephoneMobile modifier(Long id, Map<String, Object> updates) {
        return telephoneMobileDAO.modifier(id, updates);
    }

    public void supprimer_par_id(Long id) {
        telephoneMobileDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return telephoneMobileDAO.count();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public List<TelephoneMobile> trouver_tout() {
        return telephoneMobileDAO.trouver_tout();
    }

}
