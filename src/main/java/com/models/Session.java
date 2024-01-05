package com.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Session implements Serializable {
    private Long id;
    private Long compte_id;
    private Compte compte;
    private Timestamp date_debut;

    public Session(Long id, Long compte_id, Timestamp date_debut) {
        this.id = id;
        this.compte_id = compte_id;
        this.date_debut = date_debut;
    }

    public Session(Compte compte) {
        this.compte = compte;
        this.compte_id = compte.getId();
    }

    public Session(Long id, Long compte_id) {
        this.id = id;
        this.compte_id = compte_id;
    }

    public Session(Long compte_id, Timestamp date_debut) {
        this.compte_id = compte_id;
        this.date_debut = date_debut;
    }

    public Session(Long compte_id) {
        this.compte_id = compte_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompte_id() {
        return compte_id;
    }

    public void setCompte_id(Long compte_id) {
        this.compte_id = compte_id;
    }

    public Timestamp getDate_debut() {
        return date_debut;
    }

    public void setdate_debut(Timestamp date_debut) {
        this.date_debut = date_debut;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}
