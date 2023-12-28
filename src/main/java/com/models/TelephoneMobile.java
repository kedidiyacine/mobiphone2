package com.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TelephoneMobile extends BaseArticle<TelephoneMobile> implements TelephoneMobileArticle {
    private final StringProperty reference = new SimpleStringProperty();
    private final StringProperty marque = new SimpleStringProperty();
    private final StringProperty modele = new SimpleStringProperty();

    protected TelephoneMobile(Long id, String libelle, Double prix_vente, int qt_stock, String reference,
            String marque, String modele) {
        super(id, "telephone mobile", libelle, prix_vente, qt_stock);
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

    protected TelephoneMobile(String libelle, Double prix_vente, int qt_stock, String reference, String marque,
            String modele) {
        super("telephone mobile", libelle, prix_vente, qt_stock);
        setReference(reference);
        setMarque(marque);
        setModele(modele);
    }

    protected TelephoneMobile(Double prix_vente, int qt_stock, String reference, String marque, String modele) {
        super("telephone mobile", prix_vente, qt_stock);
        setReference(reference);
        setMarque(marque);
        setModele(modele);
    }

    // Getters and setters for JavaFX properties

    public String getReference() {
        return reference.get();
    }

    public StringProperty referenceProperty() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    public String getMarque() {
        return marque.get();
    }

    public StringProperty marqueProperty() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque.set(marque);
    }

    public String getModele() {
        return modele.get();
    }

    public StringProperty modeleProperty() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele.set(modele);
    }
}
