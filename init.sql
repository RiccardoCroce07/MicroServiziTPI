-- ─────────────────────────────────────────────────────────────
-- Script di inizializzazione automatica del database
-- Eseguito da MySQL SOLO al primo avvio (volume vuoto)
-- ─────────────────────────────────────────────────────────────

GRANT ALL PRIVILEGES ON employeedb.* TO 'appuser'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS employee (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email   VARCHAR(150) UNIQUE,
    age     INT,
    salary  DOUBLE
);

CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    ruolo    VARCHAR(20)  NOT NULL DEFAULT 'USER'
);

-- I dati di esempio vengono inseriti dal DataLoader Java all'avvio di Spring
