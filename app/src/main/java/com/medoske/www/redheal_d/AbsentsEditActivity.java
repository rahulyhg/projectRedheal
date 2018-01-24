package com.medoske.www.redheal_d;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AbsentsEditActivity extends AppCompatActivity implements View.OnClickListener {
String id,doctorID;
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
    String FROMDATE ,TODATE;
    String[] separated1,separated2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absents_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Absents Edit");

        id = getIntent().getExtras().getString("id","defaultKey");
        doctorID = getIntent().getExtras().getString("doctorId","defaultKey");
        String fromDate = getIntent().getExtras().getString("fromDate","defaultKey");
        String toDate = getIntent().getExtras().getString("toDate","defaultKey");

         separated1 = fromDate.split(" ");
         separated2 = toDate.split(" ");
        Log.e("from adte",""+separated1[0]+"---"+separated1[1]+"==="+separated1[2]);


        stRequestDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // date Format
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();

        Button button=(Button)findViewById(R.id.submit_Button_absents);
        button.setOnClickListener(this);

        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        // etFromTime1 Format

        etFromTime1 =(EditText)findViewById(R.id.editFromTime);
        etFromTime1.setText(separated1[1]+" "+separated1[2]);
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

                mTimePicker = new TimePickerDialog(AbsentsEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        etToTime1.setText(separated2[1]+" "+separated2[2]);
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

                mTimePicker = new TimePickerDialog(AbsentsEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etFromDate);
        fromDateEtxt.setText(separated1[0]);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etToDate);
        toDateEtxt.setText(separated2[0]);
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
            Toast.makeText(AbsentsEditActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }
        else{
            putData();
        }

    }


    private void putData(){
        FROMDATE = stFromdate+" "+fromTime1;
        Log.e("fromdateetetf",""+FROMDATE);
        TODATE = stEtTodate+" "+toTime1;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, Apis.ABSENTS_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

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
                            Toast.makeText(AbsentsEditActivity.this,responseReson,Toast.LENGTH_LONG).show();
                        }
                        else {

                            Intent intent =new Intent(AbsentsEditActivity.this,AbsentsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("from_date", FROMDATE);
                params.put("to_date", TODATE);
                params.put("doctor_redhealId",doctorID);
                params.put("id", id);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(putRequest);
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
}
