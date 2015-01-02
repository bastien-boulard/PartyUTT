-- create database partyUTT --
CREATE DATABASE IF NOT EXISTS partyUTT;
USE partyUTT;

-- create user for the database only with local access for security --
CREATE USER 'userPartyUTT'@'localhost' IDENTIFIED BY 'azerty';
GRANT SELECT , INSERT , UPDATE , DELETE ON `partyUTT`.* TO 'userPartyUTT'@'localhost';

-- create User table --
CREATE TABLE IF NOT EXISTS User (
	userID INT NOT NULL AUTO_INCREMENT,
	userEmail VARCHAR(50) NOT NULL,
	userPwd VARCHAR(100) NOT NULL,
	userPseudo VARCHAR(50) NOT NULL,
	userToken VARCHAR(50) DEFAULT NULL,
	CONSTRAINT PRIMARY KEY (userID),
	CONSTRAINT UNIQUE (userEmail, userPseudo)
);

-- create Event table --
CREATE TABLE IF NOT EXISTS Event (
	eventID INT NOT NULL AUTO_INCREMENT,
	eventOwner_FK INT NOT NULL,
	eventName VARCHAR(50) NOT NULL,
	eventDate DATETIME NOT NULL,
	eventAddress VARCHAR(50) NOT NULL,
	CONSTRAINT PRIMARY KEY (eventID),
	CONSTRAINT FOREIGN KEY (eventOwner_FK) REFERENCES User(userID) ON DELETE CASCADE
);

-- create Invite table --
CREATE TABLE IF NOT EXISTS Invite (
	inviteID INT NOT NULL AUTO_INCREMENT,
	inviteUser_FK INT NOT NULL,
	inviteEvent_FK INT NOT NULL,
	isOrga BOOLEAN NOT NULL DEFAULT 0,
	isComing INT NOT NULL DEFAULT 0,
	CONSTRAINT PRIMARY KEY (inviteID),
	CONSTRAINT FOREIGN KEY (inviteUser_FK) REFERENCES User(userID) ON DELETE CASCADE,
	CONSTRAINT FOREIGN KEY (inviteEvent_FK) REFERENCES Event(eventID) ON DELETE CASCADE
);

-- create ToBring table --
CREATE TABLE IF NOT EXISTS ToBring (
	toBringID INT NOT NULL AUTO_INCREMENT,
	toBringInvite_FK INT NOT NULL,
	toBringName VARCHAR(100) NOT NULL,
	toBringQuantity INT NOT NULL,
	CONSTRAINT PRIMARY KEY (toBringID),
	CONSTRAINT FOREIGN KEY (toBringInvite_FK) REFERENCES Invite(inviteID) ON DELETE CASCADE
);

-- insert some data in the base --
INSERT INTO User (userEmail, userPwd, userPseudo) VALUES ('johnmcafee@utt.fr', SHA2('johnmcafee', 256), 'johnmcafee'), ('kimdotcom@utt.fr', SHA2('kimdotcom', 256), 'kimdotcom');
INSERT INTO Event (eventOwner_FK, eventName, eventDate, eventAddress) VALUES ('2', 'kim\'s MEGA party', '2010-10-10 00:42:00', 'the shire');
INSERT INTO Invite (inviteUser_FK, inviteEvent_FK, isOrga, isComing) VALUES ('1', '1', '1', '1'), ('2', '1', '1', '1');
INSERT INTO ToBring (toBringInvite_FK, toBringName, toBringQuantity) VALUES ('1', 'Hookers', '5'), ('2', 'Tequila', '3');