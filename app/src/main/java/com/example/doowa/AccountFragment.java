package com.example.doowa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    ImageView img_acc;
    TextView txt_accFullname,txt_accUsername;
    TextView googleSignOut;
    RatingBar ratingbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);

        txt_accFullname = (TextView)view.findViewById(R.id.txt_accountFullname);
        txt_accUsername = (TextView)view.findViewById(R.id.txt_accountUsername);
        img_acc = (ImageView) view.findViewById(R.id.img_accountPic);
        ratingbar = (RatingBar) view.findViewById(R.id.ratingBar);

        ratingbar.setRating(4.6f);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(signInAccount!=null){
            txt_accFullname.setText(signInAccount.getGivenName());
            txt_accUsername.setText(signInAccount.getDisplayName());
            Glide.with(this).load(String.valueOf(signInAccount.getPhotoUrl())).into(img_acc);
        }




        googleSignOut = (TextView) view.findViewById(R.id.txt_accSignOut);
        googleSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure you want to log out?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
