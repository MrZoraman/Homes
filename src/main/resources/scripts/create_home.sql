INSERT INTO `homes` (`uuid_id`, `home_name`, `x`, `y`, `z`, `yaw`, `pitch`, `world_id`)
    VALUES ((SELECT `id` FROM `homes_uuids` WHERE `uuid`=?), ?, ?, ?, ?, ?, ?, (SELECT `id` FROM `homes_worlds` WHERE `world_name`=?));
