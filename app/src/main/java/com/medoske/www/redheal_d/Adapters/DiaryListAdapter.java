package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.medoske.www.redheal_d.Items.DiaryItem;
import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 7.4.17.
 */
public class DiaryListAdapter extends ArrayAdapter<DiaryItem> {

    private Activity activity;
    public List<DiaryItem> parkingList;
    ArrayList<DiaryItem> arraylist;
    DiaryItem book;


    // constructor
    public DiaryListAdapter(Activity activity, int resource, List<DiaryItem> books) {
        super(activity, resource, books);
        this.activity = activity;
        this.parkingList = books;
        arraylist = new ArrayList<DiaryItem>();
        arraylist.addAll(parkingList);
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public DiaryItem getItem(int position) {
        return parkingList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // result for item get position
        final DiaryItem productItem=parkingList.get(position);

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {

            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.diary_list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


       /* // alternate cell colors in list
        if ( position % 2 == 0)
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#35a583"));
        else
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#2e4270"));*/

          /*  productItem = parkingList.get(position);
             Log.e("book",""+book);*/

        holder.patientId.setText(productItem.getPatientId());
        Log.e("patientId",""+productItem.getPatientId());

        holder.patientName.setText(productItem.getPatientName());
        holder.bookingDate.setText(" | "+productItem.getBookingDate());
        holder.bookingTime.setText(" | "+productItem.getBookingTime()+"(ScheduledTime)");
        holder.status.setText(productItem.getStatus());
        holder.clinicName.setText(productItem.getClinicName());
        holder.paymentMode.setText("Payment Mode : "+productItem.getPaymentMode());
        try {
            holder.colorLayout.setBackgroundColor(Color.parseColor(productItem.getColorCode()));
        }catch (Exception e) {
            e.printStackTrace();
        }



        return convertView;
    }


    // View Holder
    private class ViewHolder {
        public TextView patientId;
        public TextView patientName;
        public TextView bookingDate;
        public TextView bookingTime;
        public TextView status;
        public TextView clinicName;
        public TextView paymentMode;
        public LinearLayout colorLayout;

        public ViewHolder(View v) {

            patientId=(TextView)v.findViewById(R.id.textRhid);
            patientName=(TextView)v.findViewById(R.id.textPatientName);
            bookingDate=(TextView)v.findViewById(R.id.textDate);
            bookingTime=(TextView)v.findViewById(R.id.textTime);
            status=(TextView)v.findViewById(R.id.textStatus);
            clinicName=(TextView)v.findViewById(R.id.textClinicName);
            paymentMode=(TextView)v.findViewById(R.id.txtPaymentMode);
            colorLayout=(LinearLayout) v.findViewById(R.id.colorLayout);

        }
    }
}


