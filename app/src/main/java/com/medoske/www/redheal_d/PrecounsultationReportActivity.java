package com.medoske.www.redheal_d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.github.barteksc.pdfviewer.PDFView;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.medoske.www.redheal_d.R.id.pdfView;

public class PrecounsultationReportActivity extends AppCompatActivity {
    PDFView pdfView1;
    String pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precounsultation_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("pre consultation report");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        pdfView = sp.getString("pdfPath","1");

         pdfView1 =(PDFView) findViewById(R.id.pdfView);
          new Retrivedata().execute(pdfView);
    }


    //retrive data for pdf
    class Retrivedata extends AsyncTask<String,Void,InputStream> {

        @Override
        protected InputStream doInBackground(String... String) {
            InputStream inputStream=null;
            try
            {
                URL url =new URL(String[0]);
                HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();


                if (urlConnection.getResponseCode()==200){

                    inputStream =new BufferedInputStream(urlConnection.getInputStream());
                }

            }
            catch (IOException e){

                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView1.fromStream(inputStream).load();
        }
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
super.onBackPressed();
        //startActivity(new Intent(PrecounsultationReportActivity.this, PatientsProfileActivity1.class));
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
                //startActivity(new Intent(PrecounsultationReportActivity.this, PatientsProfileActivity1.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}
