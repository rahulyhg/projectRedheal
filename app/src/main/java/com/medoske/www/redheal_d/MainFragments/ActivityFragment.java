package com.medoske.www.redheal_d.MainFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.ActivityListAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.ActivityItem;
import com.medoske.www.redheal_d.PatientsProfileActivity1;
import com.medoske.www.redheal_d.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityListAdapter myAppAdapter;
    private ArrayList<ActivityItem> postArrayList=new ArrayList<ActivityItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    View rootView;
    String doctorRedhealId;
    TextView nullText;
    String URL ;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_one, container, false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        // current date and time
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Log.e("currentdate",""+formattedDate);

        // url construct
        URL=Apis.DAILY_ACTIVITY_URL+doctorRedhealId+"/"+formattedDate;

        listView= (ListView)rootView.findViewById(R.id.activityList);
        nullText =(TextView)rootView.findViewById(R.id.txtError);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

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



        return rootView;
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
            progressDialog = new ProgressDialog(getContext());
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
            String url = URL +"/" +offSet;
            //Getting product details from making HTTP Request
            JSONObject json = jsonParser.makeHttpRequest(url, "GET", param);

            Log.e("url", url);

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
                JSONArray jsonArray = json.optJSONArray("today_activity");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String bookingId = jsonObject.optString("bookingId").toString();
                    String bookingDate = jsonObject.optString("bookingDate").toString();
                    String bookingTime = jsonObject.optString("bookingTime").toString();
                    Log.e("time",""+bookingTime);
                    String patient_redhealId = jsonObject.optString("patient_redhealId").toString();
                    String doctor_redhealId = jsonObject.optString("doctor_redhealId").toString();
                    String clinic_redhealId = jsonObject.optString("clinic_redhealId").toString();
                    String appointmentId = jsonObject.optString("appointmentId").toString();
                    String status = jsonObject.optString("status").toString();
                    String patientName = jsonObject.optString("patientName").toString();
                    String doctorFullName = jsonObject.optString("fullName").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String location = jsonObject.optString("location").toString();
                    String paymentMode = jsonObject.optString("paymentMode").toString();
                    String imagePath = jsonObject.optString("imagePath").toString();
                    String appointmentRefNo =jsonObject.optString("appointmentRefNo").toString();


                    postArrayList.add(new ActivityItem(bookingTime,patientName,status,bookingId,bookingDate,patient_redhealId,doctor_redhealId,clinic_redhealId,clinicName,paymentMode,imagePath,appointmentRefNo));

                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(postArrayList.size()>0)
            {
                myAppAdapter = new ActivityListAdapter(getContext(), R.layout.activity_item, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);
                myAppAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      ActivityItem activityItem = (ActivityItem) listView.getItemAtPosition(position);

                        Intent i = new Intent(getContext(), PatientsProfileActivity1.class);

                       /* Bundle data1 = new Bundle();
                        data1.putString("patientRId",activityItem.getPatient_redhealId());// key_name is the name through which you can retrieve it later.
                        data1.putString("patientName",activityItem.getName());
                        data1.putString("patientImage",activityItem.getImagePath());
                        i.putExtras(data1);*/

                        SharedPreferences sp = getContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("patientRId",activityItem.getPatient_redhealId());// key_name is the name through which you can retrieve it later.
                        editor.putString("patientName",activityItem.getName());
                        editor.putString("patientImage",activityItem.getImagePath());
                        editor.commit();


                        startActivity(i);
                    }
                });

                nullText.setVisibility(View.INVISIBLE);

            }
            else
            {
                Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();


                 nullText.setText("No Data Found In this Activity");
                 nullText.setVisibility(View.VISIBLE);
//                myAppAdapter.notifyDataSetChanged();
            }


            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);


        }

    }



}




