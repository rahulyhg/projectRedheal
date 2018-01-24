package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Controllers.Utils;
import com.medoske.www.redheal_d.Helpers.Session;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity {

    String paramUsername ;
    String paramPassword ;
    JSONObject jsonObj;
    JSONObject c;
    Button buttonLogin;
    TextView forgotLink;
    String Email, Password;
    EditText email,password;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        session = new Session(this);

        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        // check internet connection

        if(!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();
        else {
            Toast.makeText(LoginActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_login);
        }

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);


        //----------------------forgot link----------------------------
        forgotLink = (TextView) findViewById(R.id.textViewForgotpass);
        forgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilogueBox_Forgot();
            }
        });

        //---------------------login button---------------------------
        buttonLogin = (Button) findViewById(R.id.button_Login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();
                else {
                    Toast.makeText(LoginActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_login);
                }

                Email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                Password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

                // Get the values given in EditText fields
                String givenUsername = email.getText().toString();
                String givenPassword = password.getText().toString();

               // Check patter for email id
                Pattern p = Pattern.compile(Utils.regEx);

                Matcher m = p.matcher(Email);


                if ((!email.getText().toString().equals("")) && (!password.getText().toString().equals(""))) {
                    // Pass those values to connectWithHttpGet() method
                    connectWithHttpGet(givenUsername, givenPassword);
                }
                else if (!m.find()){
                    email.setError("please Enter Valid Email");
                }
                else if ((!email.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else if ((!password.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }


    //-----get the login values
    private void connectWithHttpGet(final String givenUsername, final String givenPassword) {

        // Connect with a server is a time consuming process.
        //Therefore we use AsyncTask to handle it
        // From the three generic types;
        //First type relate with the argument send in execute()
        //Second type relate with onProgressUpdate method which I haven't use in this code
        //Third type relate with the return type of the doInBackground method, which also the input type of the onPostExecute method
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //Create a new progress dialog
                progressDialog = new ProgressDialog(LoginActivity.this);
                //Set the progress dialog to display a horizontal progress bar
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Set the dialog title to 'Loading...'
                // progressDialog.setTitle("Chargement...");
                //Set the dialog message to 'Loading application View, please wait...'
                progressDialog.setMessage("Please Wait ...");

                //This dialog can't be canceled by pressing the back key
                progressDialog.setCancelable(true);
                //This dialog isn't indeterminate
                progressDialog.setIndeterminate(false);
                progressDialog.show();

            }
            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings as the argument
                //We need to specifically get the givenUsername and givenPassword
                paramUsername = params[0];
                paramPassword = params[1];
                System.out.println("paramUsername" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL
                HttpGet httpGet = new HttpGet(Apis.LOGIN_URL + paramUsername + "/" + paramPassword);
                //HttpGet httpGet = new HttpGet(LOGIN_URL);
                Log.e("httpGet12345",""+httpGet);


                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    // getContent() ; creates a new InputStream object of the entity.
                    // Now we need a readable source to read the byte stream that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity().getContent();

                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
                    //The buffer size is 8K
                    //Therefore we need a mechanism to append the separately coming chunks in to one String element
                    // We have to use a class that can handle modifiable sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go through each and every chunk of characters
                    //and assign a each chunk to bufferedStrChunk String variable
                    //and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    //We return that value then the onPostExecute() can handle the content
                    System.out.println("Returninge of doInBackground :" + stringBuilder.toString());

                    // If the Username and Password match, it will return "working" as response
                    // If the Username or Password wrong, it will return "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exceptionrates caz of httpResponse :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Secondption generates caz of httpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of the doInBackground() and
            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                Log.e("result123", "" + result);

                try {
                    jsonObj = new JSONObject(result);
                    ;
                    Boolean loginStatus =jsonObj.getBoolean("login_status");
                    JSONArray loginarray = jsonObj.getJSONArray("doctordata");


                    Log.e("loginarray",""+loginarray);

                    String resUname = "";
                    String resPassword = "";
                    String resName = "";
                    String resPrimeryMobile = "";
                    String resSecondaryMobile = "";
                    String resRedhealId = "";
                    String address = "";
                    String specialization = "";
                    String education = "";
                    String imagePath = "";
                    String status = "";
                    String specializationId = "";
                    // looping through All Contacts
                    //for (int i = 0; i < contacts.length(); i++) {
                    try {
                        c = loginarray.getJSONObject(0);
                        resUname = c.getString("email");
                        resPassword = c.getString("password");
                        resName=c.getString("fullName");
                        resPrimeryMobile=c.getString("primaryNumber");
                        resSecondaryMobile=c.getString("secondaryNumber");
                        resRedhealId=c.getString("doctor_redhealId");
                        address=c.getString("address");
                        specialization=c.getString("specialization");
                       // education=c.getString("education");
                        specializationId=c.getString("specializationId");
                       // status= c.getString("status");
                        Log.e("status",""+status);

                       /* // String to boolean conversion
                        statusBoolean = Boolean.parseBoolean(status);
                        Log.e("statusBoolean","boolean value of String str is"+statusBoolean);*/

                        imagePath=Apis.DOCTOR_IMAGE_BASEURL+c.getString("imagePath");

                        Log.e("c123455",""+resUname);
                        Log.e("c1234ddf",""+imagePath);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*if (resUname.equals(paramUsername)&& resPassword.equals(paramPassword)) {*/


                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data

                        if (loginStatus.equals(true)){
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("firstName",resName);// key_name is the name through which you can retrieve it later.
                        editor.putString("mobileNumber1",resPrimeryMobile);
                        editor.putString("mobileNumber2",resSecondaryMobile);
                        editor.putString("email",resUname);
                        editor.putString("RedhealId",resRedhealId);
                        editor.putString("address",address);
                        editor.putString("specialization",specialization);
                       // editor.putString("education",education);
                        editor.putString("imagepath",imagePath);
                       // editor.putString("status",status);
                        editor.putString("specializationId",specializationId);
                        editor.putBoolean("isLoggedIn", true );
                        editor.commit();

                        session.setLoggedin(true);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Success..", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email/Password", Toast.LENGTH_LONG).show();
                        //Toast.makeText((Context) object.get("msg"),"",Toast.LENGTH_SHORT).show();
                    }
                    // }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(givenUsername, givenPassword);

    }


    // dilogue box method for services
    private void dilogueBox_Forgot(){

        // custom_dilogue_box dialog1
        final Dialog dialog1 = new Dialog(LoginActivity.this);
        dialog1.setContentView(R.layout.custom_dilog_forgot);
        dialog1.setTitle("Forgot Password ?");

        // set the custom_dilogue_box dialog components - text, image and button
        Button dialogButton1 = (Button) dialog1.findViewById(R.id.ok_button);
        // if button is clicked, close the custom_dilogue_box dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getContext(), ProfileActivity.class));
               // serviceName = ((EditText) dialog1.findViewById(R.id.etService)).getText().toString();
                // description = ((EditText) dialog1.findViewById(R.id.etDescription)).getText().toString();

                dialog1.dismiss();

            }
        });

        dialog1.show();
    }

}
