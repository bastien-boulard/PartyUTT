<?php
	
class User_db {

	function newUser($email, $pwd, $pseudo) {

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

	function checkToken($email, $token){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
	    	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT userToken FROM User WHERE userEmail = ?');
		$req->execute(array($email));
		$data = $req->fetch();
		$token_db = $data['userToken'];

		$req->closeCursor();

		if (strcmp($token_db, $token) == 0){

			$loginReturn = array(
				'error' => false,
			);
		}
		else {
			$loginReturn = array(
				'error' => true,
			);
		}
		return $loginReturn;
	}

	function setToken($email, $token) {

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('UPDATE User SET userToken = ? WHERE userEmail = ?');
		$req->execute(array($token,$email));

		$req->closeCursor();
	}

	function getPassword($email){

		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT userPwd FROM User WHERE userEmail = ?');
		$req->execute(array($email));

		$data = $req->fetch();
		$passwordDB = $data['userPwd'];

		$req->closeCursor();

		return $passwordDB;
	}

	function getPseudo($email){
		try {
			$bdd = new PDO('mysql:host=localhost;dbname=partyUTT', 'userPartyUTT', 'azerty', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT userPseudo FROM User WHERE userEmail = ?');
		$req->execute(array($email));

		$data = $req->fetch();
		$pseudoDB = $data['userPseudo'];

		$req->closeCursor();

		return $pseudoDB;
	}

	function changePassword() {

	}

	function forgottenPassword() {

	}
}

?>