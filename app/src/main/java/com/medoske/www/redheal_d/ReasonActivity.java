package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReasonActivity extends AppCompatActivity {
    RadioButton rb1,rb2,rb3;
    EditText editText;
    Button buttonReason;
    String reason,otherReason;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reason");

        final String appointmentId = getIntent().getExtras().getString("appointmentId","1");
        final String time = getIntent().getExtras().getString("time","1");
        final String date = getIntent().getExtras().getString("date","1");
        final String clinicId = getIntent().getExtras().getString("clinicId","1");
        final String patientId = getIntent().getExtras().getString("patientId","1");
        final String doctorId = getIntent().getExtras().getString("doctorId","1");
        final String appReferenceNo = getIntent().getExtras().getString("appointmentRefernceNo","1");


        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        editText = (EditText)findViewById(R.id.etComments);
        editText.setVisibility(View.GONE);


        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setVisibility(View.GONE);
                    rb2.setChecked(false);
                    reason ="i am in surgery";
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setVisibility(View.GONE);
                    rb2.setChecked(true);
                    reason ="I am out of Station";
                }
            }
        });


        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    editText.setVisibility(View.VISIBLE);
                    rb3.setChecked(true);

                }
            }
        });


        buttonReason=(Button)findViewById(R.id.reasonButton);
        buttonReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ProgressDialog progressDialog;
                //Create a new progress dialog
                progressDialog = new ProgressDialog(ReasonActivity.this);
                //Set the progress dialog to display a horizontal progress bar
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Set the dialog title to 'Loading...'
                progressDialog.setMessage("Fetching ...");
                //This dialog can't be canceled by pressing the back key
                progressDialog.setCancelable(true);
                //This dialog isn't indeterminate
                progressDialog.setIndeterminate(false);
                progressDialog.show();

                otherReason =editText.getText().toString().trim();

                //convert product => json array
                JSONArray jProducts = new JSONArray();
                JSONObject jProduct = new JSONObject();
                String status = "reject";
                String colorCode = "#ef5350";

                try {

                    jProduct.put("bookingId",appointmentId);
                    jProduct.put("bookingTime",time);
                    jProduct.put("bookingDate",date);
                    jProduct.put("clinic_redhealId",clinicId);
                    jProduct.put("patient_redhealId",patientId);
                    jProduct.put("doctor_redhealId",doctorId);
                    jProduct.put("status",status);
                    jProduct.put("reason",reason);
                    jProduct.put("description",otherReason);
                    jProduct.put("color",colorCode);
                    jProduct.put("appointmentRefNo",appReferenceNo);
                    Log.e("appointmentRefNo",""+appReferenceNo);

                    //add to json array
                    jProducts.put(jProduct);
                    Log.d("json api", "Json array converted from Product: " + jProducts.toString());

                    String jsonData = jProduct.toString();
                    Log.e("jsonData",""+jsonData);
                    new DoUpdateAppointment().execute(jsonData);

                    startActivity(new Intent(ReasonActivity.this,AppointmentActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class DoUpdateAppointment extends AsyncTask<String, Void, Void> {
   JSONObject response;
        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                URL url = new URL(Apis.APPOINTMENT_UPDATE_URL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonData);
                osw.flush();
                osw.close();


                InputStream is = connection.getInputStream();
                String result = "";
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;


                }
                Log.d("json_api", "DoUpdateProduct.doInBackground Json return: " + result);


                // return respone
                response = new JSONObject(result);
                Log.e("result",""+response.getString("status"));

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(ReasonActivity.this, response.getString("status"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                is.close();


//                osw.flush();
                osw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
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

        startActivity(new Intent(ReasonActivity.this,AppointmentActivity.class));
        super.onBackPressed();
    }
}
