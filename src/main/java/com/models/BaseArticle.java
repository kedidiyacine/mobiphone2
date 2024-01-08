package com.models;

import java.sql.Timestamp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class BaseArticle<T> implements Article, Identifiable<T, Long> {
    protected final LongProperty id = new SimpleLongProperty();
    protected final StringProperty type = new SimpleStringProperty();
    protected final StringProperty libelle = new SimpleStringProperty();
    protected final DoubleProperty prix_vente = new SimpleDoubleProperty();
    protected final IntegerProperty qt_stock = new SimpleIntegerProperty();
    protected final ObjectProperty<Timestamp> date_creation = new SimpleObjectProperty<Timestamp>();
    protected final ObjectProperty<Timestamp> date_maj = new SimpleObjectProperty<Timestamp>();

    protected BaseArticle() {
    }

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, Integer qt_stock) {
        setId(id);
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, Integer qt_stock,
            Timestamp date_creation, Timestamp date_maj) {
        setId(id);
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(String type, String libelle, Double prix_vente, Integer qt_stock,
            Timestamp date_creation, Timestamp date_maj) {
        this(type, libelle, prix_vente, qt_stock);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(Long id, String type, Double prix_vente, Integer qt_stock) {
        setId(id);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type, Double prix_vente, Integer qt_stock, Timestamp date_creation,
            Timestamp date_maj) {
        this(id, type, prix_vente, qt_stock);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(String type, String libelle, Double prix_vente, Integer qt_stock) {
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(String type, Double prix_vente, Integer qt_stock) {
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type) {
        setId(id);
        setType(type);
    }

    // Getters and setters for JavaFX properties
    public StringProperty typeProperty() {
        return type;
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty libelleProperty() {
        return libelle;
    }

    public DoubleProperty prix_venteProperty() {
        return prix_vente;
    }

    public IntegerProperty qt_stockProperty() {
        return qt_stock;
    }

    public ObjectProperty<Timestamp> date_creationProperty() {
        return date_creation;
    }

    public ObjectProperty<Timestamp> date_majProperty() {
        return date_maj;
    }

    // Getters and Setters

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getLibelle() {
        return libelle.get();
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    public Double getPrix_vente() {
        return prix_vente.get();
    }

    public void setPrix_vente(Double prix_vente) {
        this.prix_vente.set(prix_vente);
    }

    public Integer getQt_stock() {
        return qt_stock.get();
    }

    public void setQt_stock(Integer qt_stock) {
        this.qt_stock.set(qt_stock);
    }

    public Timestamp getDate_creation() {
        return date_creation.get();
    }

    public void setDate_creation(Timestamp date_creation) {
        this.date_creation.set(date_creation);
    }

    public Timestamp getDate_maj() {
        return date_maj.get();
    }

    public void setDate_maj(Timestamp date_maj) {
        this.date_maj.set(date_maj);
    }

}
