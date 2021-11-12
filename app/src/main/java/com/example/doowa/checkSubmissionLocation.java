package com.example.doowa;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.doowa.databinding.ActivitySetLocationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class checkSubmissionLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySetLocationBinding binding;
    String lat,lng;

    MarkerOptions marker = new MarkerOptions();
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_submission_location);

        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng pos = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        mMap.addMarker(marker
                .position(pos));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
    }

}