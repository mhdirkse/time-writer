USE users;
CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id),
  username VARCHAR(255),
  password VARCHAR(255));

INSERT INTO users (username, password) VALUES ("mhdirkse", "password");

