package com.services.DAOs.article;

import java.util.List;

import com.models.Cle3g;

public interface Cle3gRepertoire extends ArticleRepertoire<Cle3g> {
    List<Cle3g> rechercher_par_numero_serie(String numero_serie);

    List<Cle3g> rechercher_par_debit_connexion(Double debit_connexion);

    List<Cle3g> rechercher_par_capacite_max_telechargement(Double capacite_max_telechargement);

}
