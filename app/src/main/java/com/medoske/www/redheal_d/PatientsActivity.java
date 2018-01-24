package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.PatientInfoItem;
import com.medoske.www.redheal_d.MainFragments.ActivityFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    private ListView listView;
    private PatientListAdapter myAppAdapter;
    private ArrayList<PatientInfoItem> postArrayList = new ArrayList<PatientInfoItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String doctorRedhealId;
    private String URL;
    String mobileNumber, email, dateOfBirth, bloodGroup, address;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" Patients Info");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId", "1");

        URL = Apis.PATIENT_LIST_URL + doctorRedhealId;
        Log.e("url", "" + URL);

        listView = (ListView) findViewById(R.id.patientDetails_List);

        // search view query
        searchView =(SearchView)findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAppAdapter.filter(newText.toString().trim());
                listView.invalidate();
                return true;
            }
        });


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

       // new ReaderdateList().execute();
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
            progressDialog = new ProgressDialog(PatientsActivity.this);
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

            Log.e("urlsuresh", url);

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
                JSONArray jsonArray = json.optJSONArray("patient_details");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String patientName = jsonObject.optString("fullName").toString();
                    String age = jsonObject.optString("age").toString();
                    Log.e("age", "" + age);
                    String patientRId = jsonObject.optString("redhealId").toString();
                    String gender = jsonObject.optString("gender").toString();
                    mobileNumber = jsonObject.optString("mobileNumber").toString();
                    Log.e("mobileNumber", "" + mobileNumber);
                    email = jsonObject.optString("email").toString();
                    bloodGroup = jsonObject.optString("bloodGroup").toString();
                    dateOfBirth = jsonObject.optString("dateOfBirth").toString();
                    address = jsonObject.optString("address").toString();
                    String imageUrl =jsonObject.optString("imagePath").toString();
                    String referenceNo = jsonObject.optString("appointmentRefNo").toString();

                    postArrayList.add(new PatientInfoItem(patientName, age, patientRId, gender,imageUrl, mobileNumber, email, bloodGroup, dateOfBirth, address,referenceNo));

                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(postArrayList.size()>0)
            {
                myAppAdapter = new PatientListAdapter(PatientsActivity.this, R.layout.patient_list_item, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        /* String listData =parent.getItemAtPosition(position).toString();
                    Log.e("listdata",""+listData);*/

                        //Get item at position
                        PatientInfoItem userInfo = (PatientInfoItem) myAppAdapter.getItem(position);
                        Log.e("userinfo123", "" + userInfo);

                        Intent i = new Intent(PatientsActivity.this,PatientsProfileActivity1.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("patientRId",userInfo.getRedhealId());// key_name is the name through which you can retrieve it later.
                        editor.putString("patientName",userInfo.getFirstName());
                        editor.putString("patientImage",userInfo.getImage());
                        editor.putString("patientGender",userInfo.getGender());
                        editor.putString("patientReferenceNo",userInfo.getReferenceNo());
                        editor.commit();


                        /*Bundle data1 = new Bundle();
                        data1.putString("patientRId",userInfo.getRedhealId());// key_name is the name through which you can retrieve it later.
                        data1.putString("patientName",userInfo.getFirstName());
                        data1.putString("patientImage",userInfo.getImage());
                        data1.putString("patientGender",userInfo.getGender());
                        i.putExtras(data1);*/
                        startActivity(i);



                        startActivity(i);



                    }
                });
                myAppAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(PatientsActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }

            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    //    adapter class
    public class PatientListAdapter extends ArrayAdapter<PatientInfoItem> {

        private Activity activity;
        public List<PatientInfoItem> parkingList;
        ArrayList<PatientInfoItem> arraylist;
       // MyClinicItem book;


        // constructor
        public PatientListAdapter(Activity activity, int resource, List<PatientInfoItem> books) {
            super(activity, resource, books);
            this.activity = activity;
            this.parkingList = books;
            arraylist = new ArrayList<PatientInfoItem>();
            arraylist.addAll(parkingList);
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public PatientInfoItem getItem(int position) {
            return parkingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // result for item get position
            final PatientInfoItem productItem = parkingList.get(position);

            ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            // If holder not exist then locate all view from UI file.
            if (convertView == null) {

                // inflate UI from XML file
                convertView = inflater.inflate(R.layout.patient_list_item, parent, Boolean.parseBoolean(null));
                // get all UI view
                holder = new ViewHolder(convertView);
                // set tag for holder
                convertView.setTag(holder);


            } else {
                // if holder created, get tag from view
                holder = (ViewHolder) convertView.getTag();
            }

        /*  productItem = parkingList.get(position);
             Log.e("book",""+book);*/
            holder.patientName.setText(productItem.getFirstName());
            Log.e("name", "" + productItem.getFirstName());

            holder.referenecNo.setText("Reference Id "+productItem.getReferenceNo());

           /* holder.gender.setText(productItem.getGender());
            holder.age.setText(productItem.getAge());*/
            holder.patientRid.setText(productItem.getRedhealId());
         //   Picasso.with(activity).load(productItem.getImage()).placeholder(R.drawable.placeholderimage).into(holder.img);

           /* if (productItem.getImage().isEmpty()) {
                holder.img.setImageResource(R.drawable.placeholderimage);
            } else{
                Picasso.with(activity).load(productItem.getImage()).into(holder.img);
            }*/

            return convertView;
        }


        // View Holder
        private class ViewHolder {
            TextView patientName;
            TextView referenecNo;
            /*TextView gender;
            TextView age;*/
            TextView patientRid;
            ImageView img;
            public RelativeLayout relativeLayout;

            public ViewHolder(View v) {

                patientName = (TextView) v.findViewById(R.id.txtFullName);
                patientName.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));
               /* gender = (TextView) v.findViewById(R.id.txtGender);
                age = (TextView) v.findViewById(R.id.txtAge);*/
                referenecNo=(TextView)v.findViewById(R.id.txtReferenceId);
                patientRid = (TextView) v.findViewById(R.id.txtRhid);
                patientRid.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

                img = (ImageView) v.findViewById(R.id.imageViewpatient);

               // relativeLayout = (RelativeLayout) v.findViewById(R.id.relative);

            }
        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            parkingList.clear();
            if (charText.length() == 0) {
                parkingList.addAll(arraylist);

            } else {
                for (PatientInfoItem postDetail : arraylist) {
                    if (charText.length() != 0 && postDetail.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getRedhealId().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(PatientsActivity.this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // startActivity(new Intent(MyAppointmentsActivity.this,Home.class));
        }*/

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(new Intent(PatientsActivity.this,MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*//*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                myAppAdapter.filter(searchQuery.toString().trim());
                listView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }*/

}