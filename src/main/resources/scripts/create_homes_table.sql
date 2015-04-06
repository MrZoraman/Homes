CREATE TABLE IF NOT EXISTS `homes` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `uuid_id` INT UNSIGNED NOT NULL,
    `home_name` VARCHAR(255),
    `x` DOUBLE,
    `y` DOUBLE,
    `z` DOUBLE,
    `yaw` FLOAT,
    `pitch` FLOAT,
    `world_id` INT UNSIGNED NOT NULL,
    INDEX `home_name_index` (`home_name` ASC),
    CONSTRAINT `FK_uuid` FOREIGN KEY (`uuid_id`)
        REFERENCES `homes_uuids` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_world` FOREIGN KEY (`world_id`)
        REFERENCES `homes_worlds` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
)  ENGINE=InnoDB;