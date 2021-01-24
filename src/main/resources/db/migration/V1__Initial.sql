SELECT * FROM pg_extension;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS organizations;

CREATE TABLE users(
    user_id uuid DEFAULT uuid_generate_v4 (),
    first_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE organizations(
    org_id uuid DEFAULT uuid_generate_v4 (),
    name varchar(255) NOT NULL,
    address varchar(255),
    PRIMARY KEY (org_id)
);

INSERT INTO users(first_name, email) VALUES ('Cody', 'cody@aol.com');
INSERT INTO organizations(name, address) VALUES ('Chicago Artist Guide', null);
