<?php
$fn = $_POST["fn"];
$ln = $_POST["ln"];
$e = $_POST["e"];
$p = $_POST["p"];
$cp = $_POST["cp"];

if($fn == "" || $ln == "" || $e == "" || $p == "" || $cp == ""){
 $result[] = array('fn' => $fn, 'ln' => $ln, 'status' => 1, 'string' => "a string");
 $js = json_encode($result);
 echo $js;
}

else if($p != $cp){
 $result[] = array('fn' => $fn, 'ln' => $ln, 'status' => 2, 'string' => "a string");
 $js = json_encode($result);
 echo $js;
}

else{
 $dbname = "cglashe1_firstDB";
 $dbpwd = "Thaiw4Nooghu";
 $host = "mysql.cs.binghamton.edu";
 $dbuser = "cglashe1";
 $cid = mysqli_connect($host, $dbuser, $dbpwd, $dbname);
 $sql = "select * from users where email = '$e'";
 $resultQ = mysqli_query($cid, $sql);
 if (mysqli_num_rows($resultQ)!=0){
  $result[] = array('fn' => $fn, 'ln' => $ln, 'status' => 3, 'string' => "a string");
  $js = json_encode($result);
  echo $js;
 }
 else{
  $sql = "insert into users (fn, ln, email, passcode, sessionkey)
values ('$fn', '$ln', '$e', '$p', 0)";
  $resultQ = mysqli_query($cid, $sql);
  $result[] = array('fn' => $fn, 'ln' => $ln, 'status' => 0, 'string' => "a string");
  $js = json_encode($result);
  echo $js;
 }
}
?>
