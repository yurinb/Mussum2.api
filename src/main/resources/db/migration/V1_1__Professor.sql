create table professor (
    id INTEGER AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(100),
    nome VARCHAR(100),
    email VARCHAR(100),
    sobre VARCHAR(250),
    descricao VARCHAR(250),
    fotolink VARCHAR(500),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

INSERT INTO professor (username, password, role, nome, email, sobre, descricao, fotolink) VALUES ("admin", "admin", "user, admin", "admin", "", "", "", "");
INSERT INTO professor (username, password, role, nome, email, sobre, descricao, fotolink) VALUES ("glad", "123", "user", "Gladimir Catarino", "gladimir@gmail.com", "Formado em Artes JEDI de banco de dados", "Professor na Faculdade FATEC/SENAC-RS", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");
INSERT INTO professor (username, password, role, nome, email, sobre, descricao, fotolink) VALUES ("angelo", "123", "user","Angelo Luz", "angelo@gmail.com", "Formado em Cienc das Boas Praticas", "Professor na Faculdade FATEC/SENAC-RS", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");
INSERT INTO professor (username, password, role, nome, email, sobre, descricao, fotolink) VALUES ("edecio", "123", "user","Edecio Lepsen", "edecio@gmail.com", "Dotourado, PHD, Mestre, Senior em PHP e Javascript", "Professor na Faculdade FATEC/SENAC-RS", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");