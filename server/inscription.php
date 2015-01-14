<?php
require_once ('model/user_db.php');


$user = new User_db();

if (isset($_POST['email']) && isset($_POST['password'])){

	if (!$user->userExists($_POST['email'])){
		$userData['userID'] = $user->newUser($_POST['email'], $_POST['password']);
		$userData['error'] = 'false';
	} 
	else {
		$userData['error'] = 'true';
	}
}

echo json_encode($userData);

?>