package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.doowa.databinding.ActivitySetLocationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;

public class SetLocationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String DB_URL = "https://doowa-server.herokuapp.com/insertRequest.php";
    private GoogleMap mMap;
    private ActivitySetLocationBinding binding;
    Button btn_setlocSet,btn_setlocCurrentLoc;
    MarkerOptions marker = new MarkerOptions();
    double lat,lng,currLat,currLng,markerLat,markerLng;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    String name,meetingTime,address,details,donationType,phone,type,time,profilepic,image;
    int report;
    boolean useCurrLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkLocationService();

        btn_setlocSet = (Button)findViewById(R.id.btn_setlocSet);
        btn_setlocCurrentLoc = (Button)findViewById(R.id.btn_setlocCurrentLoc);
        btn_setlocSet.setOnClickListener(this);
        btn_setlocCurrentLoc.setOnClickListener(this);

        getRequestData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(SetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(SetLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }




        mapFragment.getMapAsync(this);
    }

    private void insertRequestDB() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            profilepic = String.valueOf(signInAccount.getPhotoUrl());
            name = signInAccount.getGivenName();
        }
        checkLocationService();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(useCurrLoc){
                    lat = currLat;
                    lng = currLng;
                }else{
                    lat = markerLat;
                    lng = markerLng;
                }
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[13];
                field[0] = "lat";
                field[1] = "lng";
                field[2] = "phone";
                field[3] = "donationType";
                field[4] = "address";
                field[5] = "details";
                field[6] = "image";
                field[7] = "report";
                field[8] = "time";
                field[9] = "name";
                field[10] = "profilepic";
                field[11] = "meetingTime";
                field[12] = "type";
                //Creating array for data
                String[] data = new String[13];
                data[0] = Double.toString(lat);
                data[1] = Double.toString(lng);
                data[2] = phone;
                data[3] = donationType;
                data[4] = address;
                data[5] = details;
                data[6] = image;
                data[7] = String.valueOf(report);
                data[8] = time;
                data[9] = name;
                data[10] = profilepic;
                data[11] = meetingTime;
                data[12] = type;
                PutData putData = new PutData(DB_URL, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Request DB Success")){
                            Toast.makeText(SetLocationActivity.this, "Your " + type + " has been marked on the map successfully! Thank you for your contribution!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else if(result.equals("Request DB Failed")){
                            Toast.makeText(SetLocationActivity.this,"Currently unable to connect to database. Please try again later..", Toast.LENGTH_SHORT).show();
                        }
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                    }
                }
                //End Write and Read data with URL
            }
        });
    }

    private void getRequestData() {

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        meetingTime = intent.getStringExtra("openingHour");
        address = intent.getStringExtra("address");
        details = intent.getStringExtra("details");
        donationType = intent.getStringExtra("donationType");
        phone = intent.getStringExtra("phone");
        type = intent.getStringExtra("type");
        report = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time = dtf.format(now);



        //image left
        image = "";


    }

    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                                      @Override
                                      public void onSuccess(Location location) {
                                          if(location!=null){
                                              mapFragment.getMapAsync(new OnMapReadyCallback() {
                                                  @Override
                                                  public void onMapReady(@NonNull GoogleMap googleMap) {
                                                      LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                                                      currLat = latlng.latitude;
                                                      currLng = latlng.longitude;
                                                  }
                                              });
                                          }
                                      }
                                  }
        );
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng seoulCenter = new LatLng(37.5516389589813, 126.99199317374934);
        lat = seoulCenter.latitude;
        lng = seoulCenter.longitude;
        mMap.addMarker(marker
                .position(seoulCenter)
                .draggable(true)
                .title("Hold and drag to location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoulCenter,10));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDragStart(Marker marker)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                // TODO Auto-generated method stub
                markerLat     = marker.getPosition().latitude;
                markerLng     = marker.getPosition().longitude;
            }

            @Override
            public void onMarkerDrag(Marker marker)
            {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setlocSet:
                useCurrLoc = false;
                confirmationWindow();
                break;
            case R.id.btn_setlocCurrentLoc:
                useCurrLoc = true;
                confirmationWindow();
                break;
        }
    }

    private void checkLocationService() {
        LocationManager lm = (LocationManager)SetLocationActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            Toast.makeText(SetLocationActivity.this,"Please enable location and network services first!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void confirmationWindow() {
        String msg;
        if(useCurrLoc)
            msg = "Are you sure you want to use your current location for your " + type +"?";
        else
            msg = "Are you sure you want to use the marked location for your " + type +"?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage(msg);
        checkLocationService();

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(SetLocationActivity.this, "Your " + type + " has been marked on the map successfully! Thank you for your contribution!", Toast.LENGTH_SHORT).show();

                insertRequestDB();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
}