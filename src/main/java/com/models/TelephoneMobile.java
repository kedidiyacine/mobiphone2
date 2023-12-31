package com.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class TelephoneMobile extends BaseArticle<TelephoneMobile> implements TelephoneMobileArticle {
    private final StringProperty reference = new SimpleStringProperty();
    private final StringProperty marque = new SimpleStringProperty();
    private final StringProperty modele = new SimpleStringProperty();

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
