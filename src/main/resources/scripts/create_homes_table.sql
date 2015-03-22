CREATE TABLE IF NOT EXISTS `multihomes` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `uuid` VARCHAR(36),
  `home_name` VARCHAR(255),
  `x` DOUBLE,
  `y` DOUBLE,
  `z` DOUBLE,
  `yaw` FLOAT,
  `pitch` FLOAT,
  `world_name` VARCHAR(255)
);