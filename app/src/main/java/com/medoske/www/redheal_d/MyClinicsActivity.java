package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.MyClinicsAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.Items.PatientInfoItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyClinicsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;
    private ListView listView;
    private MyClinicsAdapter myAppAdapter;
    private ArrayList<MyClinicItem> postArrayList=new ArrayList<MyClinicItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String doctorRedhealId;
    private String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clinics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Clinics");


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        URL= Apis.CLINIC_LIST_URL+doctorRedhealId;
        Log.e("urllll",""+URL);

//fab button
        final FloatingActionButton floatingActionButton =(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClinicsActivity.this, AddClinicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // listview
        listView= (ListView)findViewById(R.id.clinicListView);

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
            progressDialog = new ProgressDialog(MyClinicsActivity.this);
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

            Log.e("url", url);

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
                JSONArray jsonArray = json.optJSONArray("clinicslistdata");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String clinicName = jsonObject.optString("clinicName").toString();
                    String password = jsonObject.optString("password").toString();
                    String primeryNo = jsonObject.optString("primaryNumber").toString();
                    String seconNo = jsonObject.optString("secondaryNumber").toString();
                    String morningTimings = jsonObject.optString("morningTime").toString();
                    String morningDays = jsonObject.optString("morningDays").toString();
                    String afterNoonTimings = jsonObject.optString("afternoonTime").toString();
                    String afterNoonDays = jsonObject.optString("afternoonDays").toString();
                    String eveningTimings = jsonObject.optString("eveningTime").toString();
                    String eveningDays = jsonObject.optString("eveningDays").toString();
                    String clinicRedhealId = jsonObject.optString("clinic_redhealId").toString();

                    String imageUrl ="http://medoske.com/rhdoctor/uploads/clinic/"+doctorRedhealId+"/"+jsonObject.optString("imagePath").toString();
                    Log.e("imageUrel",""+imageUrl);

                    String street = jsonObject.optString("street").toString();
                    String landmark = jsonObject.optString("landmark").toString();
                    String pincode = jsonObject.optString("pincode").toString();
                    String areaName = jsonObject.optString("areaName").toString();
                    String cityName = jsonObject.optString("cityName").toString();

                    Log.e("areaName",""+areaName);
                    Log.e("cityName",""+cityName);

                    postArrayList.add(new MyClinicItem(clinicName,password,primeryNo,seconNo,morningTimings,morningDays,afterNoonTimings,afterNoonDays,eveningTimings,eveningDays,clinicRedhealId,imageUrl,street,landmark,areaName,cityName,pincode));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}


            if(postArrayList.size()>0)
            {
                myAppAdapter = new MyClinicsAdapter(MyClinicsActivity.this, R.layout.clinic_list_item, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);
               // myAppAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(MyClinicsActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }




        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
             return true;
        }
        return super.onOptionsItemSelected(item);
    }
}