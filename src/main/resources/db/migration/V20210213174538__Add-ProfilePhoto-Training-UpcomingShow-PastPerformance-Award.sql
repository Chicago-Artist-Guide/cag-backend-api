CREATE TABLE IF NOT EXISTS profile_photo(
    profile_photo_id UUID DEFAULT uuid_generate_v4(),
    profile_id UUID NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (profile_photo_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id)
);

CREATE TABLE IF NOT EXISTS training(
    training_id UUID DEFAULT uuid_generate_v4(),
    profile_id UUID NOT NULL,
    institution VARCHAR(255) NOT NULL,
    degree VARCHAR(255) NOT NULL,
    start_year SMALLINT NOT NULL,
    end_year SMALLINT,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(50),
    notes TEXT,
    PRIMARY KEY (training_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id)
);

CREATE TABLE IF NOT EXISTS upcoming_show(
    upcoming_show_id UUID DEFAULT uuid_generate_v4(),
    profile_id UUID NOT NULL,
    photo_url VARCHAR(100),
    show_title VARCHAR(50) NOT NULL,
    show_synopsis TEXT,
    industry_code VARCHAR(50),
    show_url VARCHAR(50),
    PRIMARY KEY (upcoming_show_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id)
);

CREATE TABLE IF NOT EXISTS past_performance(
    past_performance_id UUID DEFAULT uuid_generate_v4(),
    profile_id UUID NOT NULL,
    show_title VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    theater_or_location VARCHAR(255) NOT NULL,
    show_url VARCHAR(50),
    director VARCHAR(50),
    musical_director VARCHAR(50),
    theater_group VARCHAR(50),
    PRIMARY KEY (past_performance_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id)
);

CREATE TABLE IF NOT EXISTS award(
    award_id UUID DEFAULT uuid_generate_v4(),
    profile_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    year_received SMALLINT,
    award_url VARCHAR(100),
    description TEXT,
    PRIMARY KEY(award_id),
    CONSTRAINT fk_profile
        FOREIGN KEY(profile_id)
            REFERENCES profile(profile_id)
);
