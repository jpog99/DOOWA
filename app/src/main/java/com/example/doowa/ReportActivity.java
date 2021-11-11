package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_Report;
    Button btn_Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txt_Report = (TextView) findViewById(R.id.txt_reportTxt);
        btn_Submit = (Button) findViewById(R.id.btn_reportSubmit);
        Intent intent = new Intent();

        btn_Submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String report = txt_Report.getText().toString().trim();
        if(report.isEmpty()){
            txt_Report.setError("Please fill the form!");
            txt_Report.requestFocus();
            return;
        }

        Toast.makeText(ReportActivity.this, "Thank you for your submission!", Toast.LENGTH_SHORT).show();
        finish();

    }

}