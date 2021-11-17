package com.example.doowa;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.doowa.databinding.ActivityFaqactivityBinding;

public class FAQActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);
        text = findViewById(R.id.txt_faq);
        text.setText("1. Do I have to sign up before using the application to donate? " +
                "\n- You have to sign up for verification purposes in order to start donating or seeking any donation. Otherwise, if you are unable to do so, you may not use the application services. Sign up process will only take you for 5 minutes. Alternatively, you can sign up using your Google account for easier process. " +
                "\n\n 2. How can I track my donation process? " +
                "\n- You will be given a tracking number once you have successfully sent the donation. You may track your donation by entering the tracking number on the tracking page. You can also refer to the details for every checkpoint of your tracking.  " +
                "\n\n3. Do I have to have an organization in order to register Food Bank? " +
                "\n- No, you can still register for food bank without having any real organization. Instead, you can register yourself as an organization on the food bank page if you desire to distribute the donations." +
                "\n\n4. What should I do with problematic/harmful content on the app?\n" +
                "-Doowa provide a function where you can report any content or account on the app. Users can do so by clicking on the report button and we will evaluate each report to determine its validity. If it is found to violate our user guidelines and terms of services the account/content will be taken down \n" +
                "\n\n" +
                "5.Is there any way for me to show gratitude to donors who have helped me?\n" +
                "-The Doowa application provide a function where users can give each other rating. So you can show your gratitude by giving good rating to your donors. We also give incentives in form of coupons/gifticons to donors according to how many times they have donated. \n" +
                "\n\n" +
                "6.How do I put a help request?\n" +
                "-You can do so by clicking on the request page at the bottom of your screen. Fill up all the necessary information about your request and set up you location om the map. Donors will be able you see your pinned location on the map once you have completed the information and posted the request.\n" );


    }
}