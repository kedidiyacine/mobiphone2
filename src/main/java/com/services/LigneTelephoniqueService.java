package com.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.db.DatabaseUtil;
import com.models.LigneTelephonique;
import com.services.DAOs.article.LigneTelephoniqueDAO;

public class LigneTelephoniqueService implements DataService<LigneTelephonique>, AutoCloseable {
    private LigneTelephoniqueDAO ligneTelephoniqueDAO;
    Connection connection;

    public LigneTelephoniqueService() throws SQLException {
        connection = DatabaseUtil.getConnection();
        ligneTelephoniqueDAO = new LigneTelephoniqueDAO(connection);
    }

    public LigneTelephonique enregistrer(LigneTelephonique ligne) {
        return ligneTelephoniqueDAO.enregistrer(ligne);
    }

    @Override
    public List<LigneTelephonique> getAllByPage(int page, int count) {
        return ligneTelephoniqueDAO.trouver_par_page(page, count);
    }

    public LigneTelephonique modifier(Long id, Map<String, Object> updates) {
        return ligneTelephoniqueDAO.modifier(id, updates);
    }

    public void supprimer_par_id(Long id) {
        ligneTelephoniqueDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return ligneTelephoniqueDAO.count();
    }

    @Override
    public List<LigneTelephonique> trouver_tout() {
        return ligneTelephoniqueDAO.trouver_tout();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed()) {
            connection.close();
        }
    }

}
