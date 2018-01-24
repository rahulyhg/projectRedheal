package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.Items.MyPackageItem;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 19.4.17.
 */
public class MyPackagesAdapter extends ArrayAdapter<MyPackageItem> {

    private Activity activity;
    public List<MyPackageItem> parkingList;
    ArrayList<MyPackageItem> arraylist;
    MyPackageItem book;


    // constructor
    public MyPackagesAdapter(Activity activity, int resource, List<MyPackageItem> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.parkingList = books;
        arraylist = new ArrayList<MyPackageItem>();
        arraylist.addAll(parkingList);
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public MyPackageItem getItem(int position) {
        return parkingList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // result for item get position
        final MyPackageItem productItem=parkingList.get(position);

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {

            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.package_list_item, parent, false);
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

        holder.packageName.setText(productItem.getPackageName());
        Log.e("packageName",""+productItem.getPackageName());
        holder.packageName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Regular.ttf"));

        holder.actulPrice.setText(productItem.getActualPrice()+" Rs/-");
        holder.actulPrice.setPaintFlags(holder.actulPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.actulPrice.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.discountPrice.setText(productItem.getDiscountPrice()+" Rs/-");
        holder.discountPrice.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.fromTime.setText(productItem.getFromTime());
        holder.fromTime.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        holder.toTime.setText(productItem.getToTime());
        holder.toTime.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Roboto-Light.ttf"));

        Picasso.with(activity).load(productItem.getImage()).into(holder.imageView);
        Log.e("getImagePath",""+productItem.getImage());
        return convertView;
    }


    // View Holder
    private class ViewHolder {
        public TextView packageName;
        public TextView actulPrice;
        public TextView discountPrice;
        public TextView fromTime;
        public TextView toTime;
        public ImageView imageView;

        public ViewHolder(View v) {

            packageName=(TextView)v.findViewById(R.id.txtPackageName);
            actulPrice=(TextView)v.findViewById(R.id.txtActualPrice);
            discountPrice=(TextView)v.findViewById(R.id.txtDiscountPrice);
            fromTime=(TextView)v.findViewById(R.id.txtFromTime);
            toTime=(TextView)v.findViewById(R.id.txtToTime);
            imageView=(ImageView)v.findViewById(R.id.image_package);


        }
    }
}