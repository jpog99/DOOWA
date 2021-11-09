package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class DetailsActivity extends AppCompatActivity {
    TextView txt_donationType,txt_name,txt_address,txt_details,txt_time;
    ImageView img_image, img_profilepic;
    private static final String DBRequest_URL = "http://172.30.1.30/LoginRegister/requestDB.php";
    List<Requests> requestList;

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
                                    Glide.with(getApplicationContext()).load(String.valueOf(requests.profilepic)).into(img_profilepic);
                                    Glide.with(getApplicationContext()).load(String.valueOf(requests.image)).into(img_image);
                                    continue;

                                }

                            }



                                //creating adapter object and setting it to recyclerview
                                //ProductsAdapter adapter = new ProductsAdapter(getActivity(), productList);
                                //recyclerView.setAdapter(adapter);
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
}