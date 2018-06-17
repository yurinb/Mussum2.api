create table arquivo (
    id INTEGER AUTO_INCREMENT,
    dir VARCHAR(500) NOT NULL,
    nome VARCHAR(300) NOT NULL,
    visivel BOOLEAN DEFAULT 0,
    comentario VARCHAR(500),
    link VARCHAR(500),
    data_criacao DATE,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';