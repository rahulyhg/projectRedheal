package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medoske.www.redheal_d.Items.MoneyTrackerItem;
import com.medoske.www.redheal_d.Items.MyPackageItem;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 20.7.17.
 */

public class MoneyTrackerListAdapter extends ArrayAdapter<MoneyTrackerItem> {

    private Activity activity;
    public List<MoneyTrackerItem> parkingList;
    ArrayList<MoneyTrackerItem> arraylist;
    MoneyTrackerItem book;


    // constructor
    public MoneyTrackerListAdapter(Activity activity, int resource, List<MoneyTrackerItem> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.parkingList = books;
        arraylist = new ArrayList<MoneyTrackerItem>();
        arraylist.addAll(parkingList);
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public MoneyTrackerItem getItem(int position) {
        return parkingList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // result for item get position
        final MoneyTrackerItem productItem=parkingList.get(position);

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {

            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.money_tracker_list_item, parent, false);
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
        Log.e("packageName",""+productItem.getClinicName());
        holder.clinicName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        holder.totalAmount.setText("Total Amount : "+productItem.getAmount()+" Rs/-");
        //holder.actulPrice.setPaintFlags(holder.actulPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.totalAmount.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.online.setText(productItem.getOnline());
        holder.online.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.offline.setText(productItem.getOffline());
        holder.offline.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.patients.setText(productItem.getPatients());
        holder.patients.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));


        return convertView;
    }


    // View Holder
    private class ViewHolder {
        public TextView clinicName;
        public TextView totalAmount;
        public TextView online;
        public TextView offline;
        public TextView patients;

        public ViewHolder(View v) {

            clinicName=(TextView)v.findViewById(R.id.txtClinicName);
            totalAmount=(TextView)v.findViewById(R.id.txtTotal);
            online=(TextView)v.findViewById(R.id.txtOnline);
            offline=(TextView)v.findViewById(R.id.txtOffline);
            patients=(TextView)v.findViewById(R.id.textViewPatients);



        }
    }
}