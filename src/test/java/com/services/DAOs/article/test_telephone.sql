DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS telephone_mobile;


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
    reference VARCHAR(255) UNIQUE,
    marque VARCHAR(255),
    modele VARCHAR(255),
    FOREIGN KEY (id_article) REFERENCES article(id)
);