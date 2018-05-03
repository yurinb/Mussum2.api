create table feed (
    id INTEGER AUTO_INCREMENT,
    acao VARCHAR(50),
    obj_link_name VARCHAR(50),
    redirect_url VARCHAR(300),
    data_criacao VARCHAR(50),
    professor_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY (professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 1);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 1);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 1);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 2);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 2);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 2);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 3);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 3);
INSERT INTO feed (acao, obj_link_name, redirect_url, data_criacao, professor_id) VALUES ("adicionou um novo link", "ExemploNomeLink", "google.com", "02/04/2018 - 04:20", 3);