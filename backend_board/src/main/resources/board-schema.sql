-- # DROP TABLE IF EXISTS `userInfoResponse`;
-- #
-- # CREATE TABLE `board` (
-- #   `id` long NOT NULL,
-- #   `channelId` varchar(128) NOT NULL,
-- #   `name` varchar(20) DEFAULT NULL,
-- #   `nickname` varchar(50) DEFAULT NULL,
-- #   `password` varchar(128) DEFAULT NULL,
-- #   `create_date` datetime DEFAULT current_timestamp(),
-- #   PRIMARY KEY (`uid`),
-- #   UNIQUE KEY `user_idx_unique_email` (`email`)
-- # ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into channel(channel_id, channel_name) values('earlyBird10TeamTestChannel1', 'Tutorial Channel s2');


