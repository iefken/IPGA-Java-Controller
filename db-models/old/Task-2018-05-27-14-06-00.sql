-- Adminer 4.6.2 MySQL dump


DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
  `idTask` int(10) unsigned NOT NULL,
  `taskUUID` varchar(45) DEFAULT NULL,
  `eventUUID` varchar(45) NOT NULL,
  `description` varchar(250) NOT NULL,
  `dateTimeStart` varchar(45) NOT NULL,
  `dateTimeEnd` varchar(45) NOT NULL,
  `timestampLastUpdated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `active` int(10) NOT NULL DEFAULT '1',
  `entity_version` int(10) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 2018-05-27 12:06:00
