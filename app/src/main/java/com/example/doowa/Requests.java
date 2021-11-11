package com.example.doowa;

public class Requests {

    String lat;
    String lng;
    String phone, donationType, time,address, image,name,profilepic,details,meetingTime,type;
    String report;

    public Requests(){
    }

    public Requests(String lat, String lng, String phone, String donationType, String address, String details, String image, String report, String time, String name, String profilepic, String meetingTime,String type){
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
        this.meetingTime = meetingTime;
        this.type = type;
    }
}
