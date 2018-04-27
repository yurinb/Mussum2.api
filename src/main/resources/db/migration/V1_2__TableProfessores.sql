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


INSERT INTO professor (username, password, nome, email) VALUES ("admin", "admin","ADMIN", "admin@gmail.com");
INSERT INTO professor (username, password, nome, email) VALUES ("glad", "123","Gladimir Catarino", "gladimir@gmail.com");
INSERT INTO professor (username, password, nome, email) VALUES ("angelo", "123","Angelo Luz", "angelo@gmail.com");
INSERT INTO professor (username, password, nome, email) VALUES ("edecio", "123","Edecio Lepsen", "edecio@gmail.com");