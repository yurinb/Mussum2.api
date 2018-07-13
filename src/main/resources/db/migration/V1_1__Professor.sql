create table professor (
    id INTEGER AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(100),
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    sobre VARCHAR(250),
    descricao VARCHAR(250),
    resumo VARCHAR(250),
    formacao VARCHAR(250),
    fotolink VARCHAR(500),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

INSERT INTO professor (username, password, role, nome, sobrenome, email, sobre, descricao, resumo, formacao, fotolink) VALUES ("angelo", "123", "admin","Angelo", "Luz", "angelo@gmail.com", "Formado em Cienc das Boas Praticas", "Professor na Faculdade FATEC/SENAC-RS", "resumo bla bla bla n sei pra q isso","formacao affs eu n vo preencher isso", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");
INSERT INTO professor (username, password, role, nome, sobrenome, email, sobre, descricao, resumo, formacao, fotolink) VALUES ("glad", "123", "professor", "Gladimir", "Catarino", "gladimir@gmail.com", "Formado em Artes JEDI de banco de dados", "Professor na Faculdade FATEC/SENAC-RS",  "resumo bla bla bla n sei pra q isso","formacao affs eu n vo preencher isso", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");
INSERT INTO professor (username, password, role, nome, sobrenome, email, sobre, descricao, resumo, formacao, fotolink) VALUES ("edecio", "123", "professor","Edecio", "Lepsen", "edecio@gmail.com", "Dotourado, PHD, Mestre, Senior em PHP e Javascript", "Professor na Faculdade FATEC/SENAC-RS",  "resumo bla bla bla n sei pra q isso","formacao affs eu n vo preencher isso", "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png");