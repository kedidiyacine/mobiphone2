package com.services.DAOs.article;

import com.models.BaseArticle;
import com.services.DAOs.CrudRepertoire;

public interface ArticleRepertoire<T> extends CrudRepertoire<BaseArticle<T>, Long> {
    // par type
    // par libelle
    // par prix_vente
}
