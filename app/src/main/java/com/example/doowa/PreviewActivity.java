package com.example.doowa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PreviewActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        img = findViewById(R.id.img_preview);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        Glide.with(getApplicationContext()).load(String.valueOf(image)).into(img);
    }
}