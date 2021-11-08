package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_reqEmail;
    private Button btn_reqEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        txt_reqEmail = (EditText) findViewById(R.id.txt_emailForgot);
        btn_reqEmail = (Button) findViewById(R.id.btn_forgotPass);

        btn_reqEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String email = txt_reqEmail.getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!email.isEmpty() && matcher.matches()){
            btn_reqEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
            Toast.makeText(ForgotPassActivity.this, "Verification Email has sent to " + email, Toast.LENGTH_SHORT).show();
        }else{
            txt_reqEmail.setError("Invalid Email!");
            txt_reqEmail.requestFocus();
            return;
        }

    }


}