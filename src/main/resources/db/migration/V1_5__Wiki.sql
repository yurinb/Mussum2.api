create table wiki (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO wiki (titulo, url) VALUES ("wiki A", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki B", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki C", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki D", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki E", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki F", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki G", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki H", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki I", "google.com");
INSERT INTO wiki (titulo, url) VALUES ("wiki J", "google.com");