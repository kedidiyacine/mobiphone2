package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.db.DatabaseUtil;
import com.models.Cle3g;
import com.services.DAOs.article.Cle3gDAO;

public class Cle3gService implements DataService<Cle3g>, AutoCloseable {
    Cle3gDAO cle3gDAO;
    Connection connection;

    public Cle3gService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        cle3gDAO = new Cle3gDAO(connection);

    }

    @Override
    public List<Cle3g> getAllByPage(int page, int count) {
        return cle3gDAO.trouver_par_page(page, count);

    }

    @Override
    public Cle3g enregistrer(Cle3g entity) {
        return cle3gDAO.enregistrer(entity);
    }

    @Override
    public Cle3g modifier(Long id, Map<String, Object> updates) {
        return cle3gDAO.modifier(id, updates);
    }

    @Override
    public void supprimer_par_id(Long id) {
        cle3gDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return cle3gDAO.count();
    }

    @Override
    public List<Cle3g> trouver_tout() {
        return cle3gDAO.trouver_tout();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }
}
