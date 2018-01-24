package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.MyClinicsAdapter;
import com.medoske.www.redheal_d.Adapters.MyPackagesAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.Items.MyPackageItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPackagesActivity extends AppCompatActivity {
Toolbar toolbar;
    private ListView listView;
    private MyPackagesAdapter myAppAdapter;
    private ArrayList<MyPackageItem> postArrayList=new ArrayList<MyPackageItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String doctorRedhealId;
    private String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Packages");


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        URL= Apis.PACKAGES_URL+doctorRedhealId;
        Log.e("urllll",""+URL);

        //fab button
        final FloatingActionButton floatingActionButton =(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPackagesActivity.this, AddPackagesActivity.class);
                startActivity(intent);
            }
        });

        // listview
        listView= (ListView)findViewById(R.id.packagesListView);

        new ReaderdateList().execute();
    }

    //  AsyncTask Doctors List
    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(MyPackagesActivity.this);
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
                postArrayList.clear();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("packages_list");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String specializationId = jsonObject.optString("specializationId").toString();
                    String packageId = jsonObject.optString("packageId").toString();
                    String specialization = jsonObject.optString("specialization").toString();
                    String packageName = jsonObject.optString("packageName").toString();
                    String actualPrice = jsonObject.optString("actualPrice").toString();
                    String discountPrice = jsonObject.optString("discountPrice").toString();
                    String fromTime = jsonObject.optString("fromTime").toString();
                    String toTime = jsonObject.optString("toTime").toString();
                    String imageUrl ="http://medoske.com/rhdoctor/uploads/package/"+doctorRedhealId+"/"+jsonObject.optString("imagePath").toString();
                    Log.e("imageUrl",""+imageUrl);
                    String description = jsonObject.optString("description").toString();
                    postArrayList.add(new MyPackageItem(packageId,specializationId,specialization,packageName,actualPrice,discountPrice,fromTime,toTime,imageUrl,description));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}



            if(postArrayList.size()>0)
            {
                myAppAdapter = new MyPackagesAdapter(MyPackagesActivity.this, R.layout.package_list_item, postArrayList);
                //list= (ListView) findViewById(R.id.listView);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MyPackageItem myPackageItem =(MyPackageItem)myAppAdapter.getItem(position);

                        Intent intent =new Intent(MyPackagesActivity.this,PackageDetailsActiviy.class);
                        /*intent.putExtra("packageId",myPackageItem.getPackageId());
                        intent.putExtra("packageName",myPackageItem.getPackageName());
                        intent.putExtra("actualPrice",myPackageItem.getActualPrice());
                        intent.putExtra("discountPrice",myPackageItem.getDiscountPrice());
                        intent.putExtra("fromTime",myPackageItem.getFromTime());
                        intent.putExtra("toTime",myPackageItem.getToTime());
                        intent.putExtra("imageUrl",myPackageItem.getImage());
                        intent.putExtra("description",myPackageItem.getDescription());*/

                        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("packageId",myPackageItem.getPackageId());// key_name is the name through which you can retrieve it later.
                        editor.putString("packageName",myPackageItem.getPackageName());
                        editor.putString("actualPrice",myPackageItem.getActualPrice());
                        editor.putString("discountPrice",myPackageItem.getDiscountPrice());
                        editor.putString("fromTime",myPackageItem.getFromTime());
                        editor.putString("toTime",myPackageItem.getToTime());
                        editor.putString("imageUrl",myPackageItem.getImage());
                        editor.putString("description",myPackageItem.getDescription());
                        editor.commit();

                        startActivity(intent);
                    }
                });
            }
            else
            {
                Toast.makeText(MyPackagesActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }


        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(MyPackagesActivity.this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(MyPackagesActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}