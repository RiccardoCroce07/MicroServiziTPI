-- ─────────────────────────────────────────────────────────────
-- Script di inizializzazione automatica del database
-- Viene eseguito da MySQL al primo avvio del container
-- ─────────────────────────────────────────────────────────────

-- Crea le tabelle
CREATE TABLE IF NOT EXISTS employee (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email   VARCHAR(150),
    age     INT,
    salary  DOUBLE
);

CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Inserisci dati di esempio
INSERT INTO employee (name, cognome, email, age, salary) VALUES
('Mario',     'Rossi',     'mario.rossi@mail.it',     35, 38000),
('Laura',     'Bianchi',   'laura.bianchi@mail.it',   28, 27000),
('Giovanni',  'Verdi',     'giovanni.verdi@mail.it',  42, 46000),
('Sofia',     'Ferrari',   'sofia.ferrari@mail.it',   31, 35000),
('Alessandro','Conti',     'ale.conti@mail.it',       24, 22500),
('Chiara',    'Russo',     'chiara.russo@mail.it',    38, 41000),
('Luca',      'Esposito',  'luca.esposito@mail.it',   27, 26000),
('Martina',   'Romano',    'martina.romano@mail.it',  45, 52000);
