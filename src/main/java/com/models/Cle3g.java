package com.models;

import java.sql.Timestamp;

import com.utils.Constants;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cle3g extends BaseArticle<Cle3g> implements Cle3gArticle {

    private final StringProperty numero_serie = new SimpleStringProperty();
    private final DoubleProperty debit_connexion = new SimpleDoubleProperty();
    private final DoubleProperty capacite_max_telechargement = new SimpleDoubleProperty();

    public Cle3g() {
        setType(Constants.CLE_3G_TYPE);
    }

    public Cle3g(Long id, String libelle, Double prix_vente,
            Integer qt_stock, String numero_serie, Double debit_connexion, Double capacite_max_telechargement,
            Timestamp date_creation, Timestamp date_maj) {
        super(id, Constants.CLE_3G_TYPE, libelle, prix_vente, qt_stock, date_creation, date_maj);
        setNumero_serie(numero_serie);
        setDebit_connexion(debit_connexion);
        setCapacite_max_telechargement(capacite_max_telechargement);
    }

    // JAVAFX Getter and Setters

    public StringProperty numero_serieProperty() {
        return numero_serie;
    }

    public DoubleProperty debit_connexionProperty() {
        return debit_connexion;
    }

    public DoubleProperty capacite_max_telechargementProperty() {
        return capacite_max_telechargement;
    }

    public String getNumero_serie() {
        return numero_serie.get();
    }

    public Double getDebit_connexion() {
        return debit_connexion.get();
    }

    public Double getCapacite_max_telechargement() {
        return capacite_max_telechargement.get();
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie.set(numero_serie);
    }

    public void setDebit_connexion(Double debit_connexion) {
        this.debit_connexion.set(debit_connexion);
    }

    public void setCapacite_max_telechargement(Double capacite_max_telechargement) {
        this.capacite_max_telechargement.set(capacite_max_telechargement);
    }

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

    // Getter and Setters

}
