package com.example.doowa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
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
        Intent intent = new Intent(getContext(),DetailsActivity.class);
        startActivity(intent);
    }
}
