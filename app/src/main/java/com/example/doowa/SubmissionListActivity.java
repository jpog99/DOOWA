package com.example.doowa;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubmissionListActivity extends AppCompatActivity {

    //recyclerview objects
    private RecyclerView recyclerView;
    private static final String DBRequest_URL = "https://doowa-server.herokuapp.com/filterRequest.php";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    List<Requests> requestList;
    ProgressDialog progressDialog;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_list);

        //initializing views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");


        filterRequest();

        requestList = new ArrayList<>();

    }

    private void filterRequest() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.show_dialog);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "username";
                //Creating array for data
                String[] data = new String[1];
                data[0] = name;
                PutData putData = new PutData(DBRequest_URL, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        loadSubmissions(result);
                        Log.i("PutData", result);
                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(SubmissionListActivity.this,"Error during data retrieval", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                //End Write and Read data with URL
            }
        });
    }

    private void loadSubmissions(String result) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DBRequest_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(result);

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

                            adapter = new CustomAdapter(SubmissionListActivity.this, requestList);
                            recyclerView.setAdapter(adapter);
                            if(requestList.size()==0)
                                Toast.makeText(SubmissionListActivity.this, "You have no submission!", Toast.LENGTH_SHORT).show();
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