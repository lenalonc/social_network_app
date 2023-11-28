ALTER TABLE `socialnetwork`.`friendrequest`
ADD COLUMN `date` DATETIME NOT NULL AFTER `id_user2`;
ALTER TABLE `socialnetwork`.`user`
CHANGE COLUMN `password` `password` VARCHAR(100) NULL ;