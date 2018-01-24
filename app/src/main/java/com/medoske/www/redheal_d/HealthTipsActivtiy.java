package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.AppointmentListAdapter;
import com.medoske.www.redheal_d.Adapters.HealthTipsAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.TipsItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HealthTipsActivtiy extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    ArrayList<TipsItem> studentArray = new ArrayList<TipsItem>();
    HealthTipsAdapter studentArrayAdapter;
//    FoldableListLayout foldableListLayout;
ListView foldableListLayout;

    private static String URL ;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips_activtiy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Health Tips");

//        // tool bar
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setTitle("Health Tips");
//        toolbar.setNavigationIcon(R.drawable.back_button);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed(); // Implemented by activity
//            }
//        });

        final FloatingActionButton floatingActionButton =(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthTipsActivtiy.this,AddHealthyTipsActivity.class));
            }
        });

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorId = sp.getString("RedhealId","1");
        String doctorName = sp.getString("doctorName","1");

        // foldable listView
        foldableListLayout = (ListView) findViewById(R.id.foldable_list);

        //-----Tips url
        URL= Apis.TIPS_URL+doctorId;
        Log.e("TIPS_URL",""+URL);


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

    // Asynk Task for Tips
    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Create a new progress dialog
            progressDialog = new ProgressDialog(HealthTipsActivtiy.this);
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
                studentArray.clear();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("healthtips");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String categoryId = jsonObject.optString("categoryId").toString();
                    String doctor_redhealId = jsonObject.optString("doctor_redhealId").toString();
                    String tipName = jsonObject.optString("tipName").toString();
                    String description = jsonObject.optString("description").toString();
                    String imagePath =Apis.TIP_IMAGE_BASEURL+doctorId+"/"+jsonObject.optString("imagePath").toString();
                    Log.e("imageUrl",""+imagePath);
                    String id =jsonObject.optString("id").toString();
                    String categoryName =jsonObject.optString("categoryName").toString();



                    studentArray.add(new TipsItem(categoryId,doctor_redhealId,tipName,description,imagePath,id,categoryName));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

            if(studentArray.size()>0)
            {
                studentArrayAdapter = new HealthTipsAdapter(HealthTipsActivtiy.this, R.layout.tips_list_item, studentArray);
                //list= (ListView) findViewById(R.id.listView);
                foldableListLayout.setAdapter(studentArrayAdapter);
                foldableListLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TipsItem tipsItem =(TipsItem)studentArrayAdapter.getItem(position);

                        /*Intent i = new Intent(HealthTipsActivtiy.this,HealthDetailsActivity.class);
                        i.putExtra("tipUrl", tipsItem.getImagePath());
                        startActivity(i);*/
                    }
                });

            }
            else
            {
                Toast.makeText(HealthTipsActivtiy.this, "no data found", Toast.LENGTH_SHORT).show();
            }

        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tips_menu, menu);

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            startActivity(new Intent(HealthTipsActivtiy.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(HealthTipsActivtiy.this,MainActivity.class));
    }




}
