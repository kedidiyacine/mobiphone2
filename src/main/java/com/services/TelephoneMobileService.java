package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.db.DatabaseUtil;
import com.models.TelephoneMobile;
import com.services.DAOs.article.TelephoneMobileDAO;

public class TelephoneMobileService implements DataService<TelephoneMobile>, AutoCloseable {
    private TelephoneMobileDAO telephoneMobileDAO;
    Connection connection;

    public TelephoneMobileService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        // articleDAO = new ArticleDAO(connection);

    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public List<TelephoneMobile> getAllByPage(int page, int count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllByPage'");
    }

}
