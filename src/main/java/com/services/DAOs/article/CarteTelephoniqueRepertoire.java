package com.services.DAOs.article;

import java.util.List;

import com.models.CarteTelephonique;

public interface CarteTelephoniqueRepertoire extends ArticleRepertoire<CarteTelephonique> {
    List<CarteTelephonique> rechercher_par_carte(String carte);

    List<CarteTelephonique> rechercher_par_duree_validite(Integer duree_validite);

    List<CarteTelephonique> rechercher_par_type_carte(String type_carte);

    List<CarteTelephonique> rechercher_par_operateur(String operateur);

}
