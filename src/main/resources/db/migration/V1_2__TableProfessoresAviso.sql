create table professor_aviso (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(50),
    detalhado VARCHAR(300),
    professor_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY (professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

INSERT INTO professor_aviso (titulo, detalhado, professor_id) VALUES ("Trabalho 12/05 MYSQL", "Triggers, functions, drop sem where...", 1);
INSERT INTO professor_aviso (titulo, detalhado, professor_id) VALUES ("Prova 24/05 GIT", "Teoria e pratica", 2);
INSERT INTO professor_aviso (titulo, detalhado, professor_id) VALUES ("Trabalho 02/05 LARAVEL", "Crud completo com pagina administrador", 3);

