package com.models;

public interface TelephoneMobileArticle extends Article {
    String getReference();

    void setReference(String ref);

    String getModele();

    void setModele(String modele);

    String getMarque();

    void setMarque(String marque);

}
