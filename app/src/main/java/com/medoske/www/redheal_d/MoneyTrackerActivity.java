package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.ActivityListAdapter;
import com.medoske.www.redheal_d.Adapters.MoneyTrackerListAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.ActivityItem;
import com.medoske.www.redheal_d.Items.MoneyTrackerItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoneyTrackerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    ArrayList<MoneyTrackerItem> moneyTrackerItems =new ArrayList<>();
    MoneyTrackerListAdapter moneyTrackerListAdapter;
    ListView listView;
    String URL ;
    String doctorRedhealId;
    String month;
    TextView txt_Income,txt_Patients,txt_PatientNo,txt_GetIncome,txt_Offline,txt_Online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Money Tracker");


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        listView =(ListView)findViewById(R.id.listMoneyTracker);

        txt_Income=(TextView)findViewById(R.id.txtAmount) ;
        txt_Income.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Medium.ttf"));

        txt_Patients=(TextView)findViewById(R.id.txtPatients);
        txt_Patients.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Medium.ttf"));

        txt_PatientNo=(TextView)findViewById(R.id.textPatients) ;
        txt_PatientNo.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        txt_GetIncome=(TextView)findViewById(R.id.textGet) ;
        txt_GetIncome.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));




        // Spinner Year element
        Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_year);

        // Spinner Drop down elements
        List<String> years = new ArrayList<String>();
        years.add("2017");



        // Creating adapter for spinner
        ArrayAdapter<String> yearDataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, years);

        // Drop down layout style - list view with radio button
        yearDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerYear.setAdapter(yearDataAdapter);

        spinnerYear.setPrompt("Select Month");

        // Spinner click listener
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = parent.getItemAtPosition(position).toString();
                Log.e("year",""+year);

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + year, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Spinner Month element
        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_month);

        // Spinner Drop down elements
        List<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        // Creating adapter for spinner
        ArrayAdapter<String> monthDataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, months);

        // Drop down layout style - list view with radio button
        monthDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerMonth.setAdapter(monthDataAdapter);

        spinnerMonth.setPrompt("Select Month");

        // Spinner click listener
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = parent.getItemAtPosition(position).toString();
                Log.e("Month",""+month);

                URL= Apis.MONEY_TRACKER_URL+doctorRedhealId+"/"+month;
                Log.e("url",""+URL);

                new ReaderdateList().execute(URL);

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + month, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    //  AsyncTask Doctors List
    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(MoneyTrackerActivity.this);
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

        }


        @Override
        protected JSONObject doInBackground(String... params) {

            //Getting product details from making HTTP Request
            JSONObject json = jsonParser.makeHttpRequest(URL, "GET", param);

            Log.e("url", URL);

            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray1 = json.optJSONArray("monthly_income");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray1.length(); i++){

                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                    String year = jsonObject1.optString("year").toString();
                    String month = jsonObject1.optString("month").toString();
                    String amount = jsonObject1.optString("amount").toString();
                    Log.e("amount",""+amount);
                    String patients = jsonObject1.optString("patients").toString();

                    txt_Income.setText(amount);
                    txt_Patients.setText(patients);

                }


                JSONArray jsonArray = json.optJSONArray("monthly_clinic_income");
                //Iterate the jsonArray and print the info of JSONObjects
                for(int j=0; j < jsonArray.length(); j++){

                    JSONObject jsonObject = jsonArray.getJSONObject(j);

                    String clinicName = jsonObject.optString("clinicName").toString();
                    String clinic_redhealId = jsonObject.optString("clinic_redhealId").toString();
                    String year = jsonObject.optString("year").toString();
                    Log.e("year",""+year);
                    String month = jsonObject.optString("month").toString();
                    String patients = jsonObject.optString("patients").toString();
                    String amount = jsonObject.optString("amount").toString();
                    String online = jsonObject.optString("online").toString();
                    Log.e("online",""+online);
                    String offline = jsonObject.optString("offline").toString();

                   moneyTrackerItems.add(new MoneyTrackerItem(clinicName,clinic_redhealId,year,month,patients,amount,online,offline));



                }

                if (moneyTrackerItems.size()>0 ){

                    moneyTrackerListAdapter =new MoneyTrackerListAdapter(MoneyTrackerActivity.this,R.layout.money_tracker_list_item,moneyTrackerItems);
                    listView.setAdapter(moneyTrackerListAdapter);
                }
                else {
                    Toast.makeText(MoneyTrackerActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
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

        startActivity(new Intent(MoneyTrackerActivity.this,MainActivity.class));
        super.onBackPressed();
    }
}
