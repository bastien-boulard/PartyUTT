<?php

require_once ('config.php');
	
/*
 * Cette classe est la classe de modèle des évènements, elle gère toutes les connections à la base concernant les évènements.
*/	
class Event_db {

	/* 
	 * Création d'un nouvel évènement
	 * @param String $owner
	 * @param String $name
	 * @param String $date
	 * @param String $address
	 *
	 * @return int $eventID
	 */
	function newEvent($owner, $name, $date, $address) {

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('INSERT INTO Event (eventOwner_FK, eventName, eventDate, eventAddress) SELECT userID, ?, ?, ? FROM User WHERE userEmail = ?');
		$req->execute(array($name, $date, $address, $owner));
		$eventID = $bdd->lastInsertId();

		// $req = $bdd->prepare('INSERT INTO Invite (inviteUser_FK, inviteEvent_FK, isOrga, isComing) SELECT userID, ?, 1, 1 FROM User WHERE userEmail = ?');
		// $req->execute(array($eventID, $owner));

		$req->closeCursor();

		return $eventID;
	}

	/* 
	 * Récupère les évenements créés par l'utilisateur indiqué
	 * @param String $owner
	 *
	 * @return array $data
	 */
	function getUserEvents($owner){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT e.eventID, e.eventName FROM Event as e, User as u WHERE u.userEmail = ? AND u.userID = e.eventOwner_FK');
		$req->execute(array($owner));
		$data = $req->fetchAll();

		$req->closeCursor();
		return $data;
	}

	/* 
	 * Récupère les informations d'un évènement
	 * @param int $eventID
	 *
	 * @return array $data
	 */
	function getEventData($eventID){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT e.eventName,  e.eventDate, e.eventAddress, o.userEmail as owner
							  FROM user as o, event as e 
							  WHERE e.eventID = ? AND e.eventOwner_FK = o.userID');

		$req->execute(array($eventID));
		$data = $req->fetch();
		$req->closeCursor();

		return $data;
	}

	/* 
	 * Récupère la liste des invités à un évènement
	 * @param int $eventID
	 *
	 * @return array $data
	 */
	function getGuestList($eventID){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT g.userEmail, i.isOrga, i.isComing, i.toBring, i.quantity
							  FROM user as g, invite as i
							  WHERE i.inviteEvent_FK = ? AND g.userID = inviteUser_FK');

		$req->execute(array($eventID));
		$data = $req->fetchAll();
		$req->closeCursor();

		return $data;
	}

	/* 
	 * Modifie les données d'un évènement: nom, date et adresse
	 * @param int $eventID
	 */
	function updateEvent($eventID, $newName, $newDate, $newAddress){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('UPDATE Event SET eventName = ?, eventDate = ?, eventAddress = ? WHERE eventID = ?');
		$req->execute(array($newName, $newDate, $newAddress, $eventID));

		$req->closeCursor();
	}
}

?>