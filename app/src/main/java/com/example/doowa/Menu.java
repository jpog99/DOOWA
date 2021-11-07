package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Menu extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();

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

        latlngs.add(new LatLng(37.567280, 127.055114));
        latlngs.add(new LatLng(37.557280, 127.045114));
        latlngs.add(new LatLng(37.557780, 127.042114));
        latlngs.add(new LatLng(37.512280, 127.021114));
        latlngs.add(new LatLng(37.587280, 127.045614));

        for (LatLng point : latlngs) {
            options.position(point);
            options.title("someTitle");
            options.snippet("someDesc");
            googleMap.addMarker(options);
        }


        //LatLng hanyang = new LatLng(37.557280, 127.045114);
        //map.addMarker(new MarkerOptions().position(hanyang).title("Hanyang1"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(hanyang));



    }
}