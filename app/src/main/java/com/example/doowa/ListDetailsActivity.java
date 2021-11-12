package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ListDetailsActivity extends AppCompatActivity {
    TextView txt_donationType,txt_name,txt_address,txt_details,txt_time, txt_detailsType, txt_meetingType, txt_meetingTime;
    ImageView img_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        txt_donationType = findViewById(R.id.txt_LDetailsdonationType);
        txt_name = findViewById(R.id.txt_detailsNameL);
        txt_address = findViewById(R.id.txt_detailsAddressL);
        txt_details = findViewById(R.id.txt_detailsDescL);
        txt_time = findViewById(R.id.txt_detailsTimeL);
        txt_detailsType = findViewById(R.id.txt_detailsTypeL);
        txt_meetingTime = findViewById(R.id.txt_detailsmeetingTimeL);
        txt_meetingType = findViewById(R.id.txt_detailsMeetingTypeL);
        img_image = findViewById(R.id.img_detailsImageL);

        Intent intent = getIntent();

        txt_donationType.setText(intent.getStringExtra("donationType"));
        txt_name.setText(intent.getStringExtra("name"));
        txt_address.setText(intent.getStringExtra("address"));
        txt_details.setText(intent.getStringExtra("details"));
        txt_time.setText("Posted on: "+ intent.getStringExtra("time"));
        txt_detailsType.setText(intent.getStringExtra("type"));
        txt_meetingTime.setText(intent.getStringExtra("openingHour"));

        if(txt_detailsType.getText().equals("Foodbank")){
            txt_meetingType.setText("Operating Hours: ");
        }else
            txt_meetingType.setText("Meeting Hour: ");

        String imageurl = intent.getStringExtra("image");
        if(!imageurl.equals(""))
            Glide.with(this).load(imageurl).into(img_image);

    }
}