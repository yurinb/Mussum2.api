create table professor (
    id INTEGER AUTO_INCREMENT,
    username VARCHAR(30),
    password VARCHAR(30),
    nome VARCHAR(30),
    email VARCHAR(30),
    sobre VARCHAR(250),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO professor (username, password, nome, email) VALUES ("glad", "123","Gladimir Catarino", "gladimir@gmail.com");
INSERT INTO professor (username, password, nome, email) VALUES ("angelo", "123","Angelo Luz", "angelo@gmail.com");
INSERT INTO professor (username, password, nome, email) VALUES ("edecio", "123","Edecio Lepsen", "edecio@gmail.com");