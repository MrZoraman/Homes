CREATE PROCEDURE `multihomes_add_world_proc` (IN world_in VARCHAR(255))
BEGIN
    DECLARE world_exists INT;

    SELECT EXISTS(SELECT 1 FROM `multihomes_worlds` WHERE `world_name`=world_in LIMIT 1) INTO world_exists;

    IF world_exists = 0 THEN
        INSERT INTO `multihomes_worlds` (`world_name`) VALUES (world_in);
    END IF;
END
