package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_donationType,txt_name,txt_address,txt_details,txt_time;
    ImageView img_image, img_profilepic, img_call, img_msg;
    String phone;
    private static final String DBRequest_URL = "http://172.30.1.30/LoginRegister/requestDB.php";
    List<Requests> requestList;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txt_donationType = (TextView) findViewById(R.id.txt_DetailsdonationType);
        txt_name = (TextView) findViewById(R.id.txt_detailsName);
        txt_address = (TextView) findViewById(R.id.txt_detailsAddress);
        txt_details = (TextView) findViewById(R.id.txt_detailsDesc);
        txt_time = (TextView) findViewById(R.id.txt_detailsTime);
        img_image = (ImageView) findViewById(R.id.img_detailsImage);
        img_profilepic = (ImageView) findViewById(R.id.img_detailsProfilePic);
        img_call = (ImageView)findViewById(R.id.img_detailsCall);
        img_msg = (ImageView)findViewById(R.id.img_detailsMsg);

        img_call.setOnClickListener(this);
        img_msg.setOnClickListener(this);

        requestList = new ArrayList<>();

        loadRequests();
    }


    private void loadRequests() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DBRequest_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject request = array.getJSONObject(i);

                                //adding the product to product list
                                requestList.add(new Requests(
                                        request.getDouble("lat"),
                                        request.getDouble("lng"),
                                        request.getString("phone"),
                                        request.getString("donationType"),
                                        request.getString("address"),
                                        request.getString("details"),
                                        request.getString("image"),
                                        request.getInt("report"),
                                        request.getString("time"),
                                        request.getString("name"),
                                        request.getString("profilepic")
                                ));
                            }
                            Log.d("Success", "Fetched from request database successfully!");
                            Intent intent = getIntent();
                            Double markerLat = intent.getDoubleExtra("markerLat",1.0);
                            Double markerLng = intent.getDoubleExtra("markerLng",1.0);

                            Log.d("Success", "Fetched from request database successfully!");

                            for (Requests requests : requestList) {
                                if(requests.lat == markerLat && requests.lng == markerLng){
                                    txt_time.setText(requests.time);
                                    txt_name.setText(requests.name);
                                    txt_donationType.setText(requests.donationType);
                                    txt_address.setText(requests.address);
                                    txt_details.setText(requests.details);
                                    phone = requests.phone;
                                    if(!requests.profilepic.equals(""))
                                        Glide.with(getApplicationContext()).load(String.valueOf(requests.profilepic)).into(img_profilepic);
                                    if(!requests.image.equals(""))
                                        Glide.with(getApplicationContext()).load(String.valueOf(requests.image)).into(img_image);
                                    break;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_detailsCall:
                makePhoneCall();
                break;
            case R.id.img_detailsMsg:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
                break;
        }
    }

    private void makePhoneCall() {
            if (ContextCompat.checkSelfPermission(DetailsActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}