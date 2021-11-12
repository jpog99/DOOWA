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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment implements View.OnClickListener{

    ImageView img_acc,img_accList,img_accExchange,img_accTrack;
    TextView txt_accFullname,txt_accUsername;
    TextView googleSignOut,txt_bug;
    RatingBar ratingbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);

        txt_accFullname = view.findViewById(R.id.txt_accountFullname);
        txt_accUsername = view.findViewById(R.id.txt_accountUsername);
        img_acc = view.findViewById(R.id.img_accountPic);
        ratingbar = view.findViewById(R.id.ratingBar);
        img_accList = view.findViewById(R.id.img_accList);
        img_accExchange = view.findViewById(R.id.img_accExchange);
        img_accTrack = view.findViewById(R.id.img_accTrack);
        txt_bug = view.findViewById(R.id.txt_accBug);
        googleSignOut = view.findViewById(R.id.txt_accSignOut);
        ratingbar.setRating(4.6f);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(signInAccount!=null){
            txt_accFullname.setText(signInAccount.getGivenName());
            txt_accUsername.setText(signInAccount.getDisplayName());
            Glide.with(this).load(String.valueOf(signInAccount.getPhotoUrl())).into(img_acc);
        }

        //Clickables on layout
        googleSignOut.setOnClickListener(this);
        txt_bug.setOnClickListener(this);
        img_accList.setOnClickListener(this);
        img_accExchange.setOnClickListener(this);
        img_accTrack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.img_accList:
                intent = new Intent(getActivity(),SubmissionListActivity.class);
                intent.putExtra("name",txt_accFullname.getText());
                startActivity(intent);
                break;
            case R.id.txt_accSignOut:
                signOut();
                break;
            case R.id.txt_accBug:
                intent = new Intent(getActivity(),BugReportActivity.class);
                startActivity(intent);
                break;
            case R.id.img_accExchange:
                Toast.makeText(getActivity(), "This feature is coming soon. Stay tuned!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_accTrack:
                intent = new Intent(getActivity(),TrackingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void signOut() {
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
                getActivity().finish();
            }
        });
        builder.show();
    }
}
