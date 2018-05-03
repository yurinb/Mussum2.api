create table professor_link (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(50),
    url VARCHAR(300),
    professor_id INTEGER,
    primary key(id),
    FOREIGN KEY (professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Github", "github.com/angeloluz", 2);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 2", "google.com", 2);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 3", "google.com", 2);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 4", "google.com", 2);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 5", "google.com", 2);

INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Duvidas?", "google.com", 1);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 2", "google.com", 1);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 3", "google.com", 1);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 4", "google.com", 1);
INSERT INTO professor_link (titulo, url, professor_id) VALUES ("Link exemplo 5", "google.com", 1);