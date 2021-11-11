<?php 
 
 //database constants
 define('DB_HOST', 'us-cdbr-east-04.cleardb.com');
 define('DB_USER', 'b61d31d80b2e74');
 define('DB_PASS', '5b824682');
 define('DB_NAME', 'heroku_0981d636c949294');
 
 //connecting to database and getting the connection object
 $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
 
 //Checking if any error occured while connecting
 if (mysqli_connect_errno()) {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 die();
 }
 
 //creating a query
 $stmt = $conn->prepare("SELECT lat,lng,phone,donationType,address,details,image,report,time,name,profilepic,meetingTime,type FROM requests;");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($lat,$lng,$phone,$donationType,$address,$details,$image,$report,$time,$name,$profilepic,$meetingTime,$type);
 
 $requests = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
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


 array_push($requests, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($requests);