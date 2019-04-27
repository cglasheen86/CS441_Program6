<?php
$like = $_POST["like"];
$email = $POST["email"];

$dbname = "cglashe1_firstDB";
$dbpwd = "Thaiw4Nooghu";
$host = "mysql.cs.binghamton.edu";
$dbuser = "cglashe1";
$cid = mysqli_connect($host, $dbuser, $dbpwd, $dbname);
$sql = "UPDATE MyGuests SET sessionkey='$like' WHERE email='$email";
$resultQ = mysqli_query($cid, $sql);

$resultQ = mysqli_query($cid, "SELECT SUM(sessionkey) as totalsum FROM users");
$row = mysqli_fetch_assoc($resultQ);
$sum = $row['totalsum'];

$result[] = array('sum' => $sum, 'status' => 0, 'string' => "a string");
$js = json_encode($result);
echo $js;
?>
