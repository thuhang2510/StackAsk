use db_stackask;

CREATE TABLE IF NOT EXISTS `tbl_verification_token` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`token` VARCHAR(70),
    `user_id` BIGINT,
	`expiry_date` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE `tbl_media`;

CREATE TABLE IF NOT EXISTS `tbl_question` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(70) UNIQUE,
    `title` NVARCHAR(255),
    `category` NVARCHAR(255),
    `content` NVARCHAR(255) NULL,
    `vote` BIGINT DEFAULT 0,
    `view` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `created_time` datetime DEFAULT NULL,
    `updated_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_answer` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` NVARCHAR(255) NULL,
    `vote` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `question_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `created_time` datetime DEFAULT NULL,
    `updated_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_replyanswer` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` NVARCHAR(255) NULL,
    `vote` BIGINT DEFAULT 0,
    `user_id` BIGINT,
    `ansewer_id` BIGINT,
    `enabled` BIT DEFAULT 1,
    `created_time` datetime DEFAULT NULL,
    `updated_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_media` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(70) UNIQUE,
    `file_name` VARCHAR(70),
    `file_path` VARCHAR(70),
    `file_size` BIGINT,
    `file_type` VARCHAR(30),
    `uploaded_by`BIGINT,
    `created_time` DATETIME DEFAULT NULL,
    `updated_time` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `tbl_media`
ADD COLUMN `parent_uuid` VARCHAR(70) NOT NULL;

ALTER TABLE `tbl_media`
ADD COLUMN `enabled` BOOLEAN DEFAULT TRUE;