package com.example.doowa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private List<Requests> list;
    private static final String DBRequest_URL = "https://doowa-server.herokuapp.com/deleteRequestHistory.php";
    private static final String DB_URL = "https://doowa-server.herokuapp.com/restoreRequest.php";
    private Context mCtx;

    public HistoryListAdapter(Context mCtx, List<Requests> list) {
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
    public void onBindViewHolder(HistoryListAdapter.ViewHolder holder, int position) {
        Requests rq = list.get(position);
        holder.textViewHead.setText(rq.getType());
        holder.textViewDesc.setText(rq.getTime());

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu_history);
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
                            case R.id.menu4:
                                restoreSubmission(rq);
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    private void restoreSubmission(Requests rq) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[15];
                field[0] = "id";
                field[1] = "lat";
                field[2] = "lng";
                field[3] = "phone";
                field[4] = "donationType";
                field[5] = "address";
                field[6] = "details";
                field[7] = "image";
                field[8] = "report";
                field[9] = "time";
                field[10] = "name";
                field[11] = "profilepic";
                field[12] = "meetingTime";
                field[13] = "type";
                field[14] = "displayName";
                //Creating array for data
                String[] data = new String[15];
                data[0] = Integer.toString(rq.id);
                data[1] = rq.lat;
                data[2] = rq.lng;
                data[3] = rq.phone;
                data[4] = rq.donationType;
                data[5] = rq.address;
                data[6] = rq.details;
                data[7] = rq.image;
                data[8] = rq.report;
                data[9] = rq.time;
                data[10] = rq.name;
                data[11] = rq.profilepic;
                data[12] = rq.meetingTime;
                data[13] = rq.type;
                data[14] = rq.displayName;
                PutData putData = new PutData(DB_URL, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Request DB Success")){
                            Toast.makeText(mCtx, "Your submission has been restored succssfully!", Toast.LENGTH_SHORT).show();
                            deleteSubmission(rq.id);
                            ((Activity)mCtx).finish();
                        }else if(result.equals("Request DB Failed")){
                            Toast.makeText(mCtx,"Currently unable to connect to database. Please try again later..", Toast.LENGTH_SHORT).show();
                        }
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                    }
                }
                //End Write and Read data with URL
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
        builder.setMessage("Are you sure to delete this submission from the history? (This cannot be undone)");

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
                Toast.makeText(mCtx, "Your submission has been deleted successfully!", Toast.LENGTH_SHORT).show();
                ((Activity)mCtx).finish();
            }
        });
        builder.show();
    }
}