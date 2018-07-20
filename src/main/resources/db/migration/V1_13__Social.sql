create table social (
    id INTEGER AUTO_INCREMENT,
    site TEXT,
    facebook TEXT,
    twitter TEXT,
    linkedin TEXT,
    github TEXT,
    whatsapp TEXT,
    google TEXT,
    youtube TEXT,
    professor_id   INTEGER UNIQUE,
    primary key(id),
    foreign key(professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO social (professor_id) VALUES (1);
INSERT INTO social (professor_id) VALUES (2);
INSERT INTO social (professor_id) VALUES (3);