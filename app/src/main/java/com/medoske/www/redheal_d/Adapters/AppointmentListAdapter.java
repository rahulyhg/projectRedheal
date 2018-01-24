package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.AppointmentActivity;
import com.medoske.www.redheal_d.Items.AppointmentItem;
import com.medoske.www.redheal_d.R;
import com.medoske.www.redheal_d.ReasonActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by USER on 3/23/2017.
 */

public class AppointmentListAdapter extends ArrayAdapter<AppointmentItem> {
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;

    private Activity activity;
    AppointmentItem appointmentItem;
    public List<AppointmentItem> parkingList;
    ArrayList<AppointmentItem> arraylist;

    ViewHolder holder;

    JSONObject response;
    String responseData;



    class DoUpdateAppointment extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                URL url = new URL(Apis.APPOINTMENT_UPDATE_URL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonData);
                osw.flush();
                osw.close();


                InputStream is = connection.getInputStream();
                String result = "";
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;


                }
                Log.d("json_api", "DoUpdateProduct.doInBackground Json return: " + result);

                response = new JSONObject(result);
                Log.e("result",""+response.getString("status"));

                 responseData = response.getString("status");
                Log.e("result",""+responseData);

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(activity, response.getString("status"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                is.close();


//                osw.flush();
                osw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    // constructor
    public AppointmentListAdapter(Activity activity, int resource, List<AppointmentItem> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.parkingList = books;
        arraylist = new ArrayList<AppointmentItem>();
        arraylist.addAll(parkingList);

    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public AppointmentItem getItem(int position) {
        return parkingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // result for item get position
        appointmentItem = parkingList.get(position);



        holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {

            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.view_row, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);



        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }



          /*  productItem = parkingList.get(position);
             Log.e("book",""+book);*/

        holder.name.setText(appointmentItem.getPatientName());
        Log.e("name", "" + appointmentItem.getPatientName());

        holder.Date.setText(appointmentItem.getDate()+" "+appointmentItem.getTime()+"\n"+"RefNo: "+appointmentItem.getAppointmentRefNo());
        holder.clinicName.setText(appointmentItem.getClinicName()+" | "+appointmentItem.getPaymentMode());
       // Picasso.with(activity).load(appointmentItem.getImagePath()).placeholder(R.drawable.placeholderimage).into(holder.image);

        if (appointmentItem.getImagePath().isEmpty()) {
            holder.image.setImageResource(R.drawable.placeholderimage);
        } else{
            Picasso.with(activity).load(appointmentItem.getImagePath()).into(holder.image);
        }

        holder.rejectTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AppointmentItem reject = parkingList.get(position);
                    Intent i = new Intent(getContext(),ReasonActivity.class);
                    i.putExtra("appointmentId", reject.getAppointmentId());
                    i.putExtra("time", reject.getTime());
                    i.putExtra("date", reject.getDate());
                    i.putExtra("clinicId", reject.getClinicRhid());
                    i.putExtra("patientId", reject.getPatientId());
                    i.putExtra("doctorId", reject.getDoctorRhid());
                    i.putExtra("appointmentRefernceNo", reject.getAppointmentRefNo());

                    getContext().startActivity(i);



            }
        });

        holder.conformTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init new Product object
                AppointmentItem confirm = parkingList.get(position);

                //convert product => json array
                JSONArray jProducts = new JSONArray();
                JSONObject jProduct = new JSONObject();
                String status = "confirm";
                String colorCode = "#d4e157";
                try {

                    jProduct.put("bookingId",confirm.getAppointmentId());
                    jProduct.put("bookingTime",confirm.getTime());
                    jProduct.put("bookingDate",confirm.getDate());
                    jProduct.put("clinic_redhealId",confirm.getClinicRhid());
                    jProduct.put("patient_redhealId",confirm.getPatientId());
                    jProduct.put("doctor_redhealId",confirm.getDoctorRhid());
                    jProduct.put("status",status);
                    jProduct.put("color",colorCode);
                    jProduct.put("appointmentRefNo",confirm.getAppointmentRefNo());
                    Log.e("appointmentRefNo",""+confirm.getAppointmentRefNo());

                    //add to json array
                    jProducts.put(jProduct);
                    Log.d("json api", "Json array converted from Product: " + jProducts.toString());


                    String jsonData = jProduct.toString();
                    Log.e("jsonData",""+jsonData);
                    new DoUpdateAppointment().execute(jsonData);

                    getContext().startActivity(new Intent(getContext(), AppointmentActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


        return convertView;
    }


    // View Holder
    private class ViewHolder{
        TextView name;
        TextView Date;
        TextView clinicName;
        CircleImageView image;
        TextView conformTxt;
        TextView rejectTxt;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.name);
            name.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            Date = (TextView) v.findViewById(R.id.dateText);
            Date.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));
            //time = (TextView) v.findViewById(R.id.time);
            clinicName = (TextView) v.findViewById(R.id.clinicName);
            clinicName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            image = (CircleImageView) v.findViewById(R.id.imageViewpatient);
            conformTxt = (TextView) v.findViewById(R.id.conformText);
            rejectTxt = (TextView) v.findViewById(R.id.rejectText);



        }





    }




}