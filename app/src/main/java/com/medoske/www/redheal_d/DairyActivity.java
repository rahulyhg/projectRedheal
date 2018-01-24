package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.DiaryListAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.AbsentsListItem;
import com.medoske.www.redheal_d.Items.DiaryItem;

import org.apache.http.NameValuePair;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DairyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private List<Event> events;
    private List<AbsentsListItem> absentsListItems;
    TextView nullText;
    TextView txtTitle;

    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String URL, URL1;
    String doctorRedhealId, status;

    long timeStrip;
    long timeStripData;
    long  lng_timeStripData ;

    private ListView listView;
    private DiaryListAdapter myAppAdapter;
    private ArrayList<DiaryItem> postArrayList = new ArrayList<DiaryItem>();
    boolean value; // default value if no value was found


    //    private ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Dairy");

       /* // Actionbar text Font
        txtTitle=(TextView)findViewById(R.id.title);
        txtTitle.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));*/

        nullText =(TextView)findViewById(R.id.txtError);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId", "1");
        status = sp.getString("status", "1");
        Log.e("status", "" + status);

        URL = Apis.DIARY_URL + doctorRedhealId;
        Log.e("urllll", "" + URL);


        listView = (ListView) findViewById(R.id.bookings_listview);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                // toolbar.setTitle(dateFormatForMonth.format(dateClicked));

                events = compactCalendarView.getEvents(dateClicked);
                Log.e("events_dateclicked", "" + events.size());

                if (events != null) {
                    Log.d("eventsnull", events.toString());
                    postArrayList.clear();
                    for (Event booking : events) {
                       // postArrayList.add(new DiaryItem(booking.getTimeInMillis()));
                        timeStripData = booking.getTimeInMillis();
                    }
                    Log.e("timeStripDataview", "" + timeStripData);

                    if(events.size() == 0){
                    lng_timeStripData = 0;
                }else{
                    lng_timeStripData = timeStripData;
                }
                URL1 = Apis.DIARY_DETAILS_URL + lng_timeStripData;
                Log.e("detail_diary", "" + URL1);
                    new DiaryDataList().execute(URL1);

            }


                /*for (int i = 0; i < events.size(); i++) {
                    timeStripData = events.get(i).getTimeInMillis();
                    Log.e("timeStripData", "" + timeStripData);
                    URL1 = Apis.DIARY_DETAILS_URL + timeStripData;
                }*/

                // url construct for events list
                /*Log.e("detail_diary", "" + URL1);
                new DiaryDataList().execute(URL1);*/

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                getSupportActionBar().setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });


        new DiaryData().execute();

    }



    //  AsyncTask Data List
    public class DiaryData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(DairyActivity.this);
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

            final String data = "";
            try {

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("diary");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String bookingId = jsonObject.optString("bookingId").toString();
                    String bookingDate = jsonObject.optString("bookingDate").toString();
                    String bookingTime = jsonObject.optString("bookingTime").toString();
                    Log.e("bookingTime", "" + bookingTime);
                    String patient_redhealId = jsonObject.optString("patient_redhealId").toString();
                    String doctor_redhealId = jsonObject.optString("doctor_redhealId").toString();
                    String clinic_redhealId = jsonObject.optString("clinic_redhealId").toString();
                    String status = jsonObject.optString("status").toString();
                    String patientName = jsonObject.optString("patientName").toString();
                    String fullName = jsonObject.optString("fullName").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String location = jsonObject.optString("location").toString();
                    String timeStamp = jsonObject.optString("timestrip").toString();

                    final int BLACK = 0xFF000000;
                    events = new ArrayList<>();
                    events.clear();
                    try {
                        timeStrip = (long) Double.parseDouble(timeStamp);
                        Log.e("timeStrip", "" + timeStrip);
                    } catch (NumberFormatException e) {
                        System.out.println("NumberFormatException: " + e.getMessage());
                    }

                    //set title on calendar scroll
                    final int finalI = i;


                    events.add(new Event(BLACK, timeStrip, patientName));
                    compactCalendarView.removeEvents(events);
                    compactCalendarView.addEvents(events);
                    compactCalendarView.invalidate();
                }



                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //  AsyncTask Appointments List
    public class DiaryDataList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(DairyActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL1, "GET", param);
            Log.e("url1", URL1);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));


            String data = "";
            try {
                postArrayList.clear();
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("diary_details");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String bookingId = jsonObject.optString("bookingId").toString();
                    String bookingDate = jsonObject.optString("requestDate").toString();
                    String bookingTime = jsonObject.optString("bookingTime").toString();
                    Log.e("bookingTime", "" + bookingTime);
                    String patient_redhealId = jsonObject.optString("patient_redhealId").toString();
                    String doctor_redhealId = jsonObject.optString("doctor_redhealId").toString();
                    String clinic_redhealId = jsonObject.optString("clinic_redhealId").toString();
                    String status = jsonObject.optString("status").toString();
                    String patientName = jsonObject.optString("patientName").toString();
                    String fullName = jsonObject.optString("fullName").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String location = jsonObject.optString("location").toString();
                    String colorCode = jsonObject.optString("color").toString();
                    String paymentMode = jsonObject.optString("paymentMode").toString();
                    // String timeStamp = jsonObject.optString("timestrip").toString();
                    postArrayList.add(new DiaryItem(patient_redhealId, patientName, bookingDate, bookingTime, status, clinicName,colorCode,paymentMode));


                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (postArrayList.size() > 0) {

                listView.setVisibility(View.VISIBLE);
                myAppAdapter = new DiaryListAdapter(DairyActivity.this, R.layout.diary_list_item, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);

                myAppAdapter.notifyDataSetChanged();
                listView.clearFocus();

                nullText.setVisibility(View.INVISIBLE);
            } else {
                listView.setVisibility(View.GONE);
                nullText.setText("No Appointments found this date");
                nullText.setVisibility(View.VISIBLE);
                Toast.makeText(DairyActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }




        }

    }


   /* //  AsyncTask Data List
    public class AbsentsData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(DairyActivity.this);
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

            final String data = "";
            try {

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("diary");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String bookingId = jsonObject.optString("bookingId").toString();
                    String bookingDate = jsonObject.optString("bookingDate").toString();
                    String bookingTime = jsonObject.optString("bookingTime").toString();
                    Log.e("bookingTime", "" + bookingTime);
                    String patient_redhealId = jsonObject.optString("patient_redhealId").toString();
                    String doctor_redhealId = jsonObject.optString("doctor_redhealId").toString();
                    String clinic_redhealId = jsonObject.optString("clinic_redhealId").toString();
                    String status = jsonObject.optString("status").toString();
                    String patientName = jsonObject.optString("patientName").toString();
                    String fullName = jsonObject.optString("fullName").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String location = jsonObject.optString("location").toString();
                    String timeStamp = jsonObject.optString("timestrip").toString();

                    final int BLACK = 0xFF000000;
                    events = new ArrayList<>();
                    events.clear();
                    try {
                        timeStrip = (long) Double.parseDouble(timeStamp);
                        Log.e("timeStrip", "" + timeStrip);
                    } catch (NumberFormatException e) {
                        System.out.println("NumberFormatException: " + e.getMessage());
                    }

                    //set title on calendar scroll
                    final int finalI = i;


                    events.add(new Event(BLACK, timeStrip, patientName));
                    compactCalendarView.removeEvents(events);
                    compactCalendarView.addEvents(events);
                    compactCalendarView.invalidate();
                }



                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dairy_menu_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            startActivity(new Intent(DairyActivity.this, MainActivity.class));
            return true;
        }else if (id==R.id.absents){
            startActivity(new Intent(DairyActivity.this,AbsentsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(DairyActivity.this,MainActivity.class));
    }


}
