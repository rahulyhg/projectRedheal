package com.medoske.www.redheal_d.MainFragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medoske.www.redheal_d.AppointmentActivity;
import com.medoske.www.redheal_d.DairyActivity;
import com.medoske.www.redheal_d.HealthTipsActivtiy;
import com.medoske.www.redheal_d.MoneyTrackerActivity;
import com.medoske.www.redheal_d.PatientsActivity;
import com.medoske.www.redheal_d.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

LinearLayout appointments,dairy,patients,reportCenter,healthTips,moneyTracker;
    TextView txtAppointment,txtDairy,txtPatients,txtHealth,txtMoneyTracker;
    View rootView;
    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_two, container, false);



        txtAppointment=(TextView)rootView.findViewById(R.id.txtAppointment);
        txtAppointment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Roboto-Regular.ttf"));

        txtDairy=(TextView)rootView.findViewById(R.id.txtDiary);
        txtDairy.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Roboto-Regular.ttf"));

        txtPatients=(TextView)rootView.findViewById(R.id.txtPatients);
        txtPatients.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Roboto-Regular.ttf"));

        txtHealth=(TextView)rootView.findViewById(R.id.txtHealthTips);
        txtHealth.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Roboto-Regular.ttf"));

        txtMoneyTracker=(TextView)rootView.findViewById(R.id.txtMoneyTracker);
        txtMoneyTracker.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Roboto-Regular.ttf"));


        //appointments Layout
        appointments=(LinearLayout)rootView.findViewById(R.id.appointments);
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AppointmentActivity.class));
            }
        });

        // dairy layout
        dairy=(LinearLayout)rootView.findViewById(R.id.dairy);
        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),DairyActivity.class));
            }
        });

        // patients info Layout
        patients=(LinearLayout)rootView.findViewById(R.id.patients);
        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),PatientsActivity.class));
            }
        });

        //reportCenter Layout
        /*reportCenter=(LinearLayout)rootView.findViewById(R.id.reportCenter);
        reportCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ReportCenterActivity.class));
            }
        });*/

        // health tips Layout
        healthTips=(LinearLayout)rootView.findViewById(R.id.healthTips);
        healthTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),HealthTipsActivtiy.class));
            }
        });

        // moneyTracker Layout
        moneyTracker=(LinearLayout)rootView.findViewById(R.id.moneyTracker);
        moneyTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MoneyTrackerActivity.class));
            }
        });

        return  rootView;
    }

}
