create table curso (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO curso (titulo) VALUES ("ADS");
INSERT INTO curso (titulo) VALUES ("Redes");
INSERT INTO curso (titulo) VALUES ("Processos Gerenciais");
INSERT INTO curso (titulo) VALUES ("Marketing");