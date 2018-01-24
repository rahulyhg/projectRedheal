package com.medoske.www.redheal_d;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CurrentStatusActivity extends AppCompatActivity {
EditText etFromTime1,etToTime1;
    RadioButton r1,r2;
    String doctorRedhealId;
    Button submitButton;
    String AvailableStatus,fromTime,toTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Current Status");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");




        r1=(RadioButton)findViewById(R.id.radioButton) ;
        r2=(RadioButton)findViewById(R.id.radioButton2) ;
        final LinearLayout linearLayout =(LinearLayout)findViewById(R.id.notAvalibleLayout);
        linearLayout.setVisibility(View.GONE);

        AvailableStatus ="available";
        r1.setChecked(true);
        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                     AvailableStatus ="available";
                     linearLayout.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(),""+AvailableStatus,Toast.LENGTH_LONG).show();

                }
            }
        });

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                 AvailableStatus ="unavailable";
                linearLayout.setVisibility(View.VISIBLE);
                    r2.setChecked(true);
                }
            }
        });

        // etFromTime1 Format

        etFromTime1 =(EditText)findViewById(R.id.editTextTime1);
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

                mTimePicker = new TimePickerDialog(CurrentStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        etToTime1 =(EditText)findViewById(R.id.editTextTime2);
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

                mTimePicker = new TimePickerDialog(CurrentStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
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





        submitButton =(Button)findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentStatus();
            }
        });
    }

    private void CurrentStatus(){

        fromTime = etFromTime1.getText().toString().trim();
        toTime = etToTime1.getText().toString().trim();

       /* if (fromTime.equals("") || toTime.equals("")){

            Toast.makeText(CurrentStatusActivity.this,"Enter All Fields",Toast.LENGTH_LONG).show();
        }*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.CURRENT_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            String error =jsonObject.getString("error");
                            Boolean bool1 = Boolean.parseBoolean(error);
                            String description =jsonObject.getString("description");

                            if (bool1.equals(false)){
                                Toast.makeText(CurrentStatusActivity.this,description,Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(CurrentStatusActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else{
                                Toast.makeText(CurrentStatusActivity.this,description,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                        }
                        Log.e("RESPONSE_ERROR: ",""+volleyError);
                        Toast.makeText(CurrentStatusActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fromtime",fromTime);
                params.put("totime",toTime);
                params.put("doctor_redhealId", doctorRedhealId);
                params.put("availability",AvailableStatus);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                    if(pairs.getValue()==null){
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
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
