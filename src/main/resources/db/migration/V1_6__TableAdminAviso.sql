create table admin_aviso (
    id INTEGER AUTO_INCREMENT,
    titulo VARCHAR(100),
    detalhado VARCHAR(300),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

INSERT INTO admin_aviso (titulo, detalhado) VALUES ("PROMOCAO CURSO DE INGLES", "Vagas abertas com até 50% de desconto!");
INSERT INTO admin_aviso (titulo, detalhado) VALUES ("PROMOCAO CURSO DE POS GRADUACAO ANDROID", "Vagas abertas com até 50% de desconto!");

