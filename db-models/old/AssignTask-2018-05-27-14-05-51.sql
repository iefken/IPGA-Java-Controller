-- Adminer 4.6.2 MySQL dump


DROP TABLE IF EXISTS `AssignTask`;
CREATE TABLE `AssignTask` (
  `idAssignTask` int(10) NOT NULL,
  `assignTaskUUID` varchar(45) DEFAULT NULL,
  `userUUID` varchar(45) NOT NULL,
  `taskUUID` varchar(45) NOT NULL,
  `timestampLastUpdated` timestamp NULL DEFAULT NULL,
  `timestampCreated` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `active` int(10) NOT NULL DEFAULT '1',
  `entity_version` int(10) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 2018-05-27 12:05:51
