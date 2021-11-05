package com.example.doowa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText username,email,pass,confirmpass;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);

        username = (EditText) findViewById(R.id.txt_username);
        email = (EditText) findViewById(R.id.txt_email);
        pass = (EditText) findViewById(R.id.txt_pass);
        confirmpass = (EditText) findViewById(R.id.txt_confirmpass);




    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String uid = username.getText().toString().trim();
        String em = email.getText().toString().trim();
        String p = pass.getText().toString().trim();
        String cp = confirmpass.getText().toString().trim();

        if(uid.isEmpty()){
            username.setError("Full name is required!");
            username.requestFocus();
            return;
        }
        if(em.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(p.isEmpty()){
            pass.setError("Password is required!");
            pass.requestFocus();
            return;
        }
        if(p.length()<5){
            pass.setError("Password must be 6 or more characters!");
            pass.requestFocus();
            return;
        }
        if(cp.isEmpty()){
            confirmpass.setError("Password confirmation is required!");
            confirmpass.requestFocus();
            return;
        }
        if(!cp.equals(p)){
            confirmpass.setError("Password is different!");
            confirmpass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(em,p)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        UserData user = new UserData(uid,p,em);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User has been successfully created!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this,Menu.class));
                                }else{
                                    Toast.makeText(RegisterActivity.this,"Failed to register! Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"Failed to register! Please try again.2", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}