CREATE TABLE `socialnetwork`.`user_friendrequest` (
  `user_id` BIGINT(64) NOT NULL,
  `friendrequest_id` BIGINT(64) NOT NULL,
  PRIMARY KEY (`user_id`, `friendrequest_id`),
  INDEX `FK8kxxo93lt6ki8eunmhbebuvio_idx` (`friendrequest_id` ASC) VISIBLE,
  CONSTRAINT `FK8kxxo93lt6ki8eunmhbebuvio`
    FOREIGN KEY (`friendrequest_id`)
    REFERENCES `socialnetwork`.`friendrequest` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FKnhe3m2nysyrfqm9rv20bjoifv`
    FOREIGN KEY (`user_id`)
    REFERENCES `socialnetwork`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);