<?php
require_once ('model/user_db.php');
require_once ('model/invite_db.php');
require_once ('model/event_db.php');

$user = new User_db();
$invite = new Invite_db();
$event = new Event_db();

if (isset($_POST['email']) && isset($_POST['token'])){

	$loginData = $user->checkToken($_POST['email'], $_POST['token']);
	$homepageData['error'] = $loginData['error'];

	if (!$loginData['error']){
		$homepageData['events'] = $invite->listInvitations($_POST['email']);
		$homepageData['eventsOwned'] = $event->getUserEvents($_POST['email']);
	}
}
else {
	$homepageData['error'] = 'true';
}

echo json_encode($homepageData);
?>