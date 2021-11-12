package com.example.doowa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;
    ProgressDialog progressDialog;
    private static final String DBRequest_URL = "https://doowa-server.herokuapp.com/requestDB.php";
    List<Requests> requestList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        progressDialog = new ProgressDialog(getActivity());

        supportMapFragment.getMapAsync(this);

        return view;
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
                                            request.getString("type")
                                    ));
                                }
                                Log.d("Success", "Fetched from request database successfully!");
                                for (Requests requests : requestList) {
                                    LatLng latlng = new LatLng(Double.parseDouble(requests.lat), Double.parseDouble(requests.lng));
                                    if(requests.type.equals("Request")){
                                        switch (requests.donationType){
                                            case "Money":
                                                map.addMarker(new MarkerOptions()
                                                        .position(latlng)
                                                        .snippet("Tap for details")
                                                        .title(requests.donationType)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.money));
                                                break;
                                            case "Necessities":
                                                map.addMarker(new MarkerOptions()
                                                        .position(latlng)
                                                        .snippet("Tap for details")
                                                        .title(requests.donationType)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.necessities));
                                                break;
                                            case "Groceries":
                                                map.addMarker(new MarkerOptions()
                                                        .position(latlng)
                                                        .snippet("Tap for details")
                                                        .title(requests.donationType)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.groceries));
                                                break;
                                            case "Others":
                                                map.addMarker(new MarkerOptions()
                                                        .position(latlng)
                                                        .snippet("Tap for details")
                                                        .title(requests.donationType)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.others));
                                                break;

                                        }
                                    }else{
                                        map.addMarker(new MarkerOptions()
                                                .position(latlng)
                                                .snippet("Tap for details")
                                                .title("Foodbank")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.foodbank));
                                    }
                                }
                                progressDialog.dismiss();

                               /* for(MarkerOptions money : moneyList){
                                    money.visible(false);
                                    Log.i("MARKER", "Money Status " + money.getPosition().latitude);
                                }*/

                               /* //creating adapter object and setting it to recyclerview
                                ProductsAdapter adapter = new ProductsAdapter(getActivity(), productList);
                                recyclerView.setAdapter(adapter);*/
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
            Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        requestList = new ArrayList<>();
        loadRequests();
        LatLng seoulCenter = new LatLng(37.5516389589813, 126.99199317374934);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoulCenter,10));

        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Intent intent = new Intent(getContext(),DetailsActivity.class);
        intent.putExtra("markerLat",marker.getPosition().latitude);
        intent.putExtra("markerLng",marker.getPosition().longitude);
        startActivity(intent);
    }



}
