ALTER TABLE age_increment
    DROP COLUMN age_increment;
ALTER TABLE age_increment
    ADD COLUMN youngest_age SMALLINT NOT NULL DEFAULT 20;
ALTER TABLE age_increment
    ADD COLUMN oldest_age SMALLINT NOT NULL DEFAULT 30;
