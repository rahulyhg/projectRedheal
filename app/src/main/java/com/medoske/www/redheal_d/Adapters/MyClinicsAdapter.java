package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.EditClinicActivity;
import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.MyClinicsActivity;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1.4.17.
 */
public class MyClinicsAdapter extends ArrayAdapter<MyClinicItem> {

    private Activity activity;
    public List<MyClinicItem> parkingList;
    ArrayList<MyClinicItem> arraylist;
    MyClinicItem book;


    // constructor
    public MyClinicsAdapter(Activity activity, int resource, List<MyClinicItem> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.parkingList = books;
        arraylist = new ArrayList<MyClinicItem>();
        arraylist.addAll(parkingList);
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public MyClinicItem getItem(int position) {
        return parkingList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // result for item get position
        final MyClinicItem productItem=parkingList.get(position);

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {

            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.clinic_list_item, parent, false);
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

        holder.clinicName.setText(productItem.getClinicName());
        Log.e("clinicName",""+productItem.getClinicName());

        holder.addressTxt.setText(productItem.getAreaName()+" | "+productItem.getCityName());
       // holder.contactNo.setText(productItem.getPrimeryNo()+" "+productItem.getSecondaryNo());
        holder.morningTime.setText(productItem.getMorningTime());
        holder.morningDays.setText(productItem.getMorningDays());
        holder.afternoonTime.setText(productItem.getAfternoonTime());
        holder.afternoonDays.setText(productItem.getAfternoonDays());
        holder.eveningTime.setText(productItem.getEveningTime());
        holder.eveningDays.setText(productItem.getEveningDays());
        Picasso.with(activity).load(productItem.getImagePath()).into(holder.imageView);
        Log.e("getImagePath",""+productItem.getImagePath());
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate menu
                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_clinic, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_edit:

                                Intent intent =new Intent(getContext(),EditClinicActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("clinicId",productItem.getClinicRedhealId());
                                getContext().startActivity(intent);

                               // Toast.makeText(getContext(),"Postion"+position,Toast.LENGTH_LONG).show();
                                return true;

                            case R.id.action_delete:

                                String deleteId =  getItem(position).getClinicRedhealId();
                                Log.e("delet",""+deleteId);

                                new DoDeleteProfile().execute(deleteId);
                                arraylist.remove(position);
                                notifyDataSetChanged();
                                //getContext().startActivity(new Intent(getContext(),MyClinicsActivity.class));
                                //Toast.makeText(getContext(), "delete"+position, Toast.LENGTH_SHORT).show();

                                return true;

                            default:
                        }


                        return false;
                    }
                });
                popup.show();
            }
        });

        return convertView;
    }


    // View Holder
    private class ViewHolder {
        public TextView clinicName;
        public TextView addressTxt;
        public TextView contactNo;
        public TextView morningTime;
        public TextView morningDays;
        public TextView afternoonTime;
        public TextView afternoonDays;
        public TextView eveningTime;
        public TextView eveningDays;
        public ImageView imageView;
        public ImageView overflow;


        public ViewHolder(View v) {

            clinicName=(TextView)v.findViewById(R.id.clinicName);
            clinicName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            addressTxt=(TextView)v.findViewById(R.id.address);
            addressTxt.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

           /* contactNo=(TextView)v.findViewById(R.id.txtMobileno);
            contactNo.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));*/

            morningTime=(TextView)v.findViewById(R.id.time_morning);
            morningTime.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            morningDays=(TextView)v.findViewById(R.id.days_morning);
            morningDays.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            afternoonTime=(TextView)v.findViewById(R.id.time_AfterNoon);
            afternoonTime.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            afternoonDays=(TextView)v.findViewById(R.id.days_afterNoon);
            afternoonDays.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            eveningTime=(TextView)v.findViewById(R.id.time_evening);
            eveningTime.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

            eveningDays=(TextView)v.findViewById(R.id.days_evening);
            eveningDays.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));


            imageView=(ImageView)v.findViewById(R.id.clinic_image);
           // relativeLayout=(RelativeLayout)v.findViewById(R.id.relative);

            overflow = (ImageView) v.findViewById(R.id.overflow);

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

                URL url = new URL(Apis.ADD_CLINIC_URL+"/"+id);
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

