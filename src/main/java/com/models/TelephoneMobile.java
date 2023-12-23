package com.models;

public class TelephoneMobile extends BaseArticle implements TelephoneMobileArticle {
    private String reference;
    private String marque;
    private String modele;

    protected TelephoneMobile(Long id, String libelle, Double prix_vente, int qt_stock, String reference,
            String marque,
            String modele) {
        super(id, "Telephone Mobile", libelle, prix_vente, qt_stock);
        this.reference = reference;
        this.marque = marque;
        this.modele = modele;
    }

    protected TelephoneMobile(Long id, Double prix_vente, int qt_stock, String reference,
            String marque,
            String modele) {
        super(id, "Telephone Mobile", prix_vente, qt_stock);
        this.reference = reference;
        this.marque = marque;
        this.modele = modele;
    }

    protected TelephoneMobile(String libelle, Double prix_vente, int qt_stock, String reference,
            String marque,
            String modele) {
        super("Telephone Mobile", libelle, prix_vente, qt_stock);
        this.reference = reference;
        this.marque = marque;
        this.modele = modele;
    }

    protected TelephoneMobile(Double prix_vente, int qt_stock, String reference,
            String marque,
            String modele) {
        super("Telephone Mobile", prix_vente, qt_stock);
        this.reference = reference;
        this.marque = marque;
        this.modele = modele;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMarque() {
        return this.marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return this.modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

}
