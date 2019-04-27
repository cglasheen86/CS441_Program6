<?php
$x = $_POST["email"];
$y = $_POST["password"];

$dbname = "cglashe1_firstDB";
$dbpwd = "Thaiw4Nooghu";
$host = "mysql.cs.binghamton.edu";
$dbuser = "cglashe1";
$cid = mysqli_connect($host, $dbuser, $dbpwd, $dbname);
$sql = "select * from users where email = '$x' and passcode = '$y'";
$resultQ = mysqli_query($cid, $sql);

while ($row = mysqli_fetch_array($resultQ, MYSQLI_ASSOC))
{
 $fn = $row["fn"];
 $ln = $row["ln"];
 $email = $row["email"];
}


$result[] = array('fn' => $fn, 'ln' => $ln, 'email' => $email, 'status' => 0, 'string' => "a string");
$js = json_encode($result);
echo $js;
?>
