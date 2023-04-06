----------------------
-- Créer les tables
----------------------


BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `STATIONS` (
                                          `id`	INTEGER,
                                          `name`	TEXT NOT NULL,
                                          PRIMARY KEY(`id`)
    );

CREATE TABLE IF NOT EXISTS `LINES` (
                                       `id`	INTEGER,
                                       PRIMARY KEY(`id`)
    );

CREATE TABLE IF NOT EXISTS `STOPS` (
                                       `id_line`	INTEGER NOT NULL,
                                       `id_station`	INTEGER NOT NULL,
                                       `id_order`	INTEGER NOT NULL,
                                       FOREIGN KEY(`id_line`) REFERENCES `LINES`(`id`),
    FOREIGN KEY(`id_station`) REFERENCES `STATIONS`(`id`),
    PRIMARY KEY(`id_line`,`id_station`)
    );


COMMIT;