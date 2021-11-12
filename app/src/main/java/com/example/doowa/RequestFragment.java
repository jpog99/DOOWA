package com.example.doowa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner dropdown;
    ArrayAdapter<String> myAdapter;
    TextView txt_name,txt_openingHour,txt_address,txt_details,txt_phone,txt_guideline;
    String donationType;
    Button btn_setLocation;
    ImageView camera;
    String name,openingHour,address,details,phone;
    String type = "Request";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_request, container, false);

        dropdown = (Spinner) view.findViewById(R.id.dropdown_rq);
        myAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.donationType_list));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(myAdapter);
        dropdown.setOnItemSelectedListener(this);

        btn_setLocation = (Button) view.findViewById(R.id.btn_rqSetMap);
        txt_name = (TextView) view.findViewById(R.id.txt_rqName);
        txt_openingHour = (TextView) view.findViewById(R.id.txt_rqMeetingHour);
        txt_address = (TextView) view.findViewById(R.id.txt_rqAddress);
        txt_phone = (TextView) view.findViewById(R.id.txt_rqPhone);
        txt_details = (TextView) view.findViewById(R.id.txt_rqDetails);
        camera = (ImageView) view.findViewById(R.id.img_rqCamera);
        txt_guideline = (TextView) view.findViewById(R.id.txt_rqguideline);
        txt_guideline.setMovementMethod(LinkMovementMethod.getInstance());

        btn_setLocation.setOnClickListener(this);
        camera.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_rqCamera:
                Intent i = new Intent(getContext(),CameraActivity.class);
                startActivity(i);
                break;
            case R.id.btn_rqSetMap:
                fbSubmission();
                if(name.isEmpty() ||address.isEmpty() ||details.isEmpty() ||donationType.isEmpty()||phone.isEmpty()){
                    break;
                }else{
                    Intent intent = new Intent(getContext(),SetLocationActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("openingHour",openingHour);
                    intent.putExtra("address",address);
                    intent.putExtra("details",details);
                    intent.putExtra("donationType",donationType);
                    intent.putExtra("phone",phone);
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
                break;
        }
    }

    private void fbSubmission() {

        name = txt_name.getText().toString().trim();
        openingHour = txt_openingHour.getText().toString().trim();
        address = txt_address.getText().toString().trim();
        details = txt_details.getText().toString().trim();
        phone = txt_phone.getText().toString().trim();
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);


        if(name.isEmpty()){
            txt_name.setError("Requester name is required!");
            txt_name.requestFocus();
            return;
        }
        if(address.isEmpty()){
            txt_address.setError("Address is required!");
            txt_address.requestFocus();
            return;
        }
        if(details.isEmpty()){
            txt_details.setError("Details is required!");
            txt_details.requestFocus();
            return;
        }
        if(openingHour.isEmpty()){
            openingHour = "";
        }
        if(phone.isEmpty() || !matcher.matches()){
            txt_phone.setError("Contact number is invalid!");
            txt_phone.requestFocus();
            phone = "";
            return;
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        donationType = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
