package com.models;

import java.time.LocalDateTime;

public class Client {
    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String adresse_de_livraison;
    private String email;
    private LocalDateTime date_creation;
    private LocalDateTime date_maj;

    public Client(Long id, String cin, String nom, String prenom, String adresse_de_livraison, String email) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse_de_livraison = adresse_de_livraison;
        this.email = email;
    }

    public Client(Long id, String cin, String nom, String prenom, String adresse_de_livraison, String email,
            LocalDateTime date_creation,
            LocalDateTime date_maj) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse_de_livraison = adresse_de_livraison;
        this.email = email;
        this.date_creation = date_creation;
        this.date_maj = date_maj;
    }

    public Client(String cin, String nom, String prenom, String adresse_de_livraison, String email) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse_de_livraison = adresse_de_livraison;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse_de_livraison() {
        return adresse_de_livraison;
    }

    public void setAdresse_de_livraison(String adresse_de_livraison) {
        this.adresse_de_livraison = adresse_de_livraison;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }

    public LocalDateTime getDate_maj() {
        return date_maj;
    }

    public void setDate_maj(LocalDateTime date_maj) {
        this.date_maj = date_maj;
    }

}
