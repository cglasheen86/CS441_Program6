<?php
$dbname = "cglashe1_firstDB";
$dbpwd = "Thaiw4Nooghu";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpwd, $dbname);
$sql = "select * from users";
$result = mysqli_query($cid, $sql);
while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC))
{
 $fn = $row["fn"];
 $ln = $row["ln"];
 echo "A person in the database is $fn $ln\n";
}
?>
