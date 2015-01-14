-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 14 Janvier 2015 à 16:49
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `partyutt`
--

-- --------------------------------------------------------

--
-- Structure de la table `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `eventID` int(11) NOT NULL AUTO_INCREMENT,
  `eventOwner_FK` int(11) NOT NULL,
  `eventName` varchar(50) NOT NULL,
  `eventDate` datetime NOT NULL,
  `eventAddress` varchar(50) NOT NULL,
  PRIMARY KEY (`eventID`),
  KEY `eventOwner_FK` (`eventOwner_FK`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Structure de la table `invite`
--

CREATE TABLE IF NOT EXISTS `invite` (
  `inviteID` int(11) NOT NULL AUTO_INCREMENT,
  `inviteUser_FK` int(11) NOT NULL,
  `inviteEvent_FK` int(11) NOT NULL,
  `isOrga` tinyint(1) NOT NULL DEFAULT '0',
  `isComing` int(11) NOT NULL DEFAULT '0',
  `toBring` varchar(50) NOT NULL DEFAULT '',
  `quantity` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`inviteID`),
  KEY `inviteUser_FK` (`inviteUser_FK`),
  KEY `inviteEvent_FK` (`inviteEvent_FK`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userEmail` varchar(50) NOT NULL,
  `userPwd` varchar(100) NOT NULL,
  `userToken` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userEmail` (`userEmail`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `event_ibfk_1` FOREIGN KEY (`eventOwner_FK`) REFERENCES `user` (`userID`) ON DELETE CASCADE;

--
-- Contraintes pour la table `invite`
--
ALTER TABLE `invite`
  ADD CONSTRAINT `invite_ibfk_1` FOREIGN KEY (`inviteUser_FK`) REFERENCES `user` (`userID`) ON DELETE CASCADE,
  ADD CONSTRAINT `invite_ibfk_2` FOREIGN KEY (`inviteEvent_FK`) REFERENCES `event` (`eventID`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
