<?php
	
class Invite_db {

	function newInvite($guestID, $eventID, $isOrga) {

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('INSERT INTO Invite (inviteUser_FK, inviteEvent_FK, isOrga, isComing) VALUES (?, ?, ?, 2');
		$req->execute(array($guestID, $eventID, $isOrga));

		$req->closeCursor();
	}

	function updateIsComing($inviteID, $isComing){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('UPDATE Invite SET isComing = ? WHERE inviteID = ?');
		$req->execute(array($isComing));

		$req->closeCursor();
	}

	function listInvitations($email){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT e.eventID, e.eventName,  e.eventDate, o.userPseudo, i.isOrga, i.isComing
							  FROM invite as i, user as g, user as o, event as e 
							  WHERE g.userEmail = ? AND g.userID = i.inviteUser_FK AND i.inviteEvent_FK = e.eventID AND e.eventOwner_FK = o.userID');

		$req->execute(array($email));
		$data = $req->fetchAll();
		$req->closeCursor();

		return $data;
	}
}

?>