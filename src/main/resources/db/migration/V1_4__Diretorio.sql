create table diretorio (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO diretorio (titulo, url) VALUES ("Anais - Projeto Integrador e TCC", "http://187.7.106.14/anais/");
INSERT INTO diretorio (titulo, url) VALUES ("Imagens em formato ISO", "http://187.7.106.14/isos");
INSERT INTO diretorio (titulo, url) VALUES ("FTP - Repositoris", "ftp://192.168.200.6/");
INSERT INTO diretorio (titulo, url) VALUES ("Material da Cisco", "http://187.7.106.14/cisco");
INSERT INTO diretorio (titulo, url) VALUES ("Filmes", "http://187.7.106.14/filmes");
INSERT INTO diretorio (titulo, url) VALUES ("Sistemas Operacionais", "http://187.7.106.14/midias");
INSERT INTO diretorio (titulo, url) VALUES ("Utilitários", "http://187.7.106.14/software");
INSERT INTO diretorio (titulo, url) VALUES ("Livros", "http://187.7.106.14/livros");
INSERT INTO diretorio (titulo, url) VALUES ("Fotos", "http://187.7.106.14/fotos");
INSERT INTO diretorio (titulo, url) VALUES ("Documentos", "http://187.7.106.14/documentos");