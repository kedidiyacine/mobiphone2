package com.models;

import java.time.LocalDateTime;

public class Compte {
    private Long id;
    private String login;
    private String mot_de_passe;
    private String role;
    private LocalDateTime date_creation;
    private LocalDateTime date_maj;

    public Compte(Long id, String login, String mot_de_passe, String role) {
        this.id = id;
        this.login = login;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
    }

    public Compte(Long id, String login, String mot_de_passe, String role, LocalDateTime date_creation,
            LocalDateTime date_maj) {
        this.id = id;
        this.login = login;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
        this.date_creation = date_creation;
        this.date_maj = date_maj;
    }

    public Compte(String login, String mot_de_passe, String role) {
        this.login = login;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
