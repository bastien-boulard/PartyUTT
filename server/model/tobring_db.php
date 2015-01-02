<?php
	
class ToBring_db {

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

	function updateEvent(){

	}
}

?>