package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Items.EducationItem;
import com.medoske.www.redheal_d.Items.MembershipItem;
import com.medoske.www.redheal_d.Items.ServiceItem;
import com.medoske.www.redheal_d.ProfileActivity;
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
 * Created by USER on 31.3.17.
 */
public class EducationAdapter extends ArrayAdapter<EducationItem> {

    private Context context;
    EducationItem serviceItem;
    List<EducationItem> parkingList;
    ArrayList<EducationItem> arraylist;

    public EducationAdapter(Context context, int resource, List<EducationItem> books) {
        super(context, resource, books);
        this.context = context;
        this.parkingList = books;
        arraylist = new ArrayList<>();
        arraylist.addAll(parkingList);

    }

    /*@Override
    public int getCount() {

        return arraylist.size();
    }

    @Override
    public EducationItem getItem(int position) {
       return arraylist.get(3);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }*/

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.profile_list_item1, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        EducationItem serviceItem = (EducationItem) getItem(position);

        holder.Name.setText(serviceItem.getQualification()+","+serviceItem.getUniversity());
        Log.e("service",""+ serviceItem.getQualification());
        holder.Name.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        final ViewHolder finalHolder = holder;

        return convertView;
    }

    // view holder for list adapter

    private static class ViewHolder {
        public TextView Name;
        public ImageView overflow;

        public ViewHolder(View v) {
            Name = (TextView) v.findViewById(R.id.txtTitle);
            overflow = (ImageView) v.findViewById(R.id.overflow);
        }
    }


    // update profile

    class DoUpdateProfile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                URL url = new URL(Apis.DOCTORS_EDUCATION_URL);

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


    //    delete list item using asynctask

    class DoDeleteProfile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //http://dev.superman-academy.com/api.php/10
            String id = params[0];

            Log.e("stringid",""+id);

            try {

                URL url = new URL(Apis.DOCTORS_EDUCATION_URL+id);
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

