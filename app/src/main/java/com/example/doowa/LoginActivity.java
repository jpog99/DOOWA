package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signin;
    private TextView register,forgotpass;
    private EditText txt_email,txt_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email = (EditText)findViewById(R.id.username);
        txt_password = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.pgb_login);

        signin = (Button) findViewById(R.id.btn_login);
        signin.setOnClickListener(this);

        register = (TextView) findViewById(R.id.txt_register);
        register.setOnClickListener(this);

        forgotpass = (TextView) findViewById(R.id.btn_forgotpass);
        forgotpass.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                userSignIn();
                break;
            case R.id.txt_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btn_forgotpass:
                startActivity(new Intent(this,ForgotPassActivity.class));
                break;
        }
    }

    private void userSignIn() {
        String email,password;
        email = txt_email.getText().toString().trim();
        password = txt_password.getText().toString().trim();

        if(email.isEmpty()){
            txt_email.setError("Username is required!");
            txt_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txt_password.setError("Password is required!");
            txt_password.requestFocus();
            return;
        }
        signin.onEditorAction(EditorInfo.IME_ACTION_DONE);
        //Start ProgressBar first (Set visibility VISIBLE)
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "password";
                //Creating array for data
                String[] data = new String[2];
                data[0] = email;
                data[1] = password;
                PutData putData = new PutData("https://doowa-server.herokuapp.com/login.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        progressBar.setVisibility(View.GONE);
                        String result = putData.getResult();
                        if(result.equals("Login Success")){
                            Toast.makeText(LoginActivity.this, "Logged In Successfuly!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();
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