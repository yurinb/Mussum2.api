create table admin_link (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100),
    url VARCHAR(300),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO admin_link (titulo, url) VALUES ("SENAC RS", "google.com");
INSERT INTO admin_link (titulo, url) VALUES ("CURSOS", "google.com");