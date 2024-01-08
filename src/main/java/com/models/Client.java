package com.models;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Client implements Identifiable<Client, Long> {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty cin = new SimpleStringProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty adresse_de_livraison = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final ObjectProperty<Timestamp> date_creation = new SimpleObjectProperty<>();
    private final ObjectProperty<Timestamp> date_maj = new SimpleObjectProperty<>();

    public Client() {
    }

    public Client(Long id, String cin, String nom, String prenom, String adresse_de_livraison, String email) {
        setId(id);
        setCin(cin);
        setNom(nom);
        setPrenom(prenom);
        setAdresse_de_livraison(adresse_de_livraison);
        setEmail(email);
    }

    public Client(Long id, String cin, String nom, String prenom, String adresse_de_livraison, String email,
            Timestamp date_creation, Timestamp date_maj) {

        this(id, cin, nom, prenom, adresse_de_livraison, email);
        setDate_creation(date_creation);
        setDate_maj(date_maj);
    }

    public Client(String cin, String nom, String prenom, String adresse_de_livraison, String email) {
        setCin(cin);
        setNom(nom);
        setPrenom(prenom);
        setAdresse_de_livraison(adresse_de_livraison);
        setEmail(email);
    }

    // Getters and setters for JavaFX properties

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty cinProperty() {
        return cin;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public StringProperty adresse_de_livraisonProperty() {
        return adresse_de_livraison;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<Timestamp> date_creationProperty() {
        return date_creation;
    }

    public ObjectProperty<Timestamp> date_majProperty() {
        return date_maj;
    }

    // Getters and setters
    @Override
    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getCin() {
        return cin.get();
    }

    public void setCin(String cin) {
        this.cin.set(cin);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getAdresse_de_livraison() {
        return adresse_de_livraison.get();
    }

    public void setAdresse_de_livraison(String adresse_de_livraison) {
        this.adresse_de_livraison.set(adresse_de_livraison);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
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
