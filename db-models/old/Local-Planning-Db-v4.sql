
-- -----------------------------------------------------
-- Schema PlanningDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema PlanningDB
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `PlanningDB` DEFAULT CHARACTER SET utf8 ;
USE `PlanningDB` ;

-- -----------------------------------------------------
-- Table `PlanningDB`.`BaseEntity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`BaseEntity` (
  `idBaseEntity` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `entity_version` INT NOT NULL DEFAULT 1,
  `active` TINYINT NOT NULL DEFAULT 1,
  `timestamp` VARCHAR(25) NOT NULL DEFAULT '01/05/2018 00:00:00',
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idBaseEntity`),
  UNIQUE INDEX `entityId_UNIQUE` (`idBaseEntity` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`Event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`Event` (
  `idEvent` INT UNSIGNED NOT NULL,
  `uuid` VARCHAR(45) NULL,
  `eventName` VARCHAR(45) NOT NULL,
  `maxAttendees` INT UNSIGNED NOT NULL,
  `description` VARCHAR(250) NOT NULL,
  `summary` VARCHAR(150) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `contactPerson` VARCHAR(45) NOT NULL,
  `dateTimeStart` VARCHAR(45) NOT NULL DEFAULT '2018-01-01T09:00:00+02:00',
  `dateTimeEnd` VARCHAR(45) NOT NULL DEFAULT '2018-01-02T21:00:00+02:00',
  `type` VARCHAR(45) NULL,
  `price` FLOAT NOT NULL DEFAULT 0,
  `GCAEventId` VARCHAR(45) NULL,
  `GCAEventLink` VARCHAR(150) NULL,
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idEvent`),
  INDEX `fk_entityId_event` (`idEvent` ASC),
  INDEX `fk_uuid_event` (`uuid` ASC),
  CONSTRAINT `fk_entityId_event`
    FOREIGN KEY (`idEvent`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`Session`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`Session` (
  `idSession` INT UNSIGNED NOT NULL,
  `uuid` VARCHAR(45) NULL,
  `eventUuid` VARCHAR(45) NULL,
  `sessionName` VARCHAR(45) NOT NULL,
  `maxAttendees` INT UNSIGNED NOT NULL,
  `description` VARCHAR(250) NOT NULL,
  `summary` VARCHAR(150) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `speaker` VARCHAR(45) NOT NULL,
  `dateTimeStart` VARCHAR(45) NOT NULL,
  `dateTimeEnd` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `price` FLOAT NOT NULL DEFAULT 0,
  `GCAEventId` VARCHAR(45) NULL,
  `GCAEventLink` VARCHAR(150) NULL,
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idSession`),
  INDEX `fk_Session_Event1_idx` (`eventUuid` ASC),
  INDEX `fk_uuid_session` (`uuid` ASC),
  CONSTRAINT `fk_Session_BaseEntity1`
    FOREIGN KEY (`idSession`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Session_Event1`
    FOREIGN KEY (`eventUuid`)
    REFERENCES `PlanningDB`.`Event` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`User` (
  `idUser` INT UNSIGNED NOT NULL,
  `uuid` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `phonenumber` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  `houseNr` VARCHAR(15) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `postalCode` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `company` VARCHAR(45) NULL,
  `type` ENUM('VISITOR', 'EMPLOYEE', 'ADMIN', 'SPONSOR', 'SPEAKER', 'CONSULTANT', 'DOCENT') NOT NULL DEFAULT 'VISITOR',
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idUser`),
  INDEX `fk_uuid_user` (`uuid` ASC),
  CONSTRAINT `fk_User_BaseEntity1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`Reservation_Event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`Reservation_Event` (
  `idReservationEvent` INT UNSIGNED NOT NULL,
  `uuid` VARCHAR(45) NULL,
  `eventUuid` VARCHAR(45) NULL,
  `userUuid` VARCHAR(45) NULL,
  `paid` FLOAT NULL,
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idReservationEvent`),
  INDEX `fk_Event_has_User_Event_idx` (`eventUuid` ASC),
  INDEX `fk_Event_has_User_User1_idx` (`userUuid` ASC),
  INDEX `fk_Reservation_Event_BaseEntity1_idx` (`idReservationEvent` ASC),
  INDEX `fk_uuid_reservationEvent` (`uuid` ASC),
  CONSTRAINT `fk_Event_has_User_Event`
    FOREIGN KEY (`eventUuid`)
    REFERENCES `PlanningDB`.`Event` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Event_has_User_User1`
    FOREIGN KEY (`userUuid`)
    REFERENCES `PlanningDB`.`User` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservation_Event_BaseEntity1`
    FOREIGN KEY (`idReservationEvent`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`Reservation_Session`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`Reservation_Session` (
  `idReservationSession` INT UNSIGNED NOT NULL,
  `uuid` VARCHAR(45) NULL,
  `sessionUuid` VARCHAR(45) NULL,
  `userUuid` VARCHAR(45) NULL,
  `paid` FLOAT NULL,
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idReservationSession`),
  INDEX `fk_Session_has_User_Session1_idx` (`sessionUuid` ASC),
  INDEX `fk_Session_has_User_User1_idx` (`userUuid` ASC),
  INDEX `fk_Reservation_Session_BaseEntity1_idx` (`idReservationSession` ASC),
  INDEX `fk_uuid_reservationSession` (`uuid` ASC),
  CONSTRAINT `fk_Session_has_User_Session1`
    FOREIGN KEY (`sessionUuid`)
    REFERENCES `PlanningDB`.`Session` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Session_has_User_User1`
    FOREIGN KEY (`userUuid`)
    REFERENCES `PlanningDB`.`User` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservation_Session_BaseEntity1`
    FOREIGN KEY (`idReservationSession`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanningDB`.`EntitiesToAdd`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanningDB`.`EntitiesToAdd` (
  `idEntitiesToAdd` INT UNSIGNED NOT NULL,
  `table` VARCHAR(45) NOT NULL DEFAULT 'BaseEntity',
  `status` ENUM('TOADD', 'ADDING', 'ADDED') NOT NULL DEFAULT 'TOADD',
  `timestampLastUpdated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idEntitiesToAdd`),
  INDEX `fk_EntitiesToAdd_BaseEntity1_idx` (`idEntitiesToAdd` ASC),
  CONSTRAINT `fk_EntitiesToAdd_BaseEntity1`
    FOREIGN KEY (`idEntitiesToAdd`)
    REFERENCES `PlanningDB`.`BaseEntity` (`idBaseEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

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
