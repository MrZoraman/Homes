SELECT `home_name` FROM `homes` WHERE `uuid_id`=(SELECT `id` FROM `homes_uuids` WHERE `uuid`=? LIMIT 1);