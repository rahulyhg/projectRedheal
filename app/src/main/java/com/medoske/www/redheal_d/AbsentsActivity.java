package com.medoske.www.redheal_d;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.AbsentsListAdapter;
import com.medoske.www.redheal_d.Items.AbsentsItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AbsentsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String stFromdate,stEtTodate,doctorRedhealId,stRequestDate;
    String responseStatus,responseReson;
    EditText etFromTime1, etToTime1;
    String fromTime1,toTime1;
    String LIST_URL;

    ListView listView;
    AbsentsListAdapter absentsListAdapter;
    private List<AbsentsItem> absentsItemList = new ArrayList<>();

    private static String TAG = AbsentsActivity.class.getSimpleName();

    String FROMDATE ,TODATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Absents");


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId", "1");

        LIST_URL =Apis.ABSENTS_URL+doctorRedhealId;
        Log.e("listUrl",""+LIST_URL);



        stRequestDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // date Format
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();

        Button button=(Button)findViewById(R.id.submit_Button_absents);
        button.setOnClickListener(this);

        listView =(ListView)findViewById(R.id.listAbsents);

        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);


        // etFromTime1 Format

        etFromTime1 =(EditText)findViewById(R.id.editFromTime);
        etFromTime1.setInputType(InputType.TYPE_NULL);
        etFromTime1.requestFocus();
        etFromTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(AbsentsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(selectedHour < 12 && selectedHour >= 0)
                        {
                            etFromTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        }
                        else {
                            selectedHour -= 12;
                            if(selectedHour == 0)
                            {
                                selectedHour = 12;
                            }
                            etFromTime1.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                        }

                    }
                }, hour, minute,false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // time picker 2
        etToTime1 =(EditText)findViewById(R.id.etToTime);
        etToTime1.setInputType(InputType.TYPE_NULL);
        etToTime1.requestFocus();
        etToTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(AbsentsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(selectedHour < 12 && selectedHour >= 0)
                        {
                            etToTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        }
                        else {
                            selectedHour -= 12;
                            if(selectedHour == 0)
                            {
                                selectedHour = 12;
                            }
                            etToTime1.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                        }

                    }
                }, hour, minute,false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        getData();
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etFromDate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etToDate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

   // absents post data
    private void Absents(){
        /*final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();*/

        FROMDATE = stFromdate+" "+fromTime1;
        Log.e("fromdateetetf",""+FROMDATE);
        TODATE = stEtTodate+" "+toTime1;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.ABSENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);

                        Log.e("URL : ",""+Apis.ABSENTS_URL);

                        try {
                            JSONObject response1 = new JSONObject(String.valueOf(response));
                            Log.e("result",""+response1.getString("status"));

                            responseStatus = response1.getString("status");
                            responseReson = response1.getString("reason");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (responseStatus.equals("failed")){
                            Toast.makeText(AbsentsActivity.this,responseReson,Toast.LENGTH_LONG).show();
                        }
                        else {

                            Intent intent =new Intent(AbsentsActivity.this,AbsentsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                       // Toast.makeText(AbsentsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("from_date",FROMDATE);
                params.put("to_date",TODATE);
                params.put("doctor_redhealId", doctorRedhealId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.etFromDate:
                fromDatePickerDialog.show();
                break;

            case  R.id.etToDate:
                toDatePickerDialog.show();
                break;

            case R.id.submit_Button_absents:
                checkValidation();
                break;
            default:
                break;
        }

    }


    // Check Validation
    private void checkValidation() {
        stFromdate = fromDateEtxt.getText().toString();
        stEtTodate = toDateEtxt.getText().toString();
        fromTime1 =etFromTime1.getText().toString();
        toTime1 =etToTime1.getText().toString();

        // Check for both field is empty or not
        if (stFromdate.equals("") || stEtTodate.equals("") || fromTime1.equals("") || toTime1.equals("")) {
           // loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(AbsentsActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }
        else{
            Absents();
        }

    }


    private void getData() {

        //showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                LIST_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("absents");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String from_date = jsonObject.getString("from_date");
                        String to_date = jsonObject.getString("to_date");
                        String status = jsonObject.getString("active_status");
                        String doctor_redhealId = jsonObject.getString("doctor_redhealId");
                        String id = jsonObject.getString("id");

                        absentsItemList.add(new AbsentsItem(doctor_redhealId,from_date,to_date,status,id));
                    }

                    absentsListAdapter=new AbsentsListAdapter(AbsentsActivity.this,R.layout.absent_item,absentsItemList);
                    listView.setAdapter(absentsListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


               // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //hidepDialog();
            }
        });

        // Adding request to request queue
       // AppController.getInstance().addToRequestQueue(jsonObjReq);

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            startActivity(new Intent(AbsentsActivity.this, DairyActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(AbsentsActivity.this,DairyActivity.class));
    }
}
