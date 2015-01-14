<?php
require_once ('model/user_db.php');
require_once ('model/event_db.php');
require_once ('model/invite_db.php');


$user = new User_db();
$event = new Event_db();

if (isset($_POST['token']) && isset($_POST['email'])){

	/* on recoit les emails des invités et leur statut (orga ou invité) sous la forme suivante: $guestList['email1'] = email  et $guestList['status1'] = 1 (orga) ou 0 (invité),
	 on supprime donc les entrées qui ne concernent pas les invités pour ensuite parcourir le tableau */
	$guestList = $_POST;
	unset($guestList['token']);
	unset($guestList['email']);
	unset($guestList['eventName']);
	unset($guestList['eventDate']);
	unset($guestList['eventAddress']);

	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	if (!$postData['error']){
		$eventData['eventID'] = $event->newEvent($_POST['email'], $_POST['eventName'], $_POST['eventDate'], $_POST['eventAddress']);

		$eventData['guestList'] = $guestList;

		if (!empty($guestList)){

			$invite = new Invite_db();

			foreach ($guestList as $guest => $data){

				if (strpos($guest, 'guestEmail') !== false){
					$guestEmail = $data;
				}
				else if (strpos($guest, 'guestStatus') !== false){
					$invite->newInvite($guestEmail, $eventData['eventID'], $data);
				}
			}
			$eventData['error'] = 'false';
		}
	}
}
else {
	$eventData['error'] = 'true';
}

echo json_encode($eventData);
?>