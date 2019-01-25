<?php
//Establishing connection;
require "conn.php";

//Gettin User Input Using POST Request;
$name = $_POST['name'];
$password = $_POST['password'];
$TL = $_POST['TL'];
$EU = $_POST['EU'];
$DL = $_POST['DL'];
$ANG = $_POST['ANG'];

//Query;
$sql = "UPDATE login_info set TL = $TL, EU = $EU, DL = $DL, ANG = $ANG where name='$name'";

//Processing QUERY;
$query = mysqli_query($connection, $sql);

//Sending in JSON format;

//Checking if query succeded; 
if($query){
	echo json_encode(array("response"=>"Success"));
}else{
	echo json_encode(array("response"=>"Fail"));
}
?>