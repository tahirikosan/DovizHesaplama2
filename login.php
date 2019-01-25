<?php
//Establishing Conection;
require "conn.php";

//Getting User Input using POST;
$name = $_POST['name'];
$password = $_POST['password'];

//Query for checking if combination exist;
$sql = "SELECT name,password,TL,EU,DL,ANG FROM login_info WHERE name='$name' and password='$password'";

//Processing Query;
$query = mysqli_query($connection, $sql);

//Checking if any row returned;
if(mysqli_num_rows($query) > 0){
	$rows = mysqli_fetch_assoc($query);
	$name = $rows['name'];
	$TL = $rows['TL'];
	$EU = $rows['EU'];
	$DL = $rows['DL'];
	$ANG = $rows['ANG']; 
	
	echo json_encode(array("response"=>"$name","response1"=>"$TL","response2"=>"$EU","response3"=>"$DL","response4"=>"$ANG"));
	
}else{
	echo json_encode(array("response"=>"Fail"));
}
?>