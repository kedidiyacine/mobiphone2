package com.models;

import java.time.LocalDateTime;
import javafx.beans.property.*;

public abstract class BaseArticle<T> implements Article, Identifiable<T, Long> {
    protected final LongProperty id = new SimpleLongProperty();
    protected final StringProperty type = new SimpleStringProperty();
    protected final StringProperty libelle = new SimpleStringProperty();
    protected final DoubleProperty prix_vente = new SimpleDoubleProperty();
    protected final IntegerProperty qt_stock = new SimpleIntegerProperty();
    protected final ObjectProperty<LocalDateTime> date_creation = new SimpleObjectProperty<>();
    protected final ObjectProperty<LocalDateTime> date_maj = new SimpleObjectProperty<>();

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, int qt_stock) {
        setId(id);
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type, String libelle, Double prix_vente, int qt_stock,
            LocalDateTime date_creation, LocalDateTime date_maj) {
        setId(id);
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(String type, String libelle, Double prix_vente, int qt_stock,
            LocalDateTime date_creation, LocalDateTime date_maj) {
        this(type, libelle, prix_vente, qt_stock);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(Long id, String type, Double prix_vente, int qt_stock) {
        setId(id);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type, Double prix_vente, int qt_stock, LocalDateTime date_creation,
            LocalDateTime date_maj) {
        this(id, type, prix_vente, qt_stock);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    protected BaseArticle(String type, String libelle, Double prix_vente, int qt_stock) {
        setLibelle(libelle);
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(String type, Double prix_vente, int qt_stock) {
        setPrix_vente(prix_vente);
        setQt_stock(qt_stock);
        setType(type);
    }

    protected BaseArticle(Long id, String type) {
        setId(id);
        setType(type);
    }

    // Getters and setters for JavaFX properties

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    @Override
    public Long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getLibelle() {
        return libelle.get();
    }

    public StringProperty libelleProperty() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    public Double getPrix_vente() {
        return prix_vente.get();
    }

    public DoubleProperty prix_venteProperty() {
        return prix_vente;
    }

    public void setPrix_vente(Double prix_vente) {
        this.prix_vente.set(prix_vente);
    }

    public int getQt_stock() {
        return qt_stock.get();
    }

    public IntegerProperty qt_stockProperty() {
        return qt_stock;
    }

    public void setQt_stock(int qt_stock) {
        this.qt_stock.set(qt_stock);
    }

    public LocalDateTime getDate_creation() {
        return date_creation.get();
    }

    public ObjectProperty<LocalDateTime> date_creationProperty() {
        return date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation.set(date_creation);
    }

    public LocalDateTime getDate_maj() {
        return date_maj.get();
    }

    public ObjectProperty<LocalDateTime> date_majProperty() {
        return date_maj;
    }

    public void setDate_maj(LocalDateTime date_maj) {
        this.date_maj.set(date_maj);
    }
}
