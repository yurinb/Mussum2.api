create table aviso (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    data VARCHAR(50),
    url TEXT,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO aviso (titulo, descricao, data, url) VALUES ("CURSO INGLES 50% DESCONTO", "somente para 85 anos +","02/05/2018 04:20", "google.com");
INSERT INTO aviso (titulo, descricao, data, url) VALUES ("CURSO FOTOGRAFIA 50% DESCONTO", "somente para acima de 60 anos acompanhado pelos pais","04/05/2018 04:20", "google.com");
INSERT INTO aviso (titulo, descricao, data, url) VALUES ("LANÇAMENTO LIVRO JAVASCRIPT MASTER Dr. Edecio Lepsen", "Quase de graça apenas 50 reau","07/05/2018 04:20", "google.com");