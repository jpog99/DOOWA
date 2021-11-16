package com.example.doowa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.Locale;

public class AccountFragment extends Fragment implements View.OnClickListener{

    ImageView img_acc,img_accList,img_accExchange,img_accTrack;
    TextView txt_accFullname,txt_accUsername;
    TextView googleSignOut,txt_bug,txt_history,txt_settings;
    RatingBar ratingbar;
    SharedPreferences prefs;

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
        txt_history = view.findViewById(R.id.txt_accHistory);
        txt_settings = view.findViewById(R.id.txt_accSettings);

        googleSignOut = view.findViewById(R.id.txt_accSignOut);
        ratingbar.setRating(4.6f);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = prefs.getString("username", "");
        String fullname = prefs.getString("fullname", "");


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(signInAccount!=null){
            txt_accFullname.setText(signInAccount.getGivenName());
            txt_accUsername.setText(signInAccount.getDisplayName());
            Glide.with(this).load(String.valueOf(signInAccount.getPhotoUrl())).into(img_acc);
        }else{
            txt_accFullname.setText(username);
            txt_accUsername.setText(fullname);
        }

        //Clickables on layout
        googleSignOut.setOnClickListener(this);
        txt_bug.setOnClickListener(this);
        img_accList.setOnClickListener(this);
        img_accExchange.setOnClickListener(this);
        img_accTrack.setOnClickListener(this);
        txt_history.setOnClickListener(this);
        txt_settings.setOnClickListener(this);
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
            case R.id.txt_accHistory:
                intent = new Intent(getActivity(),HistoryListActivity.class);
                intent.putExtra("name",txt_accFullname.getText());
                startActivity(intent);
                break;
            case R.id.txt_accSettings:
                showChangeLanguageDialog();
                break;
        }
    }

    private void showChangeLanguageDialog() {
        final String[] listitems = {"English","Bahasa Melayu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.language_choose));
        builder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocale("en");
                    getActivity().recreate();
                }
                else if (which == 1){
                    setLocale("ms");
                    getActivity().recreate();
                }

                dialog.dismiss();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,getActivity().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs  = getContext().getSharedPreferences("Settings",getActivity().MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.signout_title));
        builder.setMessage(getResources().getString(R.string.signout_message));

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
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("username");
                editor.commit();
                editor.remove("fullname");
                editor.commit();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.show();
    }
}
