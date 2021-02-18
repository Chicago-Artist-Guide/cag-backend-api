CREATE TABLE IF NOT EXISTS union_status_member(
    union_status_member_id UUID DEFAULT uuid_generate_v4 (),
    profile_id UUID NOT NULL,
    union_status_id UUID NOT NULL,
    UNIQUE (profile_id, union_status_id),
    PRIMARY KEY (union_status_member_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id),
    CONSTRAINT fk_union_status
        FOREIGN KEY(union_status_id)
            REFERENCES union_status(union_status_id)
);

CREATE TABLE IF NOT EXISTS ethnicity_member(
    ethnicity_member_id UUID DEFAULT uuid_generate_v4 (),
    profile_id UUID NOT NULL,
    ethnicity_id UUID NOT NULL,
    UNIQUE (profile_id, ethnicity_id),
    PRIMARY KEY (ethnicity_member_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id),
    CONSTRAINT fk_ethnicity
        FOREIGN KEY(ethnicity_id)
            REFERENCES ethnicity(ethnicity_id)
);

CREATE TABLE IF NOT EXISTS age_increment_member(
    age_increment_member_id UUID DEFAULT uuid_generate_v4 (),
    profile_id UUID NOT NULL,
    age_increment_id UUID NOT NULL,
    UNIQUE (profile_id, age_increment_id),
    PRIMARY KEY (age_increment_member_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id),
    CONSTRAINT fk_age_increment
        FOREIGN KEY(age_increment_id)
            REFERENCES age_increment(age_increment_id)
);

CREATE TABLE IF NOT EXISTS skill_member(
    skill_member_id UUID DEFAULT uuid_generate_v4 (),
    profile_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    UNIQUE (profile_id, skill_id),
    PRIMARY KEY (skill_member_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id),
    CONSTRAINT fk_skill
        FOREIGN KEY(skill_id)
            REFERENCES skill(skill_id)
);
