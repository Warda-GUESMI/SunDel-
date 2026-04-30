-- ============================================================
-- BASE DE DONNÉES : BDG_LivraisonCom_25
-- ============================================================

CREATE DATABASE IF NOT EXISTS BDG_LivraisonCom_25;
USE BDG_LivraisonCom_25;

-- Table Postes
CREATE TABLE Postes (
    codeposte VARCHAR(20) PRIMARY KEY,
    libelle   VARCHAR(100) NOT NULL,
    indice    INT DEFAULT 1
);

INSERT INTO Postes VALUES
('controleur', 'Contrôleur des livraisons', 2),
('livreur',    'Livreur',                  1);

-- Table Personnel
CREATE TABLE Personnel (
    idpers      INT AUTO_INCREMENT PRIMARY KEY,
    nompers     VARCHAR(100) NOT NULL,
    prenompers  VARCHAR(100) NOT NULL,
    adrpers     VARCHAR(255),
    villepers   VARCHAR(100),
    telpers     VARCHAR(20),
    d_embauche  DATE,
    Login       VARCHAR(50)  NOT NULL UNIQUE,
    motP        VARCHAR(255) NOT NULL,   -- stocker un hash bcrypt
    codeposte   VARCHAR(20)  NOT NULL,
    FOREIGN KEY (codeposte) REFERENCES Postes(codeposte)
);

-- Données de test
INSERT INTO Personnel (nompers,prenompers,adrpers,villepers,telpers,d_embauche,Login,motP,codeposte) VALUES
('Ben Ali',   'Sami',  '12 rue de la Paix', 'Tunis',  '55001122', '2022-01-10', 'ctrl1',    '$2b$10$examplehash1', 'controleur'),
('Trabelsi',  'Rami',  '5 avenue Bourguiba','Sfax',   '55003344', '2022-03-15', 'livr1',    '$2b$10$examplehash2', 'livreur'),
('Mansouri',  'Karim', '8 rue Ibn Khaldoun','Sousse', '55005566', '2023-06-01', 'livr2',    '$2b$10$examplehash3', 'livreur');

-- Table Clients
CREATE TABLE Clients (
    noclt       INT AUTO_INCREMENT PRIMARY KEY,
    nomclt      VARCHAR(100) NOT NULL,
    prenomclt   VARCHAR(100),
    adrclt      VARCHAR(255),
    villeclt    VARCHAR(100),
    code_postal VARCHAR(10),
    telclt      VARCHAR(20),
    adrmail     VARCHAR(150)
);

INSERT INTO Clients (nomclt,prenomclt,adrclt,villeclt,code_postal,telclt,adrmail) VALUES
('Chaabane', 'Ahmed',   '3 rue Habib', 'Tunis',  '1001', '22001100', 'ahmed@mail.com'),
('Karray',   'Fatma',   '7 rue Zitoun','Sfax',   '3000', '22002200', 'fatma@mail.com'),
('Baccouche','Mohamed', '1 rue Jemaa', 'Sousse', '4000', '22003300', 'med@mail.com');

-- Table Articles
CREATE TABLE Articles (
    refart      VARCHAR(20) PRIMARY KEY,
    designation VARCHAR(200) NOT NULL,
    prixA       DECIMAL(10,3),
    prixV       DECIMAL(10,3),
    codetva     INT DEFAULT 19,
    categorie   VARCHAR(100),
    qtestk      INT DEFAULT 0
);

INSERT INTO Articles VALUES
('ART001', 'Produit A', 5.000, 8.500, 19, 'Général', 100),
('ART002', 'Produit B', 3.200, 6.000, 19, 'Général', 200),
('ART003', 'Produit C', 7.000, 12.000,19, 'Spécial', 50);

-- Table Commandes
CREATE TABLE Commandes (
    nocde   INT AUTO_INCREMENT PRIMARY KEY,
    noclt   INT NOT NULL,
    datecde DATE NOT NULL,
    etatcde VARCHAR(30) DEFAULT 'en_attente',
    FOREIGN KEY (noclt) REFERENCES Clients(noclt)
);

-- Table LigCdes (lignes de commande)
CREATE TABLE LigCdes (
    nocde   INT NOT NULL,
    refart  VARCHAR(20) NOT NULL,
    qtecde  INT NOT NULL DEFAULT 1,
    PRIMARY KEY (nocde, refart),
    FOREIGN KEY (nocde)  REFERENCES Commandes(nocde),
    FOREIGN KEY (refart) REFERENCES Articles(refart)
);

-- Table LivraisonCom
CREATE TABLE LivraisonCom (
    nocde     INT PRIMARY KEY,
    dateliv   DATE NOT NULL,
    livreur   INT  NOT NULL,
    modepay   VARCHAR(50) DEFAULT 'especes',
    etatliv   VARCHAR(30) DEFAULT 'en_cours',
    remarques TEXT,
    ordre_livraison INT DEFAULT 1,
    FOREIGN KEY (nocde)   REFERENCES Commandes(nocde),
    FOREIGN KEY (livreur) REFERENCES Personnel(idpers)
);

-- Table Messages
CREATE TABLE Messages (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    expediteur_id  INT NOT NULL,
    destinataire_id INT NOT NULL,
    contenu        TEXT NOT NULL,
    type           VARCHAR(20) DEFAULT 'info',   -- 'info' | 'urgence'
    nocde          INT,
    date_envoi     DATETIME DEFAULT NOW(),
    lu             TINYINT(1) DEFAULT 0,
    FOREIGN KEY (expediteur_id)   REFERENCES Personnel(idpers),
    FOREIGN KEY (destinataire_id) REFERENCES Personnel(idpers)
);

-- ============================================================
-- VUE utile pour les livraisons complètes
-- ============================================================
CREATE OR REPLACE VIEW v_livraisons_completes AS
SELECT
    lc.nocde,
    lc.dateliv,
    lc.livreur         AS livreur_id,
    CONCAT(p.prenompers,' ',p.nompers) AS livreur_nom,
    lc.modepay,
    lc.etatliv,
    lc.remarques,
    lc.ordre_livraison,
    c.noclt,
    c.nomclt           AS client_nom,
    c.prenomclt        AS client_prenom,
    c.telclt           AS client_tel,
    c.adrclt           AS client_adresse,
    c.villeclt         AS client_ville,
    c.code_postal      AS client_code_postal,
    cmd.datecde,
    COALESCE(SUM(a.prixV * lg.qtecde), 0)  AS montant_total,
    COALESCE(SUM(lg.qtecde), 0)            AS nb_articles
FROM LivraisonCom lc
JOIN Commandes  cmd ON cmd.nocde  = lc.nocde
JOIN Clients    c   ON c.noclt    = cmd.noclt
JOIN Personnel  p   ON p.idpers   = lc.livreur
LEFT JOIN LigCdes  lg ON lg.nocde  = lc.nocde
LEFT JOIN Articles a  ON a.refart  = lg.refart
GROUP BY lc.nocde, lc.dateliv, lc.livreur, livreur_nom, lc.modepay,
         lc.etatliv, lc.remarques, lc.ordre_livraison,
         c.noclt, client_nom, client_prenom, client_tel,
         client_adresse, client_ville, client_code_postal, cmd.datecde;

-- Données de test
INSERT INTO Commandes (noclt, datecde, etatcde) VALUES (1, CURDATE(), 'validee');
INSERT INTO Commandes (noclt, datecde, etatcde) VALUES (2, CURDATE(), 'validee');
INSERT INTO LigCdes VALUES (1,'ART001',2),(1,'ART002',1);
INSERT INTO LigCdes VALUES (2,'ART003',3);
INSERT INTO LivraisonCom VALUES (1, CURDATE(), 2, 'especes',  'en_cours', NULL, 1);
INSERT INTO LivraisonCom VALUES (2, CURDATE(), 2, 'cheque',   'en_cours', NULL, 2);
