package com.models;

import java.sql.Timestamp;

public interface Article {
    Long getId();

    public void setId(Long id);

    String getType();

    public void setType(String type);

    Integer getQt_stock();

    public void setQt_stock(Integer qt_stock);

    Double getPrix_vente();

    public void setPrix_vente(Double prix_vente);

    String getLibelle();

    public void setLibelle(String libelle);

    Timestamp getDate_creation();

    public void setDate_creation(Timestamp date_creation);

    Timestamp getDate_maj();

    public void setDate_maj(Timestamp date_maj);

}
