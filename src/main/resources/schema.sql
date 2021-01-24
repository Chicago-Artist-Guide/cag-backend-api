CREATE TABLE users (id uuid PRIMARY KEY, name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL);
CREATE TABLE organizations (id uuid PRIMARY KEY, name varchar(255) NOT NULL, address varchar(255));
DROP TABLE users;
DROP TABLE organizations;
SELECT * FROM organizations;
SELECT * FROM users;