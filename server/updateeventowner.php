<?php
require_once ('model/user_db.php');
require_once ('model/invite_db.php');
require_once ('model/event_db.php');


$user = new User_db();
$invite = new Invite_db();
$event = new Event_db();


if (isset($_POST['token']) && isset($_POST['email'])){

	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	$eventID = $_POST['eventID'];
	$userEmail = $_POST['email'];
	unset($_POST['token']);
	unset($_POST['email']);
	unset($_POST['eventID']);

	if (!$postData['error']){

		$event->updateEvent($eventID, $_POST['eventName'], $_POST['eventDate'], $_POST['eventAddress']);

		unset($_POST['eventName']);
		unset($_POST['eventDate']);
		unset($_POST['eventAddress']);

		if (!empty($_POST)){

			foreach ($_POST as $guest => $data){

				if (strpos($guest, 'guestEmail') !== false){
					$guestEmail = $data;
				}
				else if (strpos($guest, 'guestStatus') !== false){
					
					if ($invite->isInvited($guestEmail, $eventID)){
						$invite->updateIsOrga($guestEmail, $eventID, $data);
					}
					else {
						$invite->newInvite($guestEmail, $eventID, $data);
					}
				}
			}
		}
	}
}
else {
	$eventData['error'] = 'true';
}

echo json_encode($eventData);
?>