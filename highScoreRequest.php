<?php
//ESTABLISHIN CONNECTION;
require "conn.php";

//Query for select users information;
$sql = "SELECT * FROM login_info";

//processing the query;
$query = mysqli_query($connection, $sql);

$names = array();
$money = array();

//Make apart every information;
$i = 1;
while($column = mysqli_fetch_array($query)){
	$names[$i] =  $column['name'];
	$TL[$i] = $column['TL'];
	$EU[$i] = $column['EU'];
	$DL[$i] = $column['DL'];
	$ANG[$i] = $column['ANG'];
	$i++;
}  

 
echo "{\"users\"".":"."[";
echo json_encode(array("name"=>"$names[1]","TL"=>"$TL[1]","EU"=>"$EU[1]","DL"=>"$DL[1]","ANG"=>"$ANG[1]"));
for($i = 2; $i <= count($names); $i++){
	echo ",".json_encode(array("name"=>"$names[$i]","TL"=>"$TL[$i]","EU"=>"$EU[$i]","DL"=>"$DL[$i]","ANG"=>"$ANG[$i]"));
}  
echo "]}";
?>