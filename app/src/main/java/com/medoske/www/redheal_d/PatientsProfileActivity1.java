package com.medoske.www.redheal_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.PatientsReportsAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.PatientInfoItem;
import com.medoske.www.redheal_d.Items.ReportItem;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientsProfileActivity1 extends AppCompatActivity {
    private ListView listView;
    private PatientsReportsAdapter myAppAdapter;
    private ArrayList<ReportItem> postArrayList = new ArrayList<ReportItem>();
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String URL,patientRhid,patientName,patientImage,patientGender,patientReferenceNo;
    TextView textPatientName, textRhid, textReferenceNo, textbloodgroup, textAddress, textGender;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_profile1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Patients Profile");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        patientRhid = sp.getString("patientRId","1");
        patientName = sp.getString("patientName","1");
        patientImage = sp.getString("patientImage","1");
        patientGender = sp.getString("patientGender","1");
        patientReferenceNo = sp.getString("patientReferenceNo","1");


       /* Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            patientRhid = data.getString("patientRId");
            patientName = data.getString("patientName");
            patientImage = data.getString("patientImage");
            patientGender = data.getString("patientGender");
        }else{
            patientRhid = "";
            patientName = "";
            patientImage = "";
            patientGender = "";
        }*/

        URL = Apis.PATIENT_REPORT_URL + patientReferenceNo;
        Log.e("pdetailsUrl", "" + URL);

        textPatientName = (TextView) findViewById(R.id.txtPatientName);
        textPatientName.setText(patientName);
        textPatientName.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        textRhid = (TextView) findViewById(R.id.txtRhid);
        textRhid.setText(patientRhid);
        textRhid.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        textReferenceNo = (TextView) findViewById(R.id.txtReferenceNo);
        textReferenceNo.setText("Ref No : "+patientReferenceNo);
        textReferenceNo.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        imageView = (ImageView) findViewById(R.id.patientProfileImg);
//        Picasso.with(getBaseContext()).load(patientImage).placeholder(R.drawable.placeholderimage).into(imageView);

        try {
            Glide.with(this).load(patientImage).placeholder(R.drawable.placeholderimage).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* if (patientImage.isEmpty()) {
            imageView.setImageResource(R.drawable.placeholderimage);
        } else{
            Picasso.with(getBaseContext()).load(patientImage).into(imageView);
        }*/

        listView=(ListView)findViewById(R.id.listReports);
        new ReaderdateList().execute();
    }

    //  AsyncTask Doctors List
    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(PatientsProfileActivity1.this);
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
                JSONArray jsonArray = json.optJSONArray("reports");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String redhealId = jsonObject.optString("patient_redhealId").toString();
                    String doctorName = jsonObject.optString("doctorName").toString();
                    String clinicName = jsonObject.optString("clinicName").toString();
                    String prescription =jsonObject.optString("prescription").toString();
                    String prescriptionReport =Apis.IP_ADDRESS_URL+"redhealPatient/uploads/reports/"+redhealId+"/prescription/"+jsonObject.optString("prescription_report").toString();
                    String preconsultationReport = Apis.IP_ADDRESS_URL+"callcenter_api/uploads/reports/"+redhealId+"/"+jsonObject.optString("preconsultation_report").toString();
                    String diagnostic = jsonObject.optString("diagnostics").toString();
                    String diagnosticReport ="http://medoske.com/redhealPatient/uploads/reports/"+redhealId+"/diagnostic/"+jsonObject.optString("diagnostic_report").toString();
                     Log.e("reportdiag",""+diagnosticReport);

                    postArrayList.add(new ReportItem(doctorName,redhealId,clinicName,preconsultationReport,prescriptionReport,diagnosticReport));


                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(postArrayList.size()>0)
            {
                myAppAdapter = new PatientsReportsAdapter(PatientsProfileActivity1.this, R.layout.report_list_item, postArrayList);
                listView.setItemsCanFocus(false);
                listView.setAdapter(myAppAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    }
                });
                myAppAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(PatientsProfileActivity1.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }



        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // code here to show dialog
       // startActivity(new Intent(PatientsProfileActivity1.this, PatientsActivity.class));
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
                //startActivity(new Intent(PatientsProfileActivity1.this, PatientsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}