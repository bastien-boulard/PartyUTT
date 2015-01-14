<?php
require_once ('model/user_db.php');
require_once ('model/invite_db.php');


$user = new User_db();
$invite = new Invite_db();


if (isset($_POST['token']) && isset($_POST['email'])){

	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	if (!$postData['error']){

		$invite->updateInvite($_POST['eventID'], $_POST['email'], $_POST['toBring'], $_POST['quantity'], $_POST['isComing']);
	}
}
else {
	$eventData['error'] = 'true';
}

echo json_encode($eventData);
?>