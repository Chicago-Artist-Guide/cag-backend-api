CREATE TABLE IF NOT EXISTS union_status(
    union_status_id UUID DEFAULT uuid_generate_v4 (),
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (union_status_id)
);

CREATE TABLE IF NOT EXISTS ethnicity(
    ethnicity_id UUID DEFAULT uuid_generate_v4 (),
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (ethnicity_id)
);

CREATE TABLE IF NOT EXISTS age_increment(
    age_increment_id UUID DEFAULT uuid_generate_v4 (),
    age_increment VARCHAR(10) NOT NULL,
    PRIMARY KEY (age_increment_id)
);

CREATE TABLE IF NOT EXISTS skill(
    skill_id UUID DEFAULT uuid_generate_v4 (),
    name VARCHAR(255) NOT NULL,
    UNIQUE (name),
    PRIMARY KEY (skill_id)
);