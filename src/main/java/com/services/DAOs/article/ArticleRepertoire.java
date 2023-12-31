package com.services.DAOs.article;

import java.util.List;

import com.services.DAOs.CrudRepertoire;

public interface ArticleRepertoire<T> extends CrudRepertoire<T, Long> {
    T trouver_par_id(Long id, String type);

    List<T> trouver_par_type(String type);

    List<T> trouver_par_libelle(String libelle);

    List<T> trouver_par_prix_vente(Double prix_vente);
}
