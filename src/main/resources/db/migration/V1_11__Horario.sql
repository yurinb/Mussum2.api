create table horario (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO horario (titulo, url) VALUES ("ADS Manhã", "http://187.7.106.14/horarios/horarios_2017_2_AM.html");
INSERT INTO horario (titulo, url) VALUES ("ADS Noite", "http://187.7.106.14/horarios/horarios_2017_2_AN.html");
INSERT INTO horario (titulo, url) VALUES ("REDES Manhã", "http://187.7.106.14/horarios/horarios_2017_2_RM.html");
INSERT INTO horario (titulo, url) VALUES ("REDES Noite", "http://187.7.106.14/horarios/horarios_2017_2_RN.html");