ALTER TABLE `socialnetwork`.`user`
DROP COLUMN `admin`;
ALTER TABLE `socialnetwork`.`user`
ADD COLUMN `secret_key` VARCHAR(10) NULL AFTER `donotdisturb`;
CREATE TABLE `socialnetwork`.`user_roles` (
  `user_id` BIGINT(64) NOT NULL,
  `roles_order` INT NOT NULL,
  `roles` VARCHAR(255) NULL,
  PRIMARY KEY (`user_id`, `roles_order`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k`
    FOREIGN KEY (`user_id`)
    REFERENCES `socialnetwork`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);