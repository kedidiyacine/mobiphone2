package com.services.DAOs.article;

import java.util.List;

import com.models.TelephoneMobile;

public interface TelephoneMobileRepertoire extends ArticleRepertoire<TelephoneMobile> {
    List<TelephoneMobile> rechercher_par_marque(String marque);

    List<TelephoneMobile> rechercher_par_modele(String modele);

    List<TelephoneMobile> rechercher_par_reference(String reference);

}
