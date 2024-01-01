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

    @Override
    public List<TelephoneMobile> getAllByPage(int page, int count) {
        return telephoneMobileDAO.trouver_par_page(page, count);
    }

    public TelephoneMobile modifier(Long id, Map<String, Object> updates) {
        return telephoneMobileDAO.modifier(id, updates);
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }

}
