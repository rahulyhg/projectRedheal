package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.EducationEditActivity;
import com.medoske.www.redheal_d.Items.EducationItem;
import com.medoske.www.redheal_d.Items.ReportItem;
import com.medoske.www.redheal_d.PatientsProfileActivity1;
import com.medoske.www.redheal_d.PrecounsultationReportActivity;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 25.5.17.
 */

public class PatientsReportsAdapter extends ArrayAdapter<ReportItem> {

    private Context context;
    ReportItem serviceItem;
    List<ReportItem> parkingList;
    ArrayList<ReportItem> arraylist;

    public PatientsReportsAdapter(Context context, int resource, List<ReportItem> books) {
        super(context, resource, books);
        this.context = context;
        this.parkingList = books;
        arraylist = new ArrayList<>();
        arraylist.addAll(parkingList);

    }



    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.report_list_item, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        serviceItem = getItem(position);

        holder.drName.setText(serviceItem.getDoctorName());
        holder.drName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        holder.drId.setText(serviceItem.getDoctorRedhealId()+" | "+serviceItem.getClinicName());
        holder.drId.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        //Loading Image from URL
       // Picasso.with(getContext()).load(serviceItem.getPrescriptionImage()).placeholder(R.drawable.placeholderimage).into(holder.priscriptionImage);
        //Picasso.with(getContext()).load(serviceItem.getDiagnosticImage()).placeholder(R.drawable.placeholderimage).into(holder.diagnosticImage);

        if (serviceItem.getDiagnosticImage().isEmpty()) {
            holder.diagnosticImage.setImageResource(R.drawable.placeholderimage);
        } else{
            Picasso.with(getContext()).load(serviceItem.getDiagnosticImage()).into(holder.diagnosticImage);
        }

        if (serviceItem.getPrescriptionImage().isEmpty()) {
            holder.priscriptionImage.setImageResource(R.drawable.placeholderimage);
        } else{
            Picasso.with(getContext()).load(serviceItem.getPrescriptionImage()).into(holder.priscriptionImage);
        }

       /* if (serviceItem.getPreconsultationImage().equals("")){

            holder.precounsultationImage.setVisibility(View.INVISIBLE);
        }*/

       Log.e("precounsyksaj",""+serviceItem.getPreconsultationImage());

        if (serviceItem.getPreconsultationImage().equals(Apis.IP_ADDRESS_URL+"callcenter_api/uploads/reports/"+serviceItem.getDoctorRedhealId()+"/"+null)){
            holder.precounsultationImage.setVisibility(View.GONE);
        }else{
            holder.precounsultationImage.setVisibility(View.VISIBLE);
        }

        holder.precounsultationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent1 = new Intent(getContext(), PrecounsultationReportActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences sp = getContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("pdfPath", serviceItem.getPreconsultationImage());// key_name is the name through which you can retrieve it later.
                    editor.commit();

                    getContext().startActivity(intent1);

            }
        });

        return convertView;
    }

    // view holder for list adapter

    private static class ViewHolder {
        public TextView drName;
        public TextView drId;
        public ImageView priscriptionImage;
        public ImageView diagnosticImage;
        public ImageView precounsultationImage;

        public ViewHolder(View v) {
            drName = (TextView) v.findViewById(R.id.txtDoctorName);
            drId = (TextView) v.findViewById(R.id.txtrhid);
            precounsultationImage = (ImageView) v.findViewById(R.id.imgPrecounsultation);
            priscriptionImage = (ImageView) v.findViewById(R.id.imgPriscription);
            diagnosticImage = (ImageView) v.findViewById(R.id.imgdiagnostic);

        }
    }




}


