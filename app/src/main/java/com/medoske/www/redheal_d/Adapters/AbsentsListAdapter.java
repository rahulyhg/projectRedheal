package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.AbsentsEditActivity;
import com.medoske.www.redheal_d.Items.AbsentsItem;
import com.medoske.www.redheal_d.Items.ActivityItem;
import com.medoske.www.redheal_d.Items.AppointmentItem;
import com.medoske.www.redheal_d.Items.ServiceItem;
import com.medoske.www.redheal_d.MainActivity;
import com.medoske.www.redheal_d.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1.8.17.
 */

public class AbsentsListAdapter extends ArrayAdapter<AbsentsItem> {

    private Context context;
    AbsentsItem serviceItem;
    List<AbsentsItem> parkingList;

    String DELETE_URL;

    public AbsentsListAdapter(Context context, int resource, List<AbsentsItem> books) {
        super(context, resource, books);
        this.context = context;
        this.parkingList = books;

    }
    @Override
    public int getCount() {
        return parkingList.size();
    }
    @Override
    public AbsentsItem getItem(int pos) {
        return parkingList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.absent_item, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final AbsentsItem book = getItem(position);




        holder.txtToDate.setText("To Date"+"\n"+book.getToDate());
        holder.txtFromDate.setText("From Date"+"\n"+book.getFromDate());
        holder.txtStatus.setText("Status"+"\n"+book.getStatus());

        if (book.getStatus().equals("active")) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#a7a4a4"));
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deleteId =  getItem(position).getId();
                Log.e("delet",""+deleteId);

                AbsentsItem deleteItem =getItem(position);

                DELETE_URL =Apis.ABSENTS_URL+book.getDoctorId()+"/"+deleteItem.getId();

                StringRequest dr = new StringRequest(Request.Method.DELETE, DELETE_URL,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                               // Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                Log.d("RESPONSE ",""+response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error.

                            }
                        }
                );
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(dr);

                parkingList.remove(position);
                notifyDataSetChanged();
            }
        });


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AbsentsItem absentsItem =getItem(position);

                Intent intent =new Intent(getContext(),AbsentsEditActivity.class);
                intent.putExtra("id",absentsItem.getId());
                intent.putExtra("doctorId",absentsItem.getDoctorId());
                intent.putExtra("fromDate",absentsItem.getFromDate());
                intent.putExtra("toDate",absentsItem.getToDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView txtToDate;
        public TextView txtFromDate;
        public TextView txtStatus;
        public LinearLayout relativeLayout;
        public ImageButton imgEdit;
        public ImageButton imgDelete;


        public ViewHolder(View v) {
            txtToDate = (TextView) v.findViewById(R.id.textToDate);
            txtFromDate = (TextView) v.findViewById(R.id.textFromDate);
            txtStatus = (TextView) v.findViewById(R.id.textStatus);
            relativeLayout =(LinearLayout)v.findViewById(R.id.relativeAbsents);
            imgEdit =(ImageButton) v.findViewById(R.id.imageEdit);
            imgDelete =(ImageButton) v.findViewById(R.id.imageDelete);

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

    class DoDeleteAbsent extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //http://dev.superman-academy.com/api.php/10
            String id = params[0];

            Log.e("stringid",""+id);

            try {

                URL url = new URL(DELETE_URL+id);
                Log.e("deleteurl",""+url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("DELETE");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                InputStream is = httpURLConnection.getInputStream();
                int byteCharacter;
                String result = "";
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }
                Log.d("json api", result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
