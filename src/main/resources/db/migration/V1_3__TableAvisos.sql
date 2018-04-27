/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  yurin
 * Created: 13/04/2018
 */


create table aviso (
    id BIGINT AUTO_INCREMENT,
    titulo VARCHAR(30),
    detalhado VARCHAR(300),
    idProfessor BIGINT,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO aviso (titulo, detalhado, idProfessor) VALUES ("Prova 02/05 GIT", "chorem", 1);
INSERT INTO aviso (titulo, detalhado, idProfessor) VALUES ("Prova 02/05 GIT", "chorem", 1);
INSERT INTO aviso (titulo, detalhado, idProfessor) VALUES ("Prova 02/05 GIT", "chorem", 1);
INSERT INTO aviso (titulo, detalhado, idProfessor) VALUES ("Prova 02/05 GIT", "chorem", 1);
INSERT INTO aviso (titulo, detalhado, idProfessor) VALUES ("Prova 02/05 GIT", "chorem", 1);

