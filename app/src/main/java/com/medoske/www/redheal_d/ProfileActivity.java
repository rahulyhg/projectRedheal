package com.medoske.www.redheal_d;

/**
 * Created by USER on 8.4.17.
 */
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.AwardsAdapter1;
import com.medoske.www.redheal_d.Adapters.EducationAdapter;
import com.medoske.www.redheal_d.Adapters.ExperienceAdapter;
import com.medoske.www.redheal_d.Adapters.MembershipAdapter1;
import com.medoske.www.redheal_d.Adapters.ServicesAdapter1;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Items.AddProfileFragmentItem;
import com.medoske.www.redheal_d.Items.AwardItem;
import com.medoske.www.redheal_d.Items.EducationItem;
import com.medoske.www.redheal_d.Items.ExperienceItem;
import com.medoske.www.redheal_d.Items.MembershipItem;
import com.medoske.www.redheal_d.Items.ServiceItem;
import com.squareup.picasso.Picasso;

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

public class ProfileActivity extends AppCompatActivity {


    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();

    ArrayList<ServiceItem> serviceItems = new ArrayList<ServiceItem>();
    ArrayList<MembershipItem> membershipItems = new ArrayList<MembershipItem>();
    ArrayList<AwardItem> awardItems = new ArrayList<AwardItem>();
    ArrayList<EducationItem> educationItems = new ArrayList<EducationItem>(3);
    ArrayList<ExperienceItem> experienceItems = new ArrayList<ExperienceItem>();

    ServicesAdapter1 serviceArrayAdapter;
    MembershipAdapter1 membershipArrayAdapter;
    AwardsAdapter1 awardsArrayAdapter;
    EducationAdapter educationArrayAdapter;
    ExperienceAdapter experienceArrayAdapter;

    private static String URL_SERVICE,URL_MEMBERSHIP,URL_AWARDS,URL_EDUCATION,URL_EXPERIENCE;
    String doctorId,doctorName,serviceName,membership,awards,education,experience;

    ListView serviceList,membershipList,awardsList,educationList,experienceList;
    Context context = ProfileActivity.this;

    String eredu="http://medoske.com/redhealDoctor/index.php/api/doctor/doctoreducation/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout_doctor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" My Profile");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorId = sp.getString("RedhealId","1");
        doctorName = sp.getString("firstName","1");
        String doctorMobile = sp.getString("mobileNumber1","1");
        String doctorEmail = sp.getString("email","1");
        String doctorAddress = sp.getString("address","1");
        String doctorImage = sp.getString("imagepath","1");
        String doctorSpecialization = sp.getString("specialization","1");
        String doctorEducation = sp.getString("education","1");



        final TextView viewMoreService=(TextView)findViewById(R.id.viewMore_Service);
        viewMoreService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ServiceEditActivity.class));
            }
        });

        final TextView viewMoreMembership=(TextView)findViewById(R.id.viewMore_Membership);
        viewMoreMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MembershipEditActivity.class));
            }
        });

        final TextView viewMoreAward=(TextView)findViewById(R.id.viewMore_Awards);
        viewMoreAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,AwardsEditActivity.class));
            }
        });

        final TextView viewMoreEducation=(TextView)findViewById(R.id.viewMore_Education);
        viewMoreEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EducationEditActivity.class));
            }
        });


        final TextView name=(TextView)findViewById(R.id.txtDoctorName);
        name.setText(doctorName);
        // Set TextView text strike through

        name.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        final TextView mobile=(TextView)findViewById(R.id.txtContactNo);
        mobile.setText(doctorMobile);
        mobile.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final TextView email=(TextView)findViewById(R.id.txtEmail);
        email.setText(doctorEmail);
        email.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final TextView address=(TextView)findViewById(R.id.txtAddress);
        address.setText(doctorAddress);
        address.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final TextView specialization=(TextView)findViewById(R.id.txtSpecilization);
        specialization.setText(doctorSpecialization);
        specialization.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

       /* final TextView education=(TextView)findViewById(R.id.txtEducation);
        education.setText(doctorEducation);*/

        final TextView redhealId=(TextView)findViewById(R.id.txtRhid);
        redhealId.setText(doctorId);
        redhealId.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final ImageView imageView=(ImageView)findViewById(R.id.doctorProfileImg);
      //  Picasso.with(getBaseContext()).load(doctorImage).placeholder(R.drawable.placeholderimage).into(imageView);

        try {
            Glide.with(this).load(doctorImage).placeholder(R.drawable.placeholderimage).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //------------------Services url
        /*URL_SERVICE= Apis.DOCTORS_SERVICES_URL+doctorId;
        Log.e("service_url",""+URL_SERVICE);*/

        URL_SERVICE= Apis.DOCTORS_SERVICES_3_POS_URL+doctorId;
        Log.e("service_url",""+URL_SERVICE);

        //------------------ membership url
        URL_MEMBERSHIP= Apis.DOCTORS_MEMBERSHIP_3_POS_URL+doctorId;
        Log.e("membership_url",""+URL_MEMBERSHIP);

        //------------------ awards url
        URL_AWARDS= Apis.DOCTORS_RECOGNIZATION_3_POS_URL+doctorId;
        Log.e("awards_url",""+URL_AWARDS);

        //------------------ education url
        /*URL_EDUCATION= Apis.DOCTORS_EDUCATION_URL+doctorId;
        Log.e("education_url",""+URL_EDUCATION);*/

        URL_EDUCATION= Apis.DOCTORS_EDUCATION_3_POS_URL+doctorId;
        Log.e("education_url",""+URL_EDUCATION);

        //------------------ experience url
        URL_EXPERIENCE= Apis.DOCTORS_EXPERIENCE_URL+doctorId;
        Log.e("experience_url",""+URL_EXPERIENCE);

        // service List view
        serviceList=(ListView)findViewById(R.id.listView_Service);
        new ServiceListData().execute();

        // membership List view
        membershipList=(ListView)findViewById(R.id.listView_Membership);
        new MembershipListData().execute();

        // awards List view
        awardsList=(ListView)findViewById(R.id.listView_Awards);
        new AwardsListData().execute();

        // education List view
        educationList=(ListView)findViewById(R.id.listView_Education);
        new EducationListData().execute();

        // experience List view
       // experienceList=(ListView)findViewById(R.id.listView_Experience);
        new ExperienceListData().execute();

        // experience add button
        final ImageButton addExperience = (ImageButton)findViewById(R.id.imageButton_addExperience);
        addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilogueBox_Experience();
            }
        });

    }

   // service list data
    public class ServiceListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
                JSONArray jsonArray = json.optJSONArray("doctor_services");
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
                serviceArrayAdapter = new ServicesAdapter1(ProfileActivity.this, R.layout.profile_list_item1, serviceItems);
               // studentArrayAdapter.notifyDataSetChanged();
                //list= (ListView) findViewById(R.id.listView);
                serviceList.setItemsCanFocus(false);
                serviceList.setAdapter(serviceArrayAdapter);
                ListUtils.setDynamicHeight(serviceList);

            }
            else
            {
               // Toast.makeText(ProfileActivity.this, "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }


        }
    }





    public class MembershipListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
                JSONArray jsonArray = json.optJSONArray("doctor_memberships");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String service = jsonObject.optString("membership").toString();
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    //  String description = jsonObject.optString("description").toString();
                    membershipItems.add(new MembershipItem(service,id,redhealId));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

            if(membershipItems.size()>0)
            {
                membershipArrayAdapter = new MembershipAdapter1(ProfileActivity.this,R.layout.profile_list_item1,membershipItems);
                //list= (ListView) findViewById(R.id.listView);

                membershipList.setItemsCanFocus(false);
                membershipList.setAdapter(membershipArrayAdapter);
                ListUtils.setDynamicHeight(membershipList);
            }
            else
            {
               // Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }


        }
    }




    // awards list data
    public class AwardsListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL_AWARDS, "GET", param);
            Log.e("url", URL_AWARDS);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

                awardItems.clear();
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("doc_awards");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String service = jsonObject.optString("awards_recognization").toString();
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    //String description = jsonObject.optString("description").toString();
                    awardItems.add(new AwardItem(service,id,redhealId));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

            if(awardItems.size()>0)
            {
                awardsArrayAdapter = new AwardsAdapter1(ProfileActivity.this, R.layout.profile_list_item1, awardItems);
                //list= (ListView) findViewById(R.id.listView);
                awardsList.setItemsCanFocus(false);
                awardsList.setAdapter(awardsArrayAdapter);
                ListUtils.setDynamicHeight(awardsList);
            }
            else
            {
               // Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }
        }
    }





    // education list data
    public class EducationListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL_EDUCATION, "GET", param);
            Log.e("url", URL_EDUCATION);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

                educationItems.clear();
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("dr_edu");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String qualification = jsonObject.optString("qualification").toString();
                    String university = jsonObject.optString("university").toString();
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    //String description = jsonObject.optString("description").toString();
                    educationItems.add(new EducationItem(qualification,university,id,redhealId));


                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}



            if( educationItems.size()>0)
            {
                educationArrayAdapter = new EducationAdapter(ProfileActivity.this, R.layout.profile_list_item1, educationItems);
                //list= (ListView) findViewById(R.id.listView);
                educationList.setItemsCanFocus(false);
                educationList.setAdapter(educationArrayAdapter);
                ListUtils.setDynamicHeight(educationList);
            }

            else
            {
                // Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }
        }
    }






    // education list data
    public class ExperienceListData extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(ProfileActivity.this);
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
            JSONObject json = jsonParser.makeHttpRequest(URL_EXPERIENCE, "GET", param);
            Log.e("url", URL_EXPERIENCE);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

               // experienceItems.clear();
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("doctor_experience");

                //Iterate the jsonArray and print the info of JSONObjects
               // for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String experience = jsonObject.optString("experience").toString();
                    String id = jsonObject.optString("id").toString();
                    String redhealId = jsonObject.optString("redhealId").toString();
                    //String description = jsonObject.optString("description").toString();
                   // experienceItems.add(new ExperienceItem(experience,id,redhealId));

                final TextView txtExperience =(TextView)findViewById(R.id.viewMore_Experience);
                txtExperience.setText(experience);

               // }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

           /* if(experienceItems.size()>0)
            {

                experienceArrayAdapter = new ExperienceAdapter(ProfileActivity.this, R.layout.profile_list_item, experienceItems);
                //list= (ListView) findViewById(R.id.listView);
                experienceList.setItemsCanFocus(false);
                experienceList.setAdapter(experienceArrayAdapter);
                experienceList.invalidateViews();
                ListUtils.setDynamicHeight(experienceList);
            }
            else
            {
                // Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//                myAppAdapter.notifyDataSetChanged();
            }*/
        }
    }


    // dilogue box method
    private void dilogueBox_Experience(){

        // custom_dilogue_box dialog1
        final Dialog dialog1 = new Dialog(ProfileActivity.this);
        dialog1.setContentView(R.layout.custom_experience_diloge);
        dialog1.setTitle("Add Experience !");

        // set the custom_dilogue_box dialog components - text, image and button
        Button dialogButton1 = (Button) dialog1.findViewById(R.id.ok_button);
        // if button is clicked, close the custom_dilogue_box dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getContext(), ProfileActivity.class));
                experience = ((EditText) dialog1.findViewById(R.id.etExperience)).getText().toString();
                // description = ((EditText) dialog1.findViewById(R.id.etDescription)).getText().toString();

                if (experience.equals("") ) {
                    // loginLayout.startAnimation(shakeAnimation);
                    Toast.makeText(dialog1.getContext(), "Enter All Fields", Toast.LENGTH_LONG).show();

                }else {

                    new ExperienceAsyncclass().execute();
                    dialog1.dismiss();
                }




            }
        });

        dialog1.show();
    }


    //  dilogue Box Post
    public String Experience_POST(String[] params, AddProfileFragmentItem addServiceItem){
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.ADD_EXPERIENCE_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("experience", new StringBody(addServiceItem.getService()));
                Log.e("experience===",""+addServiceItem.getService());
                entity.addPart("redhealId", new StringBody(doctorId));
                Log.e("redhealId===",""+doctorId);
                /*entity.addPart("description", new StringBody(addServiceItem.getDescription()));
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
            if(inputStream != null)
                result = convertInputStreamToString4(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    // convert Input Stream To String
    private String convertInputStreamToString4(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    //         Asyncclass for Awards
    private class ExperienceAsyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            pDialog = new ProgressDialog(ProfileActivity.this);

            // Set progressdialog message
            pDialog.setMessage("Submitting ...");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);

            // Show progressdialog
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            AddProfileFragmentItem addServiceItem = new AddProfileFragmentItem();
            addServiceItem.setService(experience);
            addServiceItem.setRedhealId(doctorId);
            Log.e("dododo",""+doctorId);
            //addServiceItem.setDescription(description);

            return Experience_POST(params,addServiceItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            pDialog.hide();
            super.onPostExecute(jsonObject);

            finish();
            startActivity(getIntent());
            pDialog.dismiss();

            /*Intent in = new Intent(ProfileActivity.this, ProfileActivity.class);
            //Toast.makeText(getContext(),"Submitted Successfully",Toast.LENGTH_SHORT).show();
            startActivity(in);*/

        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }*/



// ListUtils for 2 or more list views in same activity
    static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

