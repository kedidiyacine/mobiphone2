package com.services.DAOs.article;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.models.Cle3g;
import com.utils.Constants;

public class Cle3gDAO implements Cle3gRepertoire {
    protected final ArticleDAO<Cle3g> articleDAO;

    public Cle3gDAO(Connection connection) {
        this.articleDAO = new ArticleDAO<Cle3g>(connection);
    }

    @Override
    public Cle3g enregistrer(Cle3g cle3g) {
        return articleDAO.enregistrer(cle3g);

    }

    @Override
    public Cle3g modifier(Long id, Map<String, Object> updates) {
        return articleDAO.modifier(id, updates);
    }

    @Override
    public List<Cle3g> trouver_par_page(int page, int items_count) {
        return articleDAO.trouver_par_page(page, items_count, Constants.CLE_3G_TABLE_NAME);
    }

    @Override
    public List<Cle3g> trouver_tout() {
        return articleDAO.trouver_tout(Constants.CLE_3G_TABLE_NAME);
    }

    @Override
    public void supprimer_par_id(Long id) {
        articleDAO.supprimer_par_id(id);
    }

    @Override
    public int count() {
        return articleDAO.count(Constants.CLE_3G_TABLE_NAME);
    }

    @Override
    public Cle3g trouver_par_id(Long id, String type) {
        return articleDAO.trouver_par_id(id, Constants.CLE_3G_TABLE_NAME);

    }

    @Override
    public List<Cle3g> trouver_par_type(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_type'");
    }

    @Override
    public List<Cle3g> trouver_par_libelle(String libelle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_libelle'");
    }

    @Override
    public List<Cle3g> trouver_par_prix_vente(Double prix_vente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_prix_vente'");
    }

    @Override
    public Cle3g trouver_par_id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouver_par_id'");
    }

    @Override
    public List<Cle3g> rechercher_par_numero_serie(String numero_serie) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_numero_serie'");
    }

    @Override
    public List<Cle3g> rechercher_par_debit_connexion(Double debit_connexion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_debit_connexion'");
    }

    @Override
    public List<Cle3g> rechercher_par_capacite_max_telechargement(Double capacite_max_telechargement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rechercher_par_capacite_max_telechargement'");
    }

}
