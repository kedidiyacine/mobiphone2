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
    date_maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);