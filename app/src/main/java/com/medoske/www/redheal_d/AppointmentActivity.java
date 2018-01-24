package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.AppointmentListAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.AppointmentItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;
    private ExpandableLayoutListView listView;
    private AppointmentListAdapter myAppAdapter;
    private ArrayList<AppointmentItem> postArrayList=new ArrayList<AppointmentItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String doctorRedhealId;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Appointments");

// To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        URL= Apis.APPOINTMENT_LIST_URL+doctorRedhealId;
        Log.e("urllll",""+URL);

        listView= (ExpandableLayoutListView)findViewById(R.id.listViewAppointments);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        new ReaderdateList().execute();
                                    }
                                }
        );



    }

    @Override
    public void onRefresh() {
        new ReaderdateList().execute();
    }

    //  AsyncTask Doctors List
    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(AppointmentActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            // progressDialog.setTitle("Chargement...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Fetching ...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(true);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            // showing refresh animation before making http call
            swipeRefreshLayout.setRefreshing(true);

        }


        @Override
        protected JSONObject doInBackground(String... params) {

            // appending offset to url
            String url = URL +"/"+offSet;
            //Getting product details from making HTTP Request
            JSONObject json = jsonParser.makeHttpRequest(url, "GET", param);

            Log.e("url", URL);

            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

                postArrayList.clear();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("appoimentlistdata");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String patientName = jsonObject.optString("patientName").toString();
                    String patientId = jsonObject.optString("patient_redhealId").toString();
                    String time = jsonObject.optString("bookingTime").toString();
                    Log.e("time",""+time);
                    String date = jsonObject.optString("bookingDate").toString();
                    String location = jsonObject.optString("address").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String bookingId = jsonObject.optString("bookingId").toString();
                    String doctorRhid = jsonObject.optString("doctor_redhealId").toString();
                    String clinicRhid = jsonObject.optString("clinic_redhealId").toString();
                    String paymentMode = jsonObject.optString("paymentMode").toString();
                    String imageUrl =jsonObject.optString("imagePath").toString();
                    String status =jsonObject.optString("status").toString();
                    String appointmentRefNo =jsonObject.optString("appointmentRefNo").toString();

                    postArrayList.add(new AppointmentItem(patientName,date,time,location,clinicName,patientId,bookingId,doctorRhid,clinicRhid,paymentMode,imageUrl,status,appointmentRefNo));

                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(postArrayList.size()>0)
            {
                myAppAdapter = new AppointmentListAdapter(AppointmentActivity.this, R.layout.view_row, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);

            }
            else
            {
                Toast.makeText(AppointmentActivity.this, "no data found", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(AppointmentActivity.this,MainActivity.class));
        super.onBackPressed();
    }
}