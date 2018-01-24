package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Items.ActivityItem;
import com.medoske.www.redheal_d.MainActivity;
import com.medoske.www.redheal_d.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 3/25/2017.
 */
public class ActivityListAdapter extends ArrayAdapter<ActivityItem> {

    private Context context;

    public ActivityListAdapter(Context context, int resource, List<ActivityItem> books) {
        super(context, resource, books);
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.activity_item, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final ActivityItem book = getItem(position);

        holder.txtTime.setText(book.getBookingTime());
        holder.txtName.setText(book.getName());
        holder.txtClinicName.setText(book.getClinicName()+"\n"+"RefNo: "+book.getAppointmentRefNo());
        holder.txtStatus.setText(book.getStatus());
        holder.txtPaid.setText(book.getPaymentMode());
        final ViewHolder finalHolder = holder;
        holder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalHolder.txtStatus.setSelected(true);
                finalHolder.txtStatus.setTextColor(Color.BLUE);
               // finalHolder.txtStatus.setBackgroundColor(Color.BLUE);

                // ListView Clicked item value
                ActivityItem userInfo = getItem(position);

                //convert product => json array
                JSONArray jProducts = new JSONArray();
                JSONObject jProduct = new JSONObject();
                String status = "complete";

                try {

                    jProduct.put("bookingId",userInfo.getBookingId());
                    jProduct.put("bookingTime",userInfo.getBookingTime());
                    jProduct.put("bookingDate",userInfo.getBookingDate());
                    jProduct.put("clinic_redhealId",userInfo.getClinic_redhealId());
                    jProduct.put("patient_redhealId",userInfo.getPatient_redhealId());
                    jProduct.put("doctor_redhealId",userInfo.getDoctor_redhealId());
                    Log.e("bookingId",""+userInfo.getBookingId());
                    jProduct.put("status",status);

                    //add to json array
                    jProducts.put(jProduct);
                    Log.d("json api", "Json array converted from Product: " + jProducts.toString());

                    String jsonData = jProduct.toString();
                    Log.e("jsonData",""+jsonData);
                    new DoUpdateAppointment().execute(jsonData);

                    getContext().startActivity(new Intent(getContext(), MainActivity.class));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

               return ;
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        public TextView txtTime;
        public TextView txtName;
        public TextView txtStatus;
        public TextView txtClinicName;
        public TextView txtPaid;

        public ViewHolder(View v) {
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtStatus = (TextView) v.findViewById(R.id.txtStatus);
            txtClinicName = (TextView) v.findViewById(R.id.txtClinicName);
            txtPaid = (TextView) v.findViewById(R.id.paidText);

        }
    }


    // update status
    class DoUpdateAppointment extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                java.net.URL url = new URL(Apis.DAILY_ACTIVITY_UPDATE_URL);

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

                is.close();


//                osw.flush();
                osw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


}