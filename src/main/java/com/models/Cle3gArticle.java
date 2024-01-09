package com.models;

public interface Cle3gArticle extends Article {
    String getNumero_serie();

    void setNumero_serie(String numero_serie);

    Double getDebit_connexion();

    void setDebit_connexion(Double debit_connexion);

    Double getCapacite_max_telechargement();

    void setCapacite_max_telechargement(Double capacite_max_telechargement);

}
