package com.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

import com.utils.Constants;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;

public class CarteTelephonique extends BaseArticle<CarteTelephonique> implements CarteTelephoniqueArticle {

    private final StringProperty code = new SimpleStringProperty();
    private final IntegerProperty duree_validite = new SimpleIntegerProperty();
    private final StringProperty type_carte = new SimpleStringProperty();
    private final StringProperty operateur = new SimpleStringProperty();

    public CarteTelephonique() {
        setType(Constants.CARTE_TELEPHONIQUE_TYPE);
    }

    public CarteTelephonique(Long id, String libelle, Double prix_vente,
            Integer qt_stock, String code, Integer duree_validite, String type_carte, String operateur,
            Timestamp date_creation, Timestamp date_maj) {
        super(id, Constants.CARTE_TELEPHONIQUE_TYPE, libelle, prix_vente, qt_stock, date_creation, date_maj);
        setCode(code);
        setDate_creation(date_creation);
        setType_carte(type_carte);
        setOperateur(operateur);
    }

    // JAVAFX Getter and Setters

    public StringProperty codeProperty() {
        return code;
    }

    public IntegerProperty duree_validiteProperty() {
        return duree_validite;
    }

    public StringProperty type_carteProperty() {
        return type_carte;
    }

    public StringProperty operateurProperty() {
        return operateur;
    }

    public String getCode() {
        return code.get();
    }

    public Integer getDuree_validite() {
        return duree_validite.get();
    }

    public String getType_carte() {
        return type_carte.get();
    }

    public String getOperateur() {
        return operateur.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setDuree_validite(Integer duree_validite) {
        this.duree_validite.set(duree_validite);
    }

    public void setType_carte(String type_carte) {
        this.type_carte.set(type_carte);
    }

    public void setOperateur(String operateur) {
        this.operateur.set(operateur);
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
