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
import com.medoske.www.redheal_d.Adapters.MembershipAdapter;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.AddProfileFragmentItem;
import com.medoske.www.redheal_d.Items.MembershipItem;

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

public class MembershipEditActivity extends AppCompatActivity {
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    ArrayList<MembershipItem> membershipItems = new ArrayList<MembershipItem>();
    MembershipAdapter membershipArrayAdapter;
    private static String URL_MEMBERSHIP;
    String doctorId, doctorName, membership;
    EditText etMembership;
    ListView membershipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" Edit Membership");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorId = sp.getString("RedhealId", "1");
        doctorName = sp.getString("firstName", "1");

        //------------------ membership url
        URL_MEMBERSHIP = Apis.DOCTORS_MEMBERSHIP_URL + doctorId;
        Log.e("membership_url", "" + URL_MEMBERSHIP);

        etMembership = (EditText) findViewById(R.id.etMembership);

        final Button button = (Button) findViewById(R.id.ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        // membership List view
        membershipList = (ListView) findViewById(R.id.listView_Membership);
        new MembershipListData().execute();

    }


    // Check Validation
    private void checkValidation() {
        // Get email id and password
        membership = etMembership.getText().toString().trim();

        // Check for both field is empty or not
        if (membership.equals("")) {
            // loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(MembershipEditActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();

        } else if (membership.isEmpty() || membership.length() < 3) {
            etMembership.setError("Enter Minimum 3 Characters");

            return;
        }
        else {
            new MembershipAsyncclass().execute();
        }


    }

    public class MembershipListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(MembershipEditActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL_MEMBERSHIP, "GET", param);
            Log.e("url", URL_MEMBERSHIP);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {
                membershipItems.clear();


                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("doctor_membership");

                /*if(membershipItems!=null && membershipItems.size()>0)
                    membershipItems.clear();*/

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String service = jsonObject.optString("membership").toString();
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    //  String description = jsonObject.optString("description").toString();
                    membershipItems.add(new MembershipItem(service,id,redhealId));


                }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (membershipItems.size() > 0) {
                membershipArrayAdapter = new MembershipAdapter(MembershipEditActivity.this, R.layout.profile_list_item, membershipItems);
                //list= (ListView) findViewById(R.id.listView);

                membershipList.setItemsCanFocus(false);
                membershipList.setAdapter(membershipArrayAdapter);
               /* MembershipItem newUser = new MembershipItem(membership);
                membershipArrayAdapter.add(newUser);*/

            } else {
                // Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }


        }
    }


    public String MEMBERSHIP_POST(String[] params, AddProfileFragmentItem addServiceItem) {
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.ADD_MEMBERSHIP_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("membership", new StringBody(addServiceItem.getService()));
                Log.e("membership===", "" + addServiceItem.getService());
                entity.addPart("redhealId", new StringBody(doctorId));
                Log.e("redhealId===", "" + doctorId);
               /* entity.addPart("description", new StringBody(addServiceItem.getDescription()));
                Log.e("description===",""+addServiceItem.getDescription());*/


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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    // convert Input Stream To String
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    //         Asyncclass for membership
    private class MembershipAsyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            pDialog = new ProgressDialog(MembershipEditActivity.this);

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
            addServiceItem.setService(membership);
            addServiceItem.setRedhealId(doctorId);
            Log.e("dododo", "" + doctorId);
            // addServiceItem.setDescription(description);

            return MEMBERSHIP_POST(params, addServiceItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            pDialog.dismiss();
            super.onPostExecute(jsonObject);

            Intent in = new Intent(MembershipEditActivity.this, MembershipEditActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Toast.makeText(getContext(),"Submitted Successfully",Toast.LENGTH_SHORT).show();
            startActivity(in);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(MembershipEditActivity.this, ProfileActivity.class));
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
                startActivity(new Intent(MembershipEditActivity.this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}

