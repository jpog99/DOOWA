<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['phone']) && isset($_POST['donationType']) && isset($_POST['address'])&& isset($_POST['details'])&& isset($_POST['image'])&& isset($_POST['report'])&& isset($_POST['time'])&& isset($_POST['name'])&& isset($_POST['profilepic'])&& isset($_POST['meetingTime'])&& isset($_POST['type']) && isset($_POST['displayName'])) {
    if ($db->dbConnect()) {
        if ($db->insertRequests("requests", $_POST['lat'], $_POST['lng'], $_POST['phone'], $_POST['donationType'], $_POST['address'], $_POST['details'], $_POST['image'], $_POST['report'], $_POST['time'], $_POST['name'], $_POST['profilepic'], $_POST['meetingTime'], $_POST['type'], $_POST['displayName'])) {
            echo "Request DB Success";
        } else echo "Request DB Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
