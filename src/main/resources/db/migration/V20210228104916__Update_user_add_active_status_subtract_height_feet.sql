ALTER TABLE users
ADD COLUMN active_status bool DEFAULT true;

ALTER TABLE profile
DROP COLUMN height_feet;
