<?php
	
require_once ('model/user_db.php');

$user = new User_db();
$user->updateToken($_POST['email'], '');

?>