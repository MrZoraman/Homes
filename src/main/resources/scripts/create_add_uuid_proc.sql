CREATE PROCEDURE `homes_add_uuid_proc` (IN uuid_in VARCHAR(36))
BEGIN
    DECLARE uuid_exists INT;

    SELECT EXISTS(SELECT 1 FROM `homes_uuids` WHERE `uuid`=uuid_in LIMIT 1) INTO uuid_exists;

    IF uuid_exists = 0 THEN
        INSERT INTO `homes_uuids` (`uuid`) VALUES (uuid_in);
    END IF;
END;