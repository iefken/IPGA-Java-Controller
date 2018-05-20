-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Gegenereerd op: 15 mei 2018 om 23:37
-- Serverversie: 5.7.19
-- PHP-versie: 7.0.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `planningdb`
--

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int(10) UNSIGNED NOT NULL,
  `userUUID` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `phonenumber` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `street` varchar(45) NOT NULL,
  `houseNr` varchar(15) NOT NULL,
  `city` varchar(45) NOT NULL,
  `postalCode` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `company` varchar(45) DEFAULT NULL,
  `type` enum('VISITOR','EMPLOYEE','ADMIN','SPONSOR','SPEAKER','CONSULTANT','DOCENT') NOT NULL,
  `timestampLastUpdated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `timestampCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  UNIQUE KEY `UUID_UNIQUE` (`userUUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `user`
--

INSERT INTO `user` (`idUser`, `userUUID`, `lastName`, `firstName`, `phonenumber`, `email`, `street`, `houseNr`, `city`, `postalCode`, `country`, `company`, `type`, `timestampLastUpdated`, `timestampCreated`) VALUES
(1, '83a02f40-ee76-4ba1-9bd7-80b5a163c61e', 'Falot', 'Ief', '0479797979', 'ief.falot@student.ehb.be', 'Nijverheidskaai', '170', 'Anderlecht', '1070', 'Belgium', 'EHB', 'ADMIN', NULL, NULL),
(2, 'aecdfece-6606-48fc-b15e-5c64f5bb49dc', 'Admin', 'Super', '0444444444', 'superadmin@ehb.be', 'Nijverheidskaai', '170', 'Anderlecht', '1070', 'Belgium', 'EHB', 'ADMIN', NULL, NULL),
(224, 'fbea0671-1324-4f92-a0b4-cc6e56c537d7', 'Parker', 'John', '+(32) 499 88 77 33', 'mockedUser@mocker.com', 'MockedNamelaan', '420 Mock', 'Mockels', '4501 Mock', 'Mockelgium', 'JP Mocked', 'VISITOR', '2018-05-15 23:17:38', '2018-05-15 23:17:38');

--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_User_BaseEntity1` FOREIGN KEY (`idUser`) REFERENCES `baseentity` (`entityId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
