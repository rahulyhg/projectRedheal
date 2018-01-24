package com.medoske.www.redheal_d;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.PackageLeadsAdapter;
import com.medoske.www.redheal_d.Items.PackageLeadsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPackagesLeadsActivity extends AppCompatActivity {
    //Creating a List of superheroes
    private List<PackageLeadsItem> listSuperHeroes;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String LIST_URL ;
    String doctorRedhealId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages_leads);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Package Leads");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId", "1");


        LIST_URL= Apis.PACKAGE_LEADS_URL+doctorRedhealId;

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();

        //Calling method to get data
        getData();
    }
    private void getData() {


        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                LIST_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //Dismissing progress dialog
                loading.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("packageleads_list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String packageLeadId = jsonObject.getString("packageLeadId");
                        String patient_redhealId = jsonObject.getString("patient_redhealId");
                        String patientName = jsonObject.getString("patientName");
                        String doctor_redhealId = jsonObject.getString("doctor_redhealId");
                        String clinic_redhealId = jsonObject.getString("clinic_redhealId");
                        String clinicName = jsonObject.getString("clinicName");
                        String packageId = jsonObject.getString("packageId");
                        String packageName = jsonObject.getString("packageName");
                        String bookingDateTime = jsonObject.getString("bookingDateTime");
                        String packageRefNo = jsonObject.getString("packageRefNo");
                        String paymentMode = jsonObject.getString("paymentMode");
                        String status = jsonObject.getString("status");
                        String actualPrice = jsonObject.getString("actualPrice");
                        String discountPrice = jsonObject.getString("discountPrice");
                        String sittingNumber = jsonObject.getString("sittingNumber");
                        String sordId = jsonObject.getString("sordId");
                        listSuperHeroes.add(new PackageLeadsItem(packageLeadId,patient_redhealId,patientName,doctor_redhealId,clinic_redhealId,clinicName,packageId,packageName,bookingDateTime,packageRefNo,paymentMode,status,actualPrice,discountPrice,sittingNumber,sordId));
                    }

                    //Finally initializing our adapter
                    adapter = new PackageLeadsAdapter(listSuperHeroes, MyPackagesLeadsActivity.this);

                    //Adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
