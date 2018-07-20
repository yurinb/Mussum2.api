create table arquivo (
    id INTEGER AUTO_INCREMENT,
    dir TEXT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    visivel BOOLEAN DEFAULT 0,
    comentario TEXT,
    link TEXT,
    data_criacao DATE,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';