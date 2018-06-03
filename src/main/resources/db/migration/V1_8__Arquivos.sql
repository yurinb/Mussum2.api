create table arquivo (
    id INTEGER AUTO_INCREMENT,
    dir VARCHAR(500) NOT NULL,
    nome VARCHAR(300) NOT NULL,
    data_criacao DATE,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';