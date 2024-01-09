package com.services.DAOs.article;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.models.CarteTelephonique;
import com.utils.Constants;

public class CarteTelephoniqueDAO implements CarteTelephoniqueRepertoire {
    protected final ArticleDAO<CarteTelephonique> articleDAO;

    public CarteTelephoniqueDAO(Connection connection) {
        this.articleDAO = new ArticleDAO<CarteTelephonique>(connection);
    }

    @Override
    public CarteTelephonique enregistrer(CarteTelephonique carte) {
        return articleDAO.enregistrer(carte);

    }

    @Override
    public CarteTelephonique modifier(Long id, Map<String, Object> updates) {
        return articleDAO.modifier(id, updates);
    }

    @Override
    public CarteTelephonique trouver_par_id(Long id, String type) {
        return articleDAO.trouver_par_id(id, Constants.CARTE_TELEPHONIQUE_TABLE_NAME);

    }

    @Override
    public List<CarteTelephonique> trouver_par_page(int page, int items_count) {
        return articleDAO.trouver_par_page(page, items_count, Constants.CARTE_TELEPHONIQUE_TABLE_NAME);

    }

    @Override
    public List<CarteTelephonique> trouver_tout() {
        return articleDAO.trouver_tout(Constants.CARTE_TELEPHONIQUE_TABLE_NAME);

    }

    @Override
    public void supprimer_par_id(Long id) {
        articleDAO.supprimer_par_id(id);

    }

    @Override
    public int count() {
        return articleDAO.count(Constants.CARTE_TELEPHONIQUE_TABLE_NAME);

    }

    @Override
    public List<CarteTelephonique> trouver_par_type(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_type'");
    }

    @Override
    public List<CarteTelephonique> trouver_par_libelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_libelle'");
    }

    @Override
    public List<CarteTelephonique> trouver_par_prix_vente(Double prix_vente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_prix_vente'");
    }

    @Override
    public CarteTelephonique trouver_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_id'");
    }

    @Override
    public List<CarteTelephonique> rechercher_par_carte(String carte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_carte'");
    }

    @Override
    public List<CarteTelephonique> rechercher_par_duree_validite(Integer duree_validite) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_duree_validite'");
    }

    @Override
    public List<CarteTelephonique> rechercher_par_type_carte(String type_carte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_type_carte'");
    }

    @Override
    public List<CarteTelephonique> rechercher_par_operateur(String operateur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_operateur'");
    }

}
