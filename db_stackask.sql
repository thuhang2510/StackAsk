use db_stackask;

DROP TABLE `tbl_user`;

CREATE TABLE IF NOT EXISTS `tbl_user` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(45),
    `fullname` NVARCHAR(45),
    `email` VARCHAR(45),
    `phonenumber` VARCHAR(15),
    `password` VARCHAR(70),
    `enabled` BOOLEAN DEFAULT 1,
    `reset_password_token` VARCHAR(45) DEFAULT NULL,
    `avatar` BLOB DEFAULT NULL,
	`created_time` datetime DEFAULT NULL,
    `updated_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT UC_email_enabled UNIQUE (`email`, `enabled`),
    CONSTRAINT UC_phone_enabled UNIQUE (`phonenumber`, `enabled`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_question` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` NVARCHAR(255),
    `category` NVARCHAR(255),
    `content` NVARCHAR(255) NULL,
    `image_name` VARCHAR(45) NULL,
    `image_url` VARCHAR(45) NULL,
    `vote` BIGINT DEFAULT 0,
    `view` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `createdTime` datetime DEFAULT NULL,
    `updatedTime` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_answer` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` NVARCHAR(255) NULL,
    `image_name` VARCHAR(45) NULL,
    `image_url` VARCHAR(45) NULL,
    `vote` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `question_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `createdTime` datetime DEFAULT NULL,
    `updatedTime` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_replyanswer` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` NVARCHAR(255) NULL,
    `vote` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `ansewer_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `createdTime` datetime DEFAULT NULL,
    `updatedTime` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;