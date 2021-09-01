CREATE TABLE IF NOT EXISTS profile(
    profile_id UUID DEFAULT uuid_generate_v4 (),
    user_id UUID NOT NULL,
    pronouns VARCHAR(255) NOT NULL,
    lgbtqplus_member BOOLEAN,
    gender_identity VARCHAR(50) NOT NULL,
    comfortable_playing_man BOOLEAN,
    comfortable_playing_women BOOLEAN,
    comfortable_playing_neither BOOLEAN,
    comfortable_playing_transition BOOLEAN,
    height_feet SMALLINT,
    height_inches SMALLINT,
    agency VARCHAR(255),
    website_type_one VARCHAR(50),
    website_link_one VARCHAR(255),
    website_type_two VARCHAR(50),
    website_link_two VARCHAR(255),
    bio TEXT,
    UNIQUE (user_id),
    PRIMARY KEY (profile_id),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users(user_id)
            ON DELETE CASCADE
);

