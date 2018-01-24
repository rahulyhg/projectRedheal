package com.medoske.www.redheal_d.MainFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.AboutUsActivity;
import com.medoske.www.redheal_d.Adapters.PackageLeadsAdapter;
import com.medoske.www.redheal_d.CurrentStatusActivity;
import com.medoske.www.redheal_d.Helpers.Session;
import com.medoske.www.redheal_d.Items.PackageLeadsItem;
import com.medoske.www.redheal_d.LoginActivity;
import com.medoske.www.redheal_d.MainActivity;
import com.medoske.www.redheal_d.MyClinicsActivity;
import com.medoske.www.redheal_d.MyPackagesActivity;
import com.medoske.www.redheal_d.MyPackagesLeadsActivity;
import com.medoske.www.redheal_d.ProfileActivity;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    View rootView;
    RelativeLayout profile,logOut;
    String doctorId,doctorName,doctorImage,specializationId;
    TextView txtSignOut,txtTerms,txtAbout,txtContactUs,txtMyPackages,txtPackageLeads,txtCurrentStatus,txtMyclinic,txtAvalibulity;
    LinearLayout myPackages,myPackageLeads;
    private Session session;
    String URL;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_five, container, false);


        session = new Session(getContext());
        if(!session.loggedin()){
            logout();
        }

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorId = sp.getString("RedhealId","1");
        doctorName = sp.getString("firstName","1");
        doctorImage = sp.getString("imagepath","1");
        specializationId = sp.getString("specializationId","1");
        int i=Integer.parseInt(specializationId);

        URL= Apis.CURRENT_STATUS_URL+doctorId;


        txtTerms=(TextView)rootView.findViewById(R.id.textTerms);
        txtTerms.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtAbout=(TextView)rootView.findViewById(R.id.textAbout);
        txtAbout.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myPackageIntent =new Intent(getContext(),AboutUsActivity.class);
                myPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myPackageIntent);
            }
        });

        txtSignOut=(TextView)rootView.findViewById(R.id.textSignOut);
        txtSignOut.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));


        txtContactUs=(TextView)rootView.findViewById(R.id.textContactUs);
        txtContactUs.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        myPackages=(LinearLayout)rootView.findViewById(R.id.myPackagesLayout);
        myPackageLeads=(LinearLayout)rootView.findViewById(R.id.myPackagesLeadsLayout);
        // myPackages.setVisibility(View.INVISIBLE);

        if (i==5 || i==10 || i==21 || i==22 || i==23){
            myPackages.setVisibility(View.VISIBLE);
            myPackageLeads.setVisibility(View.VISIBLE);
        }else{
            myPackages.setVisibility(View.GONE);
            myPackageLeads.setVisibility(View.GONE);
        }

        txtMyPackages=(TextView)rootView.findViewById(R.id.textMypackage);
        txtMyPackages.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtMyPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myPackageIntent =new Intent(getContext(),MyPackagesActivity.class);
                myPackageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myPackageIntent);
            }
        });

        txtPackageLeads =(TextView)rootView.findViewById(R.id.textMypackageLeads);
        txtMyPackages.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtPackageLeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myPackageLeadsIntent =new Intent(getContext(),MyPackagesLeadsActivity.class);
                myPackageLeadsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myPackageLeadsIntent);
            }
        });

        txtCurrentStatus=(TextView)rootView.findViewById(R.id.textCurrentStatus);
        txtCurrentStatus.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtCurrentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statusIntent =new Intent(getContext(),CurrentStatusActivity.class);
                statusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(statusIntent);
            }
        });

        txtMyclinic=(TextView)rootView.findViewById(R.id.textClinic);
        txtMyclinic.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        txtMyclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clinicIntent =new Intent(getContext(),MyClinicsActivity.class);
                clinicIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(clinicIntent);
            }
        });

        txtAvalibulity=(TextView)rootView.findViewById(R.id.txtAvalibulity);




        final TextView rhId =(TextView)rootView.findViewById(R.id.txtRhid);
        rhId.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        rhId.setText(doctorId);

        final TextView Name =(TextView)rootView.findViewById(R.id.txtName);
        Name.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
        Name.setText(doctorName);

        final ImageView imageView=(ImageView)rootView.findViewById(R.id.doctorImage);
        Picasso.with(getContext()).load(doctorImage).placeholder(R.drawable.placeholderimage). into(imageView);

        profile=(RelativeLayout)rootView.findViewById(R.id.profileLayout);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ProfileActivity.class));
            }
        });


        myPackageLeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyPackagesLeadsActivity.class));
            }
        });


        logOut=(RelativeLayout)rootView.findViewById(R.id.logoutLayout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

          getData();

        return rootView;
    }

    private void logout(){
        session.setLoggedin(false);
        getActivity().finish();
        startActivity(new Intent(getContext(),LoginActivity.class));
    }

    private void getData() {


        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Loading Data", "Please wait...",false,false);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //Dismissing progress dialog
                loading.dismiss();
                try {
                    //JSONArray jsonArray = response.getJSONArray("packageleads_list");

                    JSONObject jsonObject =new JSONObject(String.valueOf(response));
                    String status =jsonObject.getString("status");
                    Boolean bool1 = Boolean.parseBoolean(status);
                    String description =jsonObject.getString("description");

                    txtAvalibulity.setText(description);

                   /* if (bool1.equals(false)){
                        Toast.makeText(getContext(),description,Toast.LENGTH_LONG).show();
                        txtAvalibulity.setText(description);
                    }else{
                        Toast.makeText(getContext(),description,Toast.LENGTH_LONG).show();
                    }*/


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }
}
