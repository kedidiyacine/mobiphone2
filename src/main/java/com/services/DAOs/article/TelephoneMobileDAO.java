package com.services.DAOs.article;

import java.util.List;
import java.util.Map;

import java.sql.Connection;

import com.models.TelephoneMobile;
import com.utils.Constants;

public class TelephoneMobileDAO implements TelephoneMobileRepertoire {
    protected final ArticleDAO<TelephoneMobile> articleDAO;

    public TelephoneMobileDAO(Connection connection) {
        this.articleDAO = new ArticleDAO<TelephoneMobile>(connection);
    }

    @Override
    public TelephoneMobile modifier(Long id, Map<String, Object> updates) {
        return articleDAO.modifier(id, updates);
    }

    @Override
    public TelephoneMobile trouver_par_id(Long id, String tablename) {
        throw new UnsupportedOperationException("Unimplemented method 'supprimer_par_id'");
    }

    @Override
    public TelephoneMobile enregistrer(TelephoneMobile telephoneMobile) {
        return articleDAO.enregistrer(telephoneMobile);
    }

    @Override
    public TelephoneMobile trouver_par_id(Long id) {
        return articleDAO.trouver_par_id(id, Constants.TELEPHONE_TABLE_NAME);
    }

    @Override
    public List<TelephoneMobile> trouver_par_page(int page, int items_count) {
        return articleDAO.trouver_par_page(page, items_count, Constants.TELEPHONE_TABLE_NAME);
    }

    @Override
    public List<TelephoneMobile> trouver_tout() {
        return articleDAO.trouver_tout(Constants.TELEPHONE_TABLE_NAME);
    }

    @Override
    public void supprimer_par_id(Long id) {
        articleDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return articleDAO.count(Constants.TELEPHONE_TABLE_NAME);
    }

    @Override
    public List<TelephoneMobile> rechercher_par_marque(String marque) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_marque'");
    }

    @Override
    public List<TelephoneMobile> rechercher_par_modele(String modele) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_modele'");
    }

    @Override
    public List<TelephoneMobile> rechercher_par_reference(String reference) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_reference'");
    }

    @Override
    public List<TelephoneMobile> trouver_par_type(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_type'");
    }

    @Override
    public List<TelephoneMobile> trouver_par_libelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_libelle'");
    }

    @Override
    public List<TelephoneMobile> trouver_par_prix_vente(Double prix_vente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_prix_vente'");
    }

}
