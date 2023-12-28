package com.services.DAOs.article;

import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.models.BaseArticle;
import com.models.TelephoneMobile;

public class TelephoneMobileDAO implements TelephoneMobileRepertoire {
    private final Connection connection;

    public TelephoneMobileDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public BaseArticle<TelephoneMobile> enregistrer(BaseArticle<TelephoneMobile> entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enregistrer'");
    }

    @Override
    public BaseArticle<TelephoneMobile> trouver_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_id'");
    }

    @Override
    public List<BaseArticle<TelephoneMobile>> trouver_tout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_tout'");
    }

    @Override
    public void supprimer_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer_par_id'");
    }

    @Override
    public List<BaseArticle<TelephoneMobile>> trouver_par_page(int page, int items_count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_page'");
    }

    @Override
    public BaseArticle<TelephoneMobile> modifier(Long id, Map<String, Object> updates) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifier'");
    }

}
