<?php 

        //database constants
        require "DataBaseConfigPull.php";
    
    //connecting to database and getting the connection object

    function filterRequests($tablename, $username){
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
        
        //Checking if any error occured while connecting
        if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        die();
        }
        
        //creating a query
        $stmt = $conn->prepare("select * from " . $tablename . " where name = '" . $username . "'");
        
        //executing the query 
        $stmt->execute();
        
        //binding results to the query 
        $stmt->bind_result($id,$lat,$lng,$phone,$donationType,$address,$details,$image,$report,$time,$name,$profilepic,$meetingTime,$type,$displayName);
        
        $requests = array(); 
        
        //traversing through all the result 
        while($stmt->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['lat'] = $lat; 
        $temp['lng'] = $lng; 
        $temp['phone'] = $phone; 
        $temp['donationType'] = $donationType; 
        $temp['address'] = $address; 
        $temp['details'] = $details; 
        $temp['image'] = $image; 
        $temp['report'] = $report; 
        $temp['time'] = $time; 
        $temp['name'] = $name; 
        $temp['profilepic'] = $profilepic; 
        $temp['meetingTime'] = $meetingTime; 
        $temp['type'] = $type; 
        $temp['displayName'] = $displayName; 

        array_push($requests, $temp);
        }
        
        //displaying the result in json format 
        echo json_encode($requests);
    }
    
filterRequests("requesthistory", $_POST['username']);