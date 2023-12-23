package com.models;

import java.time.LocalDateTime;

public class Session {
    private Long id;
    private Long compte_id;
    private Compte compte;
    private LocalDateTime date_creation;

    public Session(Long id, Long compte_id, LocalDateTime date_creation) {
        this.id = id;
        this.compte_id = compte_id;
        this.date_creation = date_creation;
    }

    public Session(Compte compte) {
        this.compte = compte;
        this.compte_id = compte.getId();
    }

    public Session(Long id, Long compte_id) {
        this.id = id;
        this.compte_id = compte_id;
    }

    public Session(Long compte_id, LocalDateTime date_creation) {
        this.compte_id = compte_id;
        this.date_creation = date_creation;
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

    public Long getcompte_id() {
        return compte_id;
    }

    public void setcompte_id(Long compte_id) {
        this.compte_id = compte_id;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }

    public Long getCompte_id() {
        return compte_id;
    }

    public void setCompte_id(Long compte_id) {
        this.compte_id = compte_id;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

}
