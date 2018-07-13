create table social (
    id INTEGER AUTO_INCREMENT,
    site VARCHAR(100),
    facebook VARCHAR(100),
    twitter VARCHAR(100),
    linkedin VARCHAR(100),
    github VARCHAR(100),
    whatsapp VARCHAR(100),
    google VARCHAR(100),
    youtube VARCHAR(100),
    professor_id   INTEGER UNIQUE,
    primary key(id),
    foreign key(professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO social (professor_id) VALUES (1);
INSERT INTO social (professor_id) VALUES (2);
INSERT INTO social (professor_id) VALUES (3);