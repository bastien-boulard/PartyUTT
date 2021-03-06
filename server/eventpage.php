<?php
require_once ('model/user_db.php');
require_once ('model/event_db.php');


$user = new User_db();
$event = new Event_db();

if (isset($_POST['token']) && isset($_POST['email'])){
	
	$postData = $user->checkToken($_POST['email'], $_POST['token']);
	$eventData['error'] = $postData['error'];

	if (!$postData['error']){
		$eventData['data'] = $event->getEventData($_POST['eventID']);
		$eventData['guests'] = $event->getGuestList($_POST['eventID']);
	}
}
else {
	$eventData['error'] = 'true';
}

echo json_encode($eventData);
?>