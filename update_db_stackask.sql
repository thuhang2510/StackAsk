use db_stackask;

CREATE TABLE IF NOT EXISTS `tbl_verification_token` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`token` VARCHAR(70),
    `user_id` BIGINT,
	`expiry_date` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;