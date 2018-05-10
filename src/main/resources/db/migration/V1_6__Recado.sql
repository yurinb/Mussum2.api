create table recado (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(300),
    data VARCHAR(100),
    professor_id INTEGER,
    primary key(id),
    foreign key(professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("PROVA QUARTA", "Vai cair tudo e mais um pouco"," 20/04/2018 - 04:20", 1);
INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("TRABALHO PROXIMA SEMANA", "Vai cair tudo e mais um pouco", " 20/04/2018 - 04:20", 1);

INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("PROVA PROXIMA QUARTA", "Vai cair tudo e mais um pouco", " 20/04/2018 - 04:20", 2);
INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("TRABALHO PROXIMA SEMANA", "Vai cair tudo e mais um pouco", " 20/04/2018 - 04:20", 2);

INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("PROVA QUARTA", "Vai cair tudo e mais um pouco", " 20/04/2018 - 04:20", 3);
INSERT INTO recado (titulo, descricao, data, professor_id) VALUES ("TRABALHO PROXIMA SEMANA", "Vai cair tudo e mais um pouco", " 20/04/2018 - 04:20", 3);