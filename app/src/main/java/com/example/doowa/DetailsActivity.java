package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    TextView txt_donationType, txt_detailsReport,txt_name,txt_address,txt_details,txt_time, txt_detailsAsk, txt_detailsType, txt_meetingType, txt_meetingTime;
    ImageView img_image, img_profilepic, img_call, img_msg;
    String phone,imageurl = "";
    private static final String DBRequest_URL = "https://doowa-server.herokuapp.com/requestDB.php";
    List<Requests> requestList;
    private static final int REQUEST_CALL = 1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txt_donationType = (TextView) findViewById(R.id.txt_DetailsdonationType);
        txt_name = (TextView) findViewById(R.id.txt_detailsName);
        txt_address = (TextView) findViewById(R.id.txt_detailsAddress);
        txt_details = (TextView) findViewById(R.id.txt_detailsDesc);
        txt_time = (TextView) findViewById(R.id.txt_detailsTime);
        txt_detailsAsk = (TextView) findViewById(R.id.txt_detailsAsk);
        txt_detailsType = (TextView) findViewById(R.id.txt_detailsType);
        txt_meetingTime = (TextView) findViewById(R.id.txt_detailsmeetingTime);
        txt_meetingType = (TextView) findViewById(R.id.txt_detailsMeetingType);
        txt_detailsReport = (TextView) findViewById(R.id.txt_detailsReport);
        img_image = (ImageView) findViewById(R.id.img_detailsImage);
        img_profilepic = (ImageView) findViewById(R.id.img_detailsProfilePic);
        img_call = (ImageView)findViewById(R.id.img_detailsCall);
        img_msg = (ImageView)findViewById(R.id.img_detailsMsg);
        progressDialog = new ProgressDialog(this);

        img_call.setOnClickListener(this);
        img_msg.setOnClickListener(this);
        txt_detailsReport.setOnClickListener(this);
        img_image.setOnClickListener(this);

        requestList = new ArrayList<>();

        loadRequests();
    }


    private void loadRequests() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.show_dialog);
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
                                        request.getInt("id"),
                                        request.getString("lat"),
                                        request.getString("lng"),
                                        request.getString("phone"),
                                        request.getString("donationType"),
                                        request.getString("address"),
                                        request.getString("details"),
                                        request.getString("image"),
                                        request.getString("report"),
                                        request.getString("time"),
                                        request.getString("name"),
                                        request.getString("profilepic"),
                                        request.getString("meetingTime"),
                                        request.getString("type"),
                                        request.getString("displayName")
                                ));
                            }
                            Log.d("Success", "Fetched from request database successfully!");
                            Intent intent = getIntent();
                            Double markerLat = intent.getDoubleExtra("markerLat",1.0);
                            Double markerLng = intent.getDoubleExtra("markerLng",1.0);

                            Log.d("Success", "Fetched from request database successfully!");

                            for (Requests requests : requestList) {
                                if(Double.parseDouble(requests.lat) == markerLat && Double.parseDouble(requests.lng) == markerLng){
                                    if(requests.type.equals("Request")){
                                        txt_detailsType.setText(getResources().getString(R.string.detail_needHelpRq));
                                        txt_detailsAsk.setText(getResources().getString(R.string.detail_giveHelpRq));
                                        txt_meetingType.setText(getResources().getString(R.string.detail_timeRq));
                                    }else{
                                        txt_detailsType.setText(getResources().getString(R.string.detail_needHelpFb));
                                        txt_detailsAsk.setText(getResources().getString(R.string.detail_giveHelpFb));
                                        txt_meetingType.setText(getResources().getString(R.string.detail_timeFb));
                                    }
                                    txt_time.setText(getResources().getString(R.string.detail_posted) + requests.time);
                                    txt_name.setText(requests.displayName);
                                    txt_donationType.setText(requests.donationType);
                                    txt_address.setText(requests.address);
                                    txt_details.setText(requests.details);
                                    txt_meetingTime.setText(requests.meetingTime);
                                    phone = requests.phone;
                                    if(!requests.profilepic.equals(""))
                                        Glide.with(getApplicationContext()).load(String.valueOf(requests.profilepic)).into(img_profilepic);
                                    if(!requests.image.equals("")){
                                        Glide.with(getApplicationContext()).load(String.valueOf(requests.image)).into(img_image);
                                        imageurl = requests.image;
                                    }
                                    if(requests.meetingTime.equals("")){
                                        txt_meetingType.setVisibility(View.INVISIBLE);
                                        txt_meetingTime.setVisibility(View.INVISIBLE);
                                    }
                                    progressDialog.dismiss();
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
        Intent intent;
        switch (v.getId()){
            case R.id.img_detailsCall:
                makePhoneCall();
                break;
            case R.id.img_detailsMsg:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
                break;
            case R.id.txt_detailsReport:
                intent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.img_detailsImage:
                if(!imageurl.equals("")){
                    intent = new Intent(getApplicationContext(), PreviewActivity.class);
                    intent.putExtra("image",imageurl);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "No image preview available.", Toast.LENGTH_SHORT).show();
                }

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