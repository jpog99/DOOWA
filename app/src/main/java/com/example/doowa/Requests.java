package com.example.doowa;

public class Requests {

    java.util.Date dt = new java.util.Date();

    java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String time = sdf.format(dt);

    double lat;
    double lng;
    String phone, donationType, address, image,name,profilepic,details;
    int report;

    public Requests(){
    }

    public Requests(double lat, double lng, String phone, String donationType, String address, String details, String image, int report, String time, String name, String profilepic){
        this.lat = lat;
        this.lng = lng;
        this.phone = phone;
        this.donationType = donationType;
        this.address = address;
        this.image = image;
        this.report = report;
        this.details = details;
        this.time = time;
        this.name = name;
        this.profilepic = profilepic;
    }
}
