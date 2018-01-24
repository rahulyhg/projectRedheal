package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.medoske.www.redheal_d.AwardsEditActivity;
import com.medoske.www.redheal_d.Items.AwardItem;
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
public class AwardsAdapter extends ArrayAdapter<AwardItem> {

    private Context context;
    AwardItem serviceItem;
    List<AwardItem> parkingList;
    ArrayList<AwardItem> arraylist;



    public AwardsAdapter(Context context, int resource, List<AwardItem> books) {
        super(context, resource, books);
        this.context = context;
        this.parkingList = books;
        arraylist = new ArrayList<>();
        arraylist.addAll(parkingList);

    }

    @Override
    public int getCount() {
        return arraylist.size();
    }
    @Override
    public AwardItem getItem(int pos) {
        return arraylist.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.profile_list_item, parent, Boolean.parseBoolean(null));
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        serviceItem = getItem(position);

        holder.Name.setText(serviceItem.getAward());
        Log.e("service",""+ serviceItem.getAward());
        holder.Name.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        final ViewHolder finalHolder = holder;

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deleteId =  getItem(position).getAwardId();

                Log.e("delet",""+deleteId);
                new DoDeleteProfile().execute(deleteId);
                arraylist.remove(position);
                notifyDataSetChanged();
               /* getContext().startActivity(new Intent(getContext(),AwardsEditActivity.class));
                Toast.makeText(getContext(), "delete"+position, Toast.LENGTH_SHORT).show();*/

              /*  // inflate menu
                PopupMenu popup = new PopupMenu(parent.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_album, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                           *//* case R.id.action_add_favourite:

                                // custom_dilogue_box dialog1
                                final Dialog dialog1 = new Dialog(getContext());
                                dialog1.setContentView(R.layout.custom_awards_diloge);
                                dialog1.setTitle("Edit Awards & Recognition !");

                                // set the custom_dilogue_box dialog components - text, image and button
                                Button dialogButton1 = (Button) dialog1.findViewById(R.id.ok_button);
                                // if button is clicked, close the custom_dilogue_box dialog
                                dialogButton1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //startActivity(new Intent(getContext(), ProfileActivity.class));
                                        String id = getItem(position).getAwardId();
                                        Log.e("id",""+id);
                                        String award = ((EditText) dialog1.findViewById(R.id.etAwards)).getText().toString();
                                        String redhealId = getItem(position).getAwardRedhealId();
                                       // String description = ((EditText) dialog1.findViewById(R.id.etDescription)).getText().toString();

                                        //init new Product object
                                        AwardItem product = new AwardItem(award, id,redhealId);

                                        //convert product => json array
                                        JSONArray jProducts = new JSONArray();
                                        JSONObject jProduct = new JSONObject();

                                        try {

                                            jProduct.put("id",product.getAwardId());
                                            Log.e("id",""+product.getAwardId());
                                            jProduct.put("redhealId",product.getAwardRedhealId());
                                            jProduct.put("awards_recognization",product.getAward());
                                            Log.e("awards_recognization",""+product.getAward());


                                            //add to json array
                                            jProducts.put(jProduct);
                                            Log.d("json api", "Json array converted from Product: " + jProducts.toString());

                                            String jsonData = jProduct.toString();
                                            Log.e("jsonData",""+jsonData);
                                            new DoUpdateProfile().execute(jsonData);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Intent intent=new Intent(getContext(), ProfileActivity.class);
                                        getContext().startActivity(intent);
                                        dialog1.dismiss();

                                    }
                                });

                                dialog1.show();

                               // Toast.makeText(getContext(), "Edit"+position, Toast.LENGTH_SHORT).show();
                                return true;*//*

                            case R.id.action_play_next:

                                String deleteId =  getItem(position).getAwardId();

                                Log.e("delet",""+deleteId);
                                new DoDeleteProfile().execute(deleteId);
                                getContext().startActivity(new Intent(getContext(),ProfileActivity.class));
                                Toast.makeText(getContext(), "delete"+position, Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                        }

                        return false;
                    }
                });
                popup.show();*/
            }
        });


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
                URL url = new URL(Apis.DOCTORS_RECOGNIZATION_URL);
                Log.e("update_award",""+url);
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


    // delete listitem using asyntask

    class DoDeleteProfile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //http://dev.superman-academy.com/api.php/10
            String id = params[0];

            Log.e("stringid",""+id);

            try {

                URL url = new URL(Apis.DOCTORS_RECOGNIZATION_URL+id);
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
