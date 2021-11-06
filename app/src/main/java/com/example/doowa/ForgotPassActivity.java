package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_reqEmail;
    private Button btn_reqEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        txt_reqEmail = (EditText) findViewById(R.id.txt_reqEmail);
        btn_reqEmail = (Button) findViewById(R.id.btn_reqEmail);

        btn_reqEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String email = txt_reqEmail.getText().toString().trim();
        Toast.makeText(ForgotPassActivity.this, email, Toast.LENGTH_SHORT).show();
    }
}