<?php
require_once ('model/user_db.php');
require_once ('model/invite_db.php');

// $_POST['email'] = 'johnmcafee@utt.fr';
// $_POST['token'] = 'eb32a5c9402788e1bd48bec87d93fcf9';

$user = new User_db();
$invite = new Invite_db();

if (isset($_POST['token'])){
	$loginData = $user->checkToken($_POST['email'], $_POST['token']);
	$homepageData['error'] = $loginData['error'];

	if (!$loginData['error']){
		$homepageData['events'] = $invite->listInvitations($_POST['email']);
	}
}

echo json_encode($homepageData);
?>