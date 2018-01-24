package com.medoske.www.redheal_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.ServicesAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.AddProfileFragmentItem;
import com.medoske.www.redheal_d.Items.ServiceItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.List;

public class ServiceEditActivity extends AppCompatActivity {
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    ArrayList<ServiceItem> serviceItems = new ArrayList<ServiceItem>();
    ServicesAdapter serviceArrayAdapter;
    private static String URL_SERVICE;
    String doctorId,doctorName,serviceName;
    ListView serviceList;
    EditText etServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Service");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorId = sp.getString("RedhealId","1");
        doctorName = sp.getString("firstName","1");

        //------------------Services url
        URL_SERVICE= Apis.DOCTORS_SERVICES_URL+doctorId;
        Log.e("service_url",""+URL_SERVICE);

        etServiceName=(EditText)findViewById(R.id.etService);

        final Button button= (Button)findViewById(R.id.ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        // service List view
        serviceList=(ListView)findViewById(R.id.listView_Service);
        new ServiceListData().execute();
    }

    // Check Validation
    private void checkValidation() {
        // Get email id and password
        serviceName = etServiceName.getText().toString().trim();

        // Check for both field is empty or not
        if (serviceName.equals("") ) {
            // loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(ServiceEditActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();

        }else if (serviceName.isEmpty() || serviceName.length()<3){
            etServiceName.setError("Enter Minimum 3 Characters");
            return;
        }

        else{
            new ServiceAsyncclass().execute();
        }


    }

    public class ServiceListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ServiceEditActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL_SERVICE, "GET", param);

            Log.e("urlSuresh", URL_SERVICE);

            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {
                serviceItems.clear();


                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("doctor_service");
                Log.e("jsonArray",""+jsonArray);

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String service = jsonObject.optString("service").toString();
                    Log.e("service",""+service);
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    // String description = jsonObject.optString("description").toString();
                    serviceItems.add(new ServiceItem(service,id,redhealId));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

            if(serviceItems.size()>0)
            {
                serviceArrayAdapter = new ServicesAdapter(ServiceEditActivity.this, R.layout.profile_list_item, serviceItems);
                // studentArrayAdapter.notifyDataSetChanged();
                //list= (ListView) findViewById(R.id.listView);
                serviceList.setItemsCanFocus(false);
                serviceList.setAdapter(serviceArrayAdapter);
                //ListUtils.setDynamicHeight(serviceList);

            }
            else
            {
                // Toast.makeText(ProfileActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }


        }
    }



    public String SERVICE_POST(String[] params, AddProfileFragmentItem addServiceItem){
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.ADD_SERVICES_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("service", new StringBody(addServiceItem.getService()));
                Log.e("service===",""+addServiceItem.getService());
                entity.addPart("redhealId", new StringBody(doctorId));
                Log.e("redhealId===",""+doctorId);

                httppost.setEntity(entity);
                response = httpclient.execute(httppost);

                Log.e("test", "SC:" + response.getStatusLine().getStatusCode());

                HttpEntity resEntity = response.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                Log.e("test", "Response: " + s);
            } catch (ClientProtocolException e) {


            } catch (IOException e) {
                e.printStackTrace();

            }

            // 9. receive response as inputStream
            inputStream = response.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString1(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    // convert Input Stream To String
    private String convertInputStreamToString1(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    //         Asyncclass for add services
    private class ServiceAsyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            pDialog = new ProgressDialog(ServiceEditActivity.this);

            // Set progressdialog message
            pDialog.setMessage("Submitting ...");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);

            // Show progressdialog
//            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            AddProfileFragmentItem addServiceItem = new AddProfileFragmentItem();
            addServiceItem.setService(serviceName);
            addServiceItem.setRedhealId(doctorId);
            Log.e("dododo",""+doctorId);
            // addServiceItem.setDescription(description);

            return SERVICE_POST(params,addServiceItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            pDialog.hide();
            super.onPostExecute(jsonObject);

            /*finish();
            startActivity(getIntent());
            pDialog.dismiss();*/

            Intent in = new Intent(getApplicationContext(), ServiceEditActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Toast.makeText(getContext(),"Submitted Successfully",Toast.LENGTH_SHORT).show();
            startActivity(in);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(ServiceEditActivity.this,ProfileActivity.class));
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
                startActivity(new Intent(ServiceEditActivity.this,ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

}
