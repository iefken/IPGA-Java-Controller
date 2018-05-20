TRUNCATE `PlanningDb`.`Reservation_Session`;
TRUNCATE `PlanningDb`.`Reservation_Event`;
TRUNCATE `PlanningDb`.`Session`;
TRUNCATE `PlanningDb`.`Event`;
TRUNCATE `PlanningDb`.`User`;
TRUNCATE `PlanningDb`.`BaseEntity`;

-- -----------------------------------------------------
-- Data for table `PlanningDB`.`BaseEntity`
-- -----------------------------------------------------
START TRANSACTION;
USE `PlanningDB`;
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (1, 1, 1, DEFAULT, NULL, NULL);
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (2, 1, 1, DEFAULT, NULL, NULL);
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (3, 1, 1, DEFAULT, NULL, NULL);
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (4, 1, 1, DEFAULT, NULL, NULL);
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (5, 1, 1, DEFAULT, NULL, NULL);
INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (6, 1, 1, DEFAULT, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `PlanningDB`.`Event`
-- -----------------------------------------------------
START TRANSACTION;
USE `PlanningDB`;
INSERT INTO `PlanningDB`.`Event` (`idEvent`, `uuid`, `eventName`, `maxAttendees`, `description`, `summary`, `location`, `contactPerson`, `dateTimeStart`, `dateTimeEnd`, `type`, `price`, `GCAEventId`, `GCAEventLink`, `timestampLastUpdated`, `timestampCreated`) VALUES (3, 'e319f8aa-1910-442c-8b17-5e809d713ee4', 'EHackB1', 200, 'EHackB1 Description', 'EHackB1 Summary', 'Nijverheidskaai 170', 'superadmin', '2018-09-01 18:00:00', '2018-09-02 06:00:00', 'eventType', 5, NULL, NULL, NULL, NULL);
INSERT INTO `PlanningDB`.`Event` (`idEvent`, `uuid`, `eventName`, `maxAttendees`, `description`, `summary`, `location`, `contactPerson`, `dateTimeStart`, `dateTimeEnd`, `type`, `price`, `GCAEventId`, `GCAEventLink`, `timestampLastUpdated`, `timestampCreated`) VALUES (4, '11ff6e8f-aef4-4f2b-b6e6-f3dae7e116ce', 'EHackB2', 400, 'EHackB2 Description', 'EHackB2 Summary', 'Nijverheidskaai 170', 'superadmin', '2018-10-01 18:00:00', '2018-10-02 18:00:00', 'eventType', 4, NULL, NULL, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `PlanningDB`.`Session`
-- -----------------------------------------------------
START TRANSACTION;
USE `PlanningDB`;
INSERT INTO `PlanningDB`.`Session` (`idSession`, `uuid`, `eventUuid`, `sessionName`, `maxAttendees`, `description`, `summary`, `location`, `speaker`, `dateTimeStart`, `dateTimeEnd`, `type`, `price`, `GCAEventId`, `GCAEventLink`, `timestampLastUpdated`, `timestampCreated`) VALUES (5, '51129fa0-4a6b-44ec-aada-ff082f5db11b', 'e319f8aa-1910-442c-8b17-5e809d713ee4', 'EHackB1 Session1', 20, 'EHackB1 Session1 Description', 'EHackB1 Session1 Summary', 'EHackB1 Session1 Location', 'EHackB1 Session1 Speaker', '17:00', '18:00', 'SessionType', 0, NULL, NULL, NULL, NULL);
INSERT INTO `PlanningDB`.`Session` (`idSession`, `uuid`, `eventUuid`, `sessionName`, `maxAttendees`, `description`, `summary`, `location`, `speaker`, `dateTimeStart`, `dateTimeEnd`, `type`, `price`, `GCAEventId`, `GCAEventLink`, `timestampLastUpdated`, `timestampCreated`) VALUES (6, 'f87ea192-76e0-4c39-b157-0215f514f801', '11ff6e8f-aef4-4f2b-b6e6-f3dae7e116ce', 'EHackB2 Session1', 10, 'EHackB2 Session1 Description', 'EHackB2 Session1 Summary', 'EHackB2 Session1 Location', 'EHackB2 Session1 Speaker', '20:00', '22:00', 'SessionType', 0, NULL, NULL, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `PlanningDB`.`User`
-- -----------------------------------------------------
START TRANSACTION;
USE `PlanningDB`;
INSERT INTO `PlanningDB`.`User` (`idUser`, `uuid`, `lastName`, `firstName`, `phonenumber`, `email`, `street`, `houseNr`, `city`, `postalCode`, `country`, `company`, `type`, `timestampLastUpdated`, `timestampCreated`) VALUES (1, '83a02f40-ee76-4ba1-9bd7-80b5a163c61e', 'Falot', 'Ief', '0479797979', 'ief.falot@student.ehb.be', 'Nijverheidskaai', '170', 'Anderlecht', '1070', 'Belgium', 'EHB', 'ADMIN', NULL, NULL);
INSERT INTO `PlanningDB`.`User` (`idUser`, `uuid`, `lastName`, `firstName`, `phonenumber`, `email`, `street`, `houseNr`, `city`, `postalCode`, `country`, `company`, `type`, `timestampLastUpdated`, `timestampCreated`) VALUES (2, 'aecdfece-6606-48fc-b15e-5c64f5bb49dc', 'Admin', 'Super', '0444444444', 'superadmin@ehb.be', 'Nijverheidskaai', '170', 'Anderlecht', '1070', 'Belgium', 'EHB', 'ADMIN', NULL, NULL);

COMMIT;
