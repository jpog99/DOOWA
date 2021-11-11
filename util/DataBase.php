<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where username = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['username'];
            $dbpassword = $row['password'];
            if ($dbusername == $username && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table, $fullname, $email, $username, $password)
    {
        $fullname = $this->prepareData($fullname);
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $email = $this->prepareData($email);
        //$password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (fullname, username, password, email) VALUES ('" . $fullname . "','" . $username . "','" . $password . "','" . $email . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function insertRequests($table,$lat,$lng,$phone,$donationType,$address,$details,$image,$report,$time,$name,$profilepic,$meetingTime,$type)
    {
        $lat = $this->prepareData($lat); 
        $lng = $this->prepareData($lng); 
        $phone = $this->prepareData($phone); 
        $donationType = $this->prepareData($donationType); 
        $address = $this->prepareData($address); 
        $details = $this->prepareData($details); 
        $image = $this->prepareData($image); 
        $report = $this->prepareData($report); 
        $time = $this->prepareData($time); 
        $name = $this->prepareData($name); 
        $profilepic = $this->prepareData($profilepic); 
        $meetingTime =$this->prepareData($meetingTime); 
        $type = $this->prepareData($type); 

        $this->sql =
            "INSERT INTO " . $table . " (lat,lng,phone,donationType,address,details,image,report,time,name,profilepic,meetingTime,type) VALUES ('" . $lat . "','" . $lng . "','" . $phone . "','" . $donationType . "','" . $address . "','" . $details . "','" . $image . "','" . $report . "','" . $time . "','" . $name . "','" . $profilepic . "','" . $meetingTime . "','" . $type . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

}

?>
