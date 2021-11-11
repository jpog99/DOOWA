package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BugReportActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_bugReport;
    Button btn_bugSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);

        txt_bugReport = (TextView) findViewById(R.id.txt_bugText);
        btn_bugSubmit = (Button) findViewById(R.id.btn_bugSubmit);

        btn_bugSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String report = txt_bugReport.getText().toString().trim();
        if(report.isEmpty()){
            txt_bugReport.setError("Please fill the form!");
            txt_bugReport.requestFocus();
            return;
        }
        Toast.makeText(BugReportActivity.this, "Thank you for your submission!", Toast.LENGTH_SHORT).show();
        finish();

    }
}