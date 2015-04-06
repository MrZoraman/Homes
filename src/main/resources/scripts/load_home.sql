SELECT `home_name`,`x`,`y`,`z`,`yaw`,`pitch`,`homes_uuids`.`uuid`,`homes_worlds`.`world_name` FROM `homes`
    JOIN `homes_uuids` ON `homes_uuids`.`id`=`homes`.`uuid_id`
    JOIN `homes_worlds` ON `homes_worlds`.`id`=`homes`.`world_id`
    WHERE `homes_uuids`.`uuid`=? AND `homes`.`home_name`=?;