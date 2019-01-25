<?php
//Establishing connection;
require "conn.php";

//Gettin User Input using POST Request;
$name = $_POST['name'];
$password = $_POST['password'];

//QUERY;
$sql = "INSERT INTO login_info values('$name', '$password', 1000, 0, 0, 0)";

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