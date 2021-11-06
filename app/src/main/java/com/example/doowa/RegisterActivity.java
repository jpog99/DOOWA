package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vishnusivadas.advanced_httpurlconnection.PutData;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_username,txt_email,txt_pass,txt_confirmpass,txt_fullname;
    TextView txt_regToLogin;
    private Button btn_register;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.btn_register);
        txt_fullname = (EditText) findViewById(R.id.txt_fullname);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_pass = (EditText) findViewById(R.id.txt_pass);
        txt_confirmpass = (EditText) findViewById(R.id.txt_confirmpass);
        txt_regToLogin = (TextView)findViewById(R.id.txt_regToLogin);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        btn_register.setOnClickListener(this);
        txt_regToLogin.setOnClickListener(this);


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                userRegisteration();
                break;
            case R.id.txt_regToLogin:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void userRegisteration() {
        String fullname,username,password,email,confirmpass;
        fullname = txt_fullname.getText().toString().trim();
        username = txt_username.getText().toString().trim();
        password = txt_pass.getText().toString().trim();
        email = txt_email.getText().toString().trim();
        confirmpass = txt_confirmpass.getText().toString().trim();

        if(fullname.isEmpty()){
            txt_fullname.setError("Full name is required!");
            txt_fullname.requestFocus();
            return;
        }
        if(username.isEmpty()){
            txt_username.setError("Username is required!");
            txt_username.requestFocus();
            return;
        }
        if(email.isEmpty()){
            txt_email.setError("Email is required!");
            txt_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txt_pass.setError("Password is required!");
            txt_pass.requestFocus();
            return;
        }
        if(password.length()<6){
            txt_pass.setError("Password must be 6 or more characters!");
            txt_pass.requestFocus();
            return;
        }
        if(confirmpass.isEmpty()){
            txt_confirmpass.setError("Password confirmation is required!");
            txt_confirmpass.requestFocus();
            return;
        }
        if(!confirmpass.equals(password)){
            txt_confirmpass.setError("Password is different!");
            txt_confirmpass.requestFocus();
            return;
        }


        //Start ProgressBar first (Set visibility VISIBLE)
        progressbar.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[4];
                field[0] = "fullname";
                field[1] = "username";
                field[2] = "password";
                field[3] = "email";
                //Creating array for data
                String[] data = new String[4];
                data[0] = fullname;
                data[1] = username;
                data[2] = password;
                data[3] = email;
                PutData putData = new PutData("https://doowa-server.herokuapp.com/signup.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        progressbar.setVisibility(View.GONE);
                        String result = putData.getResult();
                        if(result.equals("Sign Up Success")){
                            Toast.makeText(RegisterActivity.this, "User has been successfully created!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this,result, Toast.LENGTH_SHORT).show();
                        }
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                    }
                }
                //End Write and Read data with URL
            }
        });
    }
}