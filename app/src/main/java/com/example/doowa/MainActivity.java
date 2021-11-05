package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signin;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = (Button) findViewById(R.id.btn_login);
        signin.setOnClickListener(this);


        register = (TextView) findViewById(R.id.txt_register);
        register.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                startActivity(new Intent(this,Menu.class));
                break;
            case R.id.txt_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }
}