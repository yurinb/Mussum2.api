create table feed (
    id INTEGER AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL,
    professor VARCHAR(300) NOT NULL,
    data_criacao VARCHAR(50),
    titulo VARCHAR(100) NOT NULL,
    comentario TEXT NOT NULL,
    arquivo VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    link TEXT NOT NULL,
    dir TEXT NOT NULL,
    priority INTEGER,
    
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';