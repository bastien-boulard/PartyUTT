<?php
	
require_once ('model/user_db.php');


$user = new User_db();

if (isset($_POST['token'])){
	
	$loginData = $user->checkToken($_POST['email'], $_POST['token']);
}
else {
	$loginData = login($user, $_POST['email'], $_POST['password']);
}
echo json_encode($loginData);


/* 
 * Fonction de login, elle hache le mot de passe envoyé via requête POST et le compare à celui stocké en base pour l'email indiqué 
 * @param User $user
 * @param String $email
 * @param String $pwd
 *
 * @return array $loginReturn
 */
function login($user, $email, $pwd){

	$hashPwd = hash('sha256', $pwd);
	$pwd_db = $user->getPassword($email);

	if (strcmp($pwd_db, $hashPwd) == 0){
		$token = generateToken($email, $pwd);

		$loginReturn = array(
			'error' => false,
			'token' => $token,
		);
		$user->updateToken($email, $token);
	}
	else {
		$loginReturn = array(
			'error' => true,
			'token' => ''
		);
	}
	return $loginReturn;
}

/*
 * Cette fonction génère un token de connexion en utilisant l'email et le mot de passe de l'utilisateur
 * @param String $email
 * @param String $password
 *
 * @return String
 */
function generateToken($email, $password){
	return md5(time() . $email . $password);
}

?>