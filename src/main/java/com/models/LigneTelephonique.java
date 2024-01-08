package com.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.sql.Timestamp;

import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;

public class LigneTelephonique extends BaseArticle<LigneTelephonique> implements LigneTelephoniqueArticle {

    private final StringProperty numero = new SimpleStringProperty();
    private final StringProperty operateur = new SimpleStringProperty();
    private final DoubleProperty montant_min_consommation = new SimpleDoubleProperty();

    public LigneTelephonique() {
        setType("ligne telephonique");
    }

    public LigneTelephonique(Long id, String libelle, Double prix_vente,
            Integer qt_stock, String numero, String operateur, Double montant_min_consommation,
            Timestamp date_creation, Timestamp date_maj) {
        super(id, "ligne telephonique", libelle, prix_vente, qt_stock, date_creation, date_maj);
        setNumero(numero);
        setOperateur(operateur);
        setMontant_min_consommation(montant_min_consommation);
    }

    // JAVAFX Getter and Setters

    public StringProperty numeroProperty() {
        return numero;
    }

    public StringProperty operateurProperty() {
        return operateur;
    }

    public DoubleProperty montant_min_consommationProperty() {
        return montant_min_consommation;
    }

    public String getNumero() {
        return numero.get();
    }

    public String getOperateur() {
        return operateur.get();
    }

    public Double getMontant_min_consommation() {
        return montant_min_consommation.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public void setOperateur(String operateur) {
        this.operateur.set(operateur);
    }

    public void setMontant_min_consommation(Double montant_min_consommation) {
        this.montant_min_consommation.set(montant_min_consommation);
    }

    // Getter and Setters

    @Override
    public ObjectProperty<Timestamp> date_creationProperty() {
        return super.date_creationProperty();
    }

    @Override
    public ObjectProperty<Timestamp> date_majProperty() {
        return super.date_majProperty();
    }

    @Override
    public Timestamp getDate_creation() {
        return super.getDate_creation();
    }

    @Override
    public Timestamp getDate_maj() {
        return super.getDate_maj();
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
    public LongProperty idProperty() {
        return super.idProperty();
    }

    @Override
    public StringProperty libelleProperty() {
        return super.libelleProperty();
    }

    @Override
    public DoubleProperty prix_venteProperty() {
        return super.prix_venteProperty();
    }

    @Override
    public IntegerProperty qt_stockProperty() {
        return super.qt_stockProperty();
    }

    @Override
    public void setDate_creation(Timestamp date_creation) {
        super.setDate_creation(date_creation);
    }

    @Override
    public void setDate_maj(Timestamp date_maj) {
        super.setDate_maj(date_maj);
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public void setLibelle(String libelle) {
        super.setLibelle(libelle);
    }

    @Override
    public void setPrix_vente(Double prix_vente) {
        super.setPrix_vente(prix_vente);
    }

    @Override
    public void setQt_stock(Integer qt_stock) {
        super.setQt_stock(qt_stock);
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public StringProperty typeProperty() {
        return super.typeProperty();
    }

}
