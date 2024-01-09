package com.models;

public interface CarteTelephoniqueArticle extends Article {

    String getCode();

    void setCode(String code);

    Integer getDuree_validite();

    void setDuree_validite(Integer duree_validite);

    String getType_carte();

    void setType_carte(String type_carte);

    String getOperateur();

    void setOperateur(String operateur);

}
