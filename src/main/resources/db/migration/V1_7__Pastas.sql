create table pasta (
    id INTEGER AUTO_INCREMENT,
    dir TEXT NOT NULL,
    nome VARCHAR(300) NOT NULL,
    visivel BOOLEAN DEFAULT 0,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';


create table pasta_files (
    id INTEGER AUTO_INCREMENT,
    pasta_id INTEGER,
    files_id INTEGER,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

create table pasta_folders (
    id INTEGER AUTO_INCREMENT,
    pasta_id INTEGER,
    folders_id INTEGER,
    primary key(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

