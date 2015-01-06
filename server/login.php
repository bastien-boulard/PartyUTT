<?php
	
require_once ('model/user_db.php');

// $_POST['email'] = 'johnmcafee@utt.fr';
// $_POST['password'] = 'johnmcafee';
// $_POST['token'] = '5c1f92225dc7b7e3791c29d95710f160';

$user = new User_db();

if (isset($_POST['token'])){
	$loginData = $user->checkToken($_POST['email'], $_POST['token']);
}
else {
	$loginData = login($user, $_POST['email'], $_POST['password']);
}
echo json_encode($loginData);


/* functions used for the login action */
function login($user, $email, $pwd){

	$hashPwd = hash('sha256', $pwd);
	$pwd_db = $user->getPassword($email);

	if (strcmp($pwd_db, $hashPwd) == 0){
		$token = generateToken($email, $pwd);

		$loginReturn = array(
			'error' => false,
			'token' => $token,
			'pseudo' => $user->getPseudo($email)
		);
		$user->setToken($email, $token);
	}
	else {
		$loginReturn = array(
			'error' => true,
			'token' => ''
		);
	}
	return $loginReturn;
}

// function checkToken($email, $token){
// 	$user = new User_db();
// 	$token_db = $user->getToken($email);
// 	if (strcmp($token_db, $token) == 0){
// 		$loginReturn = array(
// 			'error' => false,
// 			'token' => $token
// 		);
// 	}
// 	else {
// 		// echo ('YOU SHALL NOT PASS !!');
// 		$loginReturn = array(
// 			'error' => true,
// 			'token' => ''
// 		);
// 	}
// 	return $loginReturn;
// }

function generateToken($email, $password){
	return md5(time() . $email . $password);
}

?>