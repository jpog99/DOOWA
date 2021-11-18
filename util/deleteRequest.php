<?php 

        //database constants
    require "DataBaseConfigPull.php";
    
    //connecting to database and getting the connection object

    function filterRequests($table, $id){
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
        
        //Checking if any error occured while connecting
        if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        die();
        }

        //copy data to history table
        $stmt = $conn->prepare("INSERT INTO requesthistory (id,lat,lng,phone,donationType,address,details,image,report,time,name,profilepic,meetingTime,type,displayName) SELECT * FROM requests WHERE id = '" . $id . "'");
        //executing the query 
        $stmt->execute();
        
        //delete data
        $stmt = $conn->prepare("DELETE FROM " . $table . " where id = '" . $id . "'");
        //executing the query 
        $stmt->execute();
        
        echo "done";
    }
    
filterRequests("requests", $_POST['id']);