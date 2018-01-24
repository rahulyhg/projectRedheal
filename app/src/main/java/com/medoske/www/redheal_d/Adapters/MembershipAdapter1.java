package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Items.MembershipItem;
import com.medoske.www.redheal_d.Items.ServiceItem;
import com.medoske.www.redheal_d.ProfileActivity;
import com.medoske.www.redheal_d.R;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 20.5.17.
 */

public class MembershipAdapter1 extends  ArrayAdapter<MembershipItem> {

private Context context;
    MembershipItem serviceItem;
        List<MembershipItem> parkingList;
        ArrayList<MembershipItem> arraylist;



public MembershipAdapter1(Context context, int resource, List<MembershipItem> books) {
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
        convertView = inflater.inflate(R.layout.profile_list_item1, parent, Boolean.parseBoolean(null));
        // get all UI view
        holder = new ViewHolder(convertView);
        // set tag for holder
        convertView.setTag(holder);


        } else {
        // if holder created, get tag from view
        holder = (ViewHolder) convertView.getTag();
        }

        serviceItem = getItem(position);

        holder.Name.setText(serviceItem.getMembership());
        Log.e("service",""+ serviceItem.getMembership());
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
}}



