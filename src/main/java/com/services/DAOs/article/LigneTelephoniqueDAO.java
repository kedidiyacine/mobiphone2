package com.services.DAOs.article;

import java.util.List;
import java.util.Map;

import java.sql.Connection;

import com.models.LigneTelephonique;
import com.utils.Constants;

public class LigneTelephoniqueDAO implements LigneTelephoniqueRepertoire {
    protected final ArticleDAO<LigneTelephonique> articleDAO;

    public LigneTelephoniqueDAO(Connection connection) {
        this.articleDAO = new ArticleDAO<LigneTelephonique>(connection);
    }

    @Override
    public LigneTelephonique modifier(Long id, Map<String, Object> updates) {
        return articleDAO.modifier(id, updates);
    }

    @Override
    public LigneTelephonique trouver_par_id(Long id, String tablename) {
        throw new UnsupportedOperationException("Unimplemented method 'supprimer_par_id'");
    }

    @Override
    public LigneTelephonique enregistrer(LigneTelephonique telephoneMobile) {
        return articleDAO.enregistrer(telephoneMobile);
    }

    @Override
    public LigneTelephonique trouver_par_id(Long id) {
        return articleDAO.trouver_par_id(id, Constants.LIGNE_TELEPHONIQUE_TABLE_NAME);
    }

    @Override
    public List<LigneTelephonique> trouver_par_page(int page, int items_count) {
        return articleDAO.trouver_par_page(page, items_count, Constants.LIGNE_TELEPHONIQUE_TABLE_NAME);
    }

    @Override
    public List<LigneTelephonique> trouver_tout() {
        return articleDAO.trouver_tout(Constants.LIGNE_TELEPHONIQUE_TABLE_NAME);
    }

    @Override
    public void supprimer_par_id(Long id) {
        articleDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return articleDAO.count(Constants.LIGNE_TELEPHONIQUE_TABLE_NAME);
    }

    @Override
    public List<LigneTelephonique> rechercher_par_numero(String numero) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_numero'");
    }

    @Override
    public List<LigneTelephonique> rechercher_par_operateur(String operateur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_operateur'");
    }

    @Override
    public List<LigneTelephonique> rechercher_par_montant_min_consommation(Double montant_min_consommation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_montant_min_consommation'");
    }

    @Override
    public List<LigneTelephonique> trouver_par_type(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_type'");
    }

    @Override
    public List<LigneTelephonique> trouver_par_libelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_libelle'");
    }

    @Override
    public List<LigneTelephonique> trouver_par_prix_vente(Double prix_vente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_prix_vente'");
    }

}
