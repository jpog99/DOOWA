package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Menu extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        LatLng seoulCenter = new LatLng(37.5516389589813, 126.99199317374934);
        LatLng hanyang = new LatLng(37.557280, 127.045114);
        LatLng hanyang2 = new LatLng(37.587280, 127.045614);
        map.addMarker(new MarkerOptions().position(hanyang).snippet("Tap for details").title("Donation type"));
        map.addMarker(new MarkerOptions().position(hanyang2).snippet("Tap for details").title("Donation type"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoulCenter,10));

        map.setOnInfoWindowClickListener(this);



    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
        startActivity(intent);
    }
}