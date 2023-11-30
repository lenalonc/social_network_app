CREATE TABLE `socialnetwork`.`attending` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `id_user` BIGINT(64) NOT NULL,
  `id_event` BIGINT(64) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_at_user_idx` (`id_user` ASC) VISIBLE,
  INDEX `fk_at_event_idx` (`id_event` ASC) VISIBLE,
  CONSTRAINT `fk_at_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `socialnetwork`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_at_event`
    FOREIGN KEY (`id_event`)
    REFERENCES `socialnetwork`.`event` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);