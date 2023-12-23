package com.models;

import java.time.LocalDateTime;

public abstract class BaseArticle implements Article {
    protected Long id;
    protected String type;
    protected String libelle;
    protected Double prix_vente;
    protected int qt_stock;
    protected LocalDateTime date_creation;
    protected LocalDateTime date_maj;

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, int qt_stock) {
        this.id = id;
        this.libelle = libelle;
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
    }

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, int qt_stock,
            LocalDateTime date_creation, LocalDateTime date_maj) {
        this.id = id;
        this.libelle = libelle;
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
        this.date_creation = date_creation;
        this.date_maj = date_maj;
    }

    protected BaseArticle(Long id, String type, Double prix_vente, int qt_stock) {
        this.id = id;
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
    }

    protected BaseArticle(Long id, String type, Double prix_vente, int qt_stock, LocalDateTime date_creation,
            LocalDateTime date_maj) {
        this.id = id;
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
        this.date_creation = date_creation;
        this.date_maj = date_maj;
    }

    protected BaseArticle(String type, String libelle, Double prix_vente, int qt_stock) {
        this.libelle = libelle;
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
    }

    protected BaseArticle(String type, Double prix_vente, int qt_stock) {
        this.prix_vente = prix_vente;
        this.qt_stock = qt_stock;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getPrix_vente() {
        return prix_vente;
    }

    public void setPrix_vente(Double prix_vente) {
        this.prix_vente = prix_vente;
    }

    public int getQt_stock() {
        return qt_stock;
    }

    public void setQt_stock(int qt_stock) {
        this.qt_stock = qt_stock;
    }

    public LocalDateTime getDate_creation() {
        return this.date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }

    public LocalDateTime getDate_maj() {
        return this.date_maj;
    }

    public void setDate_maj(LocalDateTime date_maj) {
        // TODO: consider timezones // LocalDateTime.now();
        this.date_maj = date_maj;
    }

}
