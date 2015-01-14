<?php

require_once ('config.php');

/*
 * Cette classe est la classe de modèle de l'utilisateur, elle gère toutes les connections à la base concernant les données de l'utilisateur.
*/	
class User_db {

	/* 
	 * Création d'un nouvel utilisateur
	 * @param String $email
	 * @param String $pwd
	 *
	 * @return int $userID
	 */
	function newUser($email, $pwd) {

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('INSERT INTO User (userEmail, userPwd) VALUES (?, SHA2(?, 256))');
		$req->execute(array($email, $pwd));
		$userID = $bdd->lastInsertId();

		$req->closeCursor();
		return $userID;
	}

	/* 
	 * Vérifie si un utilisateur existe
	 * @param String $email
	 *
	 * @return boolean $data
	 */
	function userExists($email){
		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT userID FROM User WHERE userEmail = ?');
		$req->execute(array($email));
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
	 * Verifie le token de connexion de l'utilisateur
	 * @param String $email
	 * @param String $token
	 *
	 * @return array $loginReturn
	 */
	function checkToken($email, $token){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
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

	/* 
	 * Récupère le token de connexion d'un utilisateur
	 * @param String $email
	 *
	 * @return String $token_db
	 */
	function getToken($email){
		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
		}
		catch (Exception $e){
	    	die('Erreur : ' . $e->getMessage());
		}

		$req = $bdd->prepare('SELECT userToken FROM User WHERE userEmail = ?');
		$req->execute(array($email));
		$data = $req->fetch();
		$token_db = $data['userToken'];

		$req->closeCursor();

		return $token_db;
	}

	/* 
	 * Modifie le token de l'utilisateur
	 * @param String $email
	 * @param String $token
	 */
	function updateToken($email, $token) {

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

		}
		catch (Exception $e){
        	die('Erreur : ' . $e->getMessage());
		}

		if (empty($token)){
			$req = $bdd->prepare('UPDATE User SET userToken = NULL WHERE userEmail = ?');
			$req->execute(array($email));
		}
		else {
			$req = $bdd->prepare('UPDATE User SET userToken = ? WHERE userEmail = ?');
			$req->execute(array($token,$email));
		}
		
		$req->closeCursor();
	}

	/* 
	 * Récupère le hash du password de l'utilisateur
	 * @param String $email
	 *
	 * @return String $passwordDB
	 */
	function getPassword($email){

		try {
			$bdd = new PDO('mysql:host='.Conf::DB_HOST.';dbname='.Conf::DB_NAME, Conf::DB_USER, Conf::DB_PWD, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));

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

	/* 
	 * Modifie le mot de passe de l'utilisateur
	 * @param String $email
	 * @param String $newPwd
	 */
	function updatePassword($email, $newPwd) {

	}
}

?>