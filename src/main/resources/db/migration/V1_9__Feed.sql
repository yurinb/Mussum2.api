create table feed (
    id INTEGER AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL,
    professor VARCHAR(300) NOT NULL,
    data_criacao VARCHAR(100),
    titulo VARCHAR(100) NOT NULL,
    comentario VARCHAR(300) NOT NULL,
    arquivo VARCHAR(100) NOT NULL,
    link VARCHAR(500) NOT NULL,
    dir VARCHAR(500) NOT NULL,
    
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';