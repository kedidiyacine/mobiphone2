package com.models;

public interface LigneTelephoniqueArticle extends Article {
    String getNumero();

    void setNumero(String numero);

    String getOperateur();

    void setOperateur(String operateur);

    Double getMontant_min_consommation();

    void setMontant_min_consommation(Double montant_min_consommation);
}
