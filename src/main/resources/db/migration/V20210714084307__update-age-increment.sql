ALTER TABLE age_increment
DROP COLUMN youngest_age;

ALTER TABLE age_increment
DROP COLUMN oldest_age;

ALTER TABLE age_increment
ADD COLUMN ages VARCHAR(255) NOT NULL;