<?php
require_once ('model/user_db.php');
require_once ('model/event_db.php');

$_POST['email'] = 'johnmcafee@utt.fr';
$_POST['token'] = 'e3784dd368979f7959ebfe968fbad5ef';
$_POST['eventID'] = '1';

$user = new User_db();
$event = new Event_db();

if (isset($_POST['token'])){
	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	if (!$postData['error']){
		$eventData['data'] = $event->getEventData($_POST['eventID']);
		$eventData['guests'] = $event->getGuestList($_POST['eventID']);
	}
}

echo json_encode($eventData);
?>