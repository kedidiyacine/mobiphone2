package com.services.DAOs.article;

import java.util.List;

import com.models.LigneTelephonique;

public interface LigneTelephoniqueRepertoire extends ArticleRepertoire<LigneTelephonique> {

    List<LigneTelephonique> rechercher_par_numero(String numero);

    List<LigneTelephonique> rechercher_par_operateur(String operateur);

    List<LigneTelephonique> rechercher_par_montant_min_consommation(Double montant_min_consommation);

}
