<?php

require_once ('config.php');

/*
 * Cette classe est la classe de modèle des invitations, elle gère toutes les connections à la base concernant les invitations.
*/
class Invite_db {

	/* 
	 * Création d'une nouvelle invitation
	 * @param int $guestID
	 * @param int $eventID
	 * @param boolean $isOrga
	 */
	function newInvite($guestEmail, $eventID, $isOrga) {

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('INSERT INTO Invite (inviteUser_FK, inviteEvent_FK, isOrga, isComing) SELECT userID, ?, ?, 0 FROM User WHERE userEmail = ?');
		$req->execute(array($eventID, $isOrga, $guestEmail));
		$inviteID = $bdd->lastInsertId();

		$req->closeCursor();

		return $inviteID;
	}


	/* 
	 * Vérifie si un user est déja invité à un évènement
	 * @param String $email
	 * @param int $eventID
	 * @return boolean
	 */
	function isInvited($email, $eventID){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT i.inviteID FROM Invite as i, User as u WHERE i.inviteEvent_FK = ? AND i.inviteUser_FK = u.userID AND u.userEmail = ?');
		$req->execute(array($eventID, $email));

		$data = $req->fetch();

		$req->closeCursor();

		if (empty($data)){
			return False;
		} 
		else { 
			return True;
		}
	}

	/* 
	 * Modifie le statut de l'invité (orga ou invité)
	 * @param String $email
	 * @param int $eventID
	 * @param boolean $isOrga
	 */
	function updateIsOrga($email, $eventID, $isOrga){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('UPDATE Invite SET isOrga = ? WHERE inviteUser_FK IN (SELECT userID FROM User WHERE userEmail = ?) AND inviteEvent_FK = ?');
		$req->execute(array($isOrga, $email, $eventID));

		$req->closeCursor();
	}

	/* 
	 * Renvoie la liste des invitations d'un utilisateur
	 * @param String $email
	 *
	 * @return array $data
	 */
	function listInvitations($email){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT e.eventID, e.eventName,  e.eventDate, o.userEmail as owner, i.isOrga, i.isComing, i.toBring, i.quantity
							  FROM invite as i, user as g, user as o, event as e 
							  WHERE g.userEmail = ? AND g.userID = i.inviteUser_FK AND i.inviteEvent_FK = e.eventID AND e.eventOwner_FK = o.userID');

		$req->execute(array($email));
		$data = $req->fetchAll();
		$req->closeCursor();

		return $data;
	}

	/* 
	 * Modifie ce que l'invité apporte ainsi que la quantité
	 * @param int $inviteID
	 * @param String $toBring
	 * @param int $quantity
	 */
	// function updateToBring($eventID, $guestEmail, $toBring, $quantity){

	// 	try {
	// 		$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
	// 	}
	// 	catch (Exception $e){
 //        	die('Erreur : ' . $e->getMessage());
	// 	}

	// 	$req = $bdd->prepare('UPDATE Invite SET toBring = ?, quantity = ? WHERE inviteUser_FK IN (SELECT userID FROM User WHERE userEmail = ?) 
	// 																		AND inviteEvent_FK = ?');
	// 	$req->execute(array($toBring, $quantity, $guestEmail, $eventID));

	// 	$req->closeCursor();
	// }

	/* 
	 * Modifie ce les données de l'invité: ce qu'il apporte, en quelle quantité et s'il vient ou non
	 * @param int $eventID
	 * @param String $toBring
	 * @param int $quantity
	 * @param boolean $isComing
	 */
	function updateInvite($eventID, $email, $toBring, $quantity, $isComing){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('UPDATE Invite SET toBring = ?, quantity = ?, isComing = ? WHERE inviteUser_FK IN (SELECT userID FROM User WHERE userEmail = ?) 
																			AND inviteEvent_FK = ?');
		$req->execute(array($toBring, $quantity, $isComing, $email, $eventID));

		$req->closeCursor();
	}
}

?>