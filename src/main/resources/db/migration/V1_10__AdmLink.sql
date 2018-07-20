create table adm_link (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    url TEXT NOT NULL,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO adm_link (titulo, url) VALUES ("Blackboard", "https://senac.blackboard.com/webapps/login/");
INSERT INTO adm_link (titulo, url) VALUES ("Portal do Aluno", "https://www.senacrs.com.br/meusenac_index.asp");
INSERT INTO adm_link (titulo, url) VALUES ("Site da Faculdade", "https://www.senacrs.com.br/index.asp");
INSERT INTO adm_link (titulo, url) VALUES ("Portal do Docente", "http://apsweb.senacrs.com.br/?irModulo=professor&session=0");
INSERT INTO adm_link (titulo, url) VALUES ("Revista Senac", "https://www.senacrs.com.br/servicos_revistasenac.asp");
INSERT INTO adm_link (titulo, url) VALUES ("Biblioteca", "https://www.senacrs.com.br/servicos_bibliotecas.asp");