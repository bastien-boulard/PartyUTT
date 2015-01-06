<?php
	
class Event_db {

	function newEvent($email, $pwd, $pseudo) {

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('INSERT INTO User (userEmail, userPwd, userPseudo) VALUES (?, SHA2(?, 256), ?');
		$req->execute(array($email, $pwd, $pseudo));

		$req->closeCursor();
	}

	function getEventData($eventID){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT e.eventName,  e.eventDate, e.eventAddress, o.userPseudo
							  FROM user as o, event as e 
							  WHERE e.eventID = ? AND e.eventOwner_FK = o.userID');

		$req->execute(array($eventID));
		$data = $req->fetch();
		$req->closeCursor();

		return $data;
	}

	function getGuestList($eventID){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT g.userPseudo, i.isOrga, i.isComing
							  FROM user as g, invite as i
							  WHERE i.inviteEvent_FK = ? AND g.userID = inviteUser_FK');

		$req->execute(array($eventID));
		$data = $req->fetchAll();
		$req->closeCursor();

		return $data;
	}

	function updateEvent(){

	}
}

?>