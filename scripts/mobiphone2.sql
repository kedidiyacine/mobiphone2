-- DROP DATABASE mobiphone2;
-- Création de la base de données
CREATE DATABASE IF NOT EXISTS mobiphone2;

USE mobiphone2;

-- Création de la table "compte"
CREATE TABLE IF NOT EXISTS compte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM(
        'responsable commercial',
        'agent commercial',
        'comptable'
    ) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_login (login),
    INDEX idx_role (role)
);

-- Création de la table "client"
CREATE TABLE IF NOT EXISTS client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cin VARCHAR(20) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    adresse_de_livraison VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cin (cin),
    INDEX idx_nom_prenom (nom, prenom)
);

-- Création de la table "session"
CREATE TABLE IF NOT EXISTS session (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compte_id INT,
    date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (compte_id) REFERENCES compte(id),
    INDEX idx_compte_id (compte_id)
);

-- Création de la table "article"
CREATE TABLE IF NOT EXISTS article (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    libelle VARCHAR(255),
    qt_stock INT NOT NULL,
    prix_vente DOUBLE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table "telephone_mobile"
CREATE TABLE IF NOT EXISTS telephone_mobile (
    id_article INT PRIMARY KEY,
    reference VARCHAR(255),
    marque VARCHAR(255),
    modele VARCHAR(255),
    FOREIGN KEY (id_article) REFERENCES article(id)
);

-- Création de la table "ligne_telephonique"
CREATE TABLE IF NOT EXISTS ligne_telephonique (
    id_article INT PRIMARY KEY,
    numero VARCHAR(255),
    operateur VARCHAR(255),
    montant_min_consommation DOUBLE,
    FOREIGN KEY (id_article) REFERENCES article(id)
);

-- Création de la table "cle_3g"
CREATE TABLE IF NOT EXISTS cle_3g (
    id_article INT PRIMARY KEY,
    numero_serie VARCHAR(255),
    debit_connexion DOUBLE,
    capacite_max_telechargement DOUBLE,
    FOREIGN KEY (id_article) REFERENCES article(id)
);

-- Création de la table "carte_telephonique"
CREATE TABLE IF NOT EXISTS carte_telephonique (
    id_article INT PRIMARY KEY,
    code VARCHAR(255),
    duree_validite INT,
    type_carte VARCHAR(25),
    operateur VARCHAR(255),
    FOREIGN KEY (id_article) REFERENCES article(id)
);