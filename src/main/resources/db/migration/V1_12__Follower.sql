create table follower (
    id INTEGER AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    pasta_dir VARCHAR(500) NOT NULL,
    professor_id   INTEGER,
    primary key(id),
    foreign key(professor_id) REFERENCES professor(id)
) CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';