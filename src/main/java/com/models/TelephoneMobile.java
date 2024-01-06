package com.models;

import java.time.LocalDateTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TelephoneMobile extends BaseArticle<TelephoneMobile> implements TelephoneMobileArticle {
    private final StringProperty reference = new SimpleStringProperty();
    private final StringProperty marque = new SimpleStringProperty();
    private final StringProperty modele = new SimpleStringProperty();

    public TelephoneMobile() {
        setType("telephone mobile");
    }

    public TelephoneMobile(Long id, String libelle, Double prix_vente, int qt_stock, String reference,
            String marque, String modele) {
        super(id, "telephone mobile", libelle, prix_vente, qt_stock);
        setReference(reference);
        setMarque(marque);
        setModele(modele);

    }

    public TelephoneMobile(Long id, String libelle, Double prix_vente, int qt_stock, String reference,
            String marque, String modele, LocalDateTime date_creation, LocalDateTime date_maj) {
        super(id, "telephone mobile", libelle, prix_vente, qt_stock, date_creation, date_maj);
        setReference(reference);
        setMarque(marque);
        setModele(modele);

    }

    public TelephoneMobile(String libelle, Double prix_vente, int qt_stock, String reference,
            String marque, String modele) {
        super("telephone mobile", libelle, prix_vente, qt_stock);
        setReference(reference);
        setMarque(marque);
        setModele(modele);

    }

    public TelephoneMobile(Long id, String reference,
            String marque, String modele) {
        super(id, "telephone mobile");
        setReference(reference);
        setMarque(marque);
        setModele(modele);

    }

    protected TelephoneMobile(Long id, Double prix_vente, int qt_stock, String reference, String marque,
            String modele) {
        super(id, "telephone mobile", prix_vente, qt_stock);
        setReference(reference);
        setMarque(marque);
        setModele(modele);

    }

    public TelephoneMobile(Long id, String libelle, Double prix_vente, int qt_stock) {
        super(id, "telephone mobile", libelle, prix_vente, qt_stock);
    }

    // Getters and setters for JavaFX properties

    public StringProperty referenceProperty() {
        return reference;
    }

    public StringProperty marqueProperty() {
        return marque;
    }

    public StringProperty modeleProperty() {
        return modele;
    }

    // Getters and setters

    public String getReference() {
        return reference.get();
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    public String getMarque() {
        return marque.get();
    }

    public void setMarque(String marque) {
        this.marque.set(marque);
    }

    public String getModele() {
        return modele.get();
    }

    public void setModele(String modele) {
        this.modele.set(modele);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getLibelle() {
        return super.getLibelle();
    }

    @Override
    public Double getPrix_vente() {
        return super.getPrix_vente();
    }

    @Override
    public Integer getQt_stock() {
        return super.getQt_stock();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public LocalDateTime getDate_creation() {
        return super.getDate_creation();
    }

    @Override
    public LocalDateTime getDate_maj() {
        return super.getDate_maj();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public void setPrix_vente(Double prix_vente) {
        super.setPrix_vente(prix_vente);
    }

    @Override
    public void setLibelle(String libelle) {
        super.setLibelle(libelle);
    }

    @Override
    public void setQt_stock(Integer qt_stock) {
        super.setQt_stock(qt_stock);
    }

    @Override
    public void setDate_creation(LocalDateTime date_creation) {
        super.setDate_creation(date_creation);
    }

    @Override
    public void setDate_maj(LocalDateTime date_maj) {
        super.setDate_maj(date_maj);
    }

}
