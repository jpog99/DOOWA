package com.example.doowa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;

public class SubmissionListAdapter extends RecyclerView.Adapter<SubmissionListAdapter.ViewHolder> {

    private List<Requests> list;
    private static final String DBRequest_URL = "https://doowa-server.herokuapp.com/deleteRequest.php";
    private Context mCtx;

    public SubmissionListAdapter(Context mCtx, List<Requests> list) {
        this.list = list;
        this.mCtx = mCtx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubmissionListAdapter.ViewHolder holder, int position) {
        Requests rq = list.get(position);
        holder.textViewHead.setText(rq.getType());
        holder.textViewDesc.setText(rq.getTime());

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                intent = new Intent(mCtx,ListDetailsActivity.class);
                                intent.putExtra("name",rq.displayName);
                                intent.putExtra("openingHour",rq.meetingTime);
                                intent.putExtra("address",rq.address);
                                intent.putExtra("details",rq.details);
                                intent.putExtra("donationType",rq.donationType);
                                intent.putExtra("phone",rq.phone);
                                intent.putExtra("type",rq.type);
                                intent.putExtra("image",rq.image);
                                intent.putExtra("time",rq.time);
                                intent.putExtra("profilepic",rq.profilepic);
                                mCtx.startActivity(intent);
                                break;
                            case R.id.menu2:
                                confirmationWindow(rq);
                                break;
                            case R.id.menu3:
                                intent = new Intent(mCtx,checkSubmissionLocation.class);
                                intent.putExtra("lat",rq.lat);
                                intent.putExtra("lng",rq.lng);
                                mCtx.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    private void deleteSubmission(int id) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "id";
                //Creating array for data
                String[] data = new String[1];
                data[0] = String.valueOf(id);
                PutData putData = new PutData(DBRequest_URL, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        Log.i("PutData", result);
                    }
                }
                //End Write and Read data with URL
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }

    private void confirmationWindow(Requests rq) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure to delete this submission from the map?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSubmission(rq.id);
                Toast.makeText(mCtx, "Your submission has been deleted from map successfully!", Toast.LENGTH_SHORT).show();
                ((Activity)mCtx).finish();
            }
        });
        builder.show();
    }
}