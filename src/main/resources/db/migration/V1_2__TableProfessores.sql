/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  yurin
 * Created: 13/04/2018
 */


create table professor (
    id BIGINT AUTO_INCREMENT,
    username VARCHAR(30),
    password VARCHAR(30),
    nome VARCHAR(30),
    email VARCHAR(30),
    sobre VARCHAR(250),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO professor (nome, email) VALUES ("angelo", "angelo@gmail.com");
INSERT INTO professor (nome, email) VALUES ("gladimir", "glad@gmail.com");
INSERT INTO professor (nome, email) VALUES ("edecio", "edeciu@gmail.com");
INSERT INTO professor (nome, email) VALUES ("cv", "cvalve@gmail.com");
INSERT INTO professor (nome, email) VALUES ("luzzard", "luzz@gmail.com");
INSERT INTO professor (nome, email) VALUES ("dartschannegger", "dart@gmail.com");