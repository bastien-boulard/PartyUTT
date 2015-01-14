<?php
require_once ('model/user_db.php');
require_once ('model/invite_db.php');


$user = new User_db();
$invite = new Invite_db();

if (isset($_POST['token']) && isset($_POST['email'])){

	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	$eventID = $_POST['eventID'];
	$userEmail = $_POST['email'];
	unset($_POST['token']);
	unset($_POST['email']);
	unset($_POST['eventID']);

	if (!$postData['error']){

		$invite->updateInvite($eventID, $userEmail, $_POST['toBring'], $_POST['quantity'], $_POST['isComing']);

		unset($_POST['toBring']);
		unset($_POST['quantity']);
		unset($_POST['isComing']);

		if (!empty($_POST)){

			foreach ($_POST as $guest){
				if (!$invite->isInvited($guestEmail, $eventID))
					$invite->newInvite($guest, $eventID, 0);
			}
		}
	}
}
else {
	$eventData['error'] = 'true';
}

echo json_encode($eventData);
?>