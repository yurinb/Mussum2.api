create table admin (
    id INTEGER AUTO_INCREMENT,
    username VARCHAR(30),
    password VARCHAR(30),
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


INSERT INTO admin (username, password) VALUES ("admin", "admin");