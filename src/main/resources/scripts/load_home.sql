SELECT `home_name`,`x`,`y`,`z`,`yaw`,`pitch`,`multihomes_uuids`.`uuid`,`multihomes_worlds`.`world_name` FROM `multihomes`
    JOIN `multihomes_uuids` ON `multihomes_uuids`.`id`=`multihomes`.`uuid_id`
    JOIN `multihomes_worlds` ON `multihomes_worlds`.`id`=`multihomes`.`world_id`
    WHERE `multihomes_uuids`.`uuid`=? AND `multihomes`.`home_name`=?;