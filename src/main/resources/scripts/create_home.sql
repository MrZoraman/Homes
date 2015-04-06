INSERT INTO `multihomes` (`uuid_id`, `home_name`, `x`, `y`, `z`, `yaw`, `pitch`, `world_id`)
    VALUES ((SELECT `id` FROM `multihomes_uuids` WHERE `uuid`=?), ?, ?, ?, ?, ?, ?, (SELECT `id` FROM `multihomes_worlds` WHERE `world_name`=?));
