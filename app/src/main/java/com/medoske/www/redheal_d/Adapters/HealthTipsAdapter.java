package com.medoske.www.redheal_d.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.medoske.www.redheal_d.Items.TipsItem;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 27-03-2017.
 */
public class HealthTipsAdapter extends ArrayAdapter<TipsItem> {
    private Context context;

    public HealthTipsAdapter(Context context, int resource, List<TipsItem> books) {
        super(context, resource, books);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.tips_list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);


        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final TipsItem book = getItem(position);

        holder.title.setText(book.getTipName());
       // holder.speclization.setText(book.getSpeclization());
        holder.catagoeryName.setText(book.getCategoryName());
       // Picasso.with(this.context).load(book.getImagePath()).into(holder.image);
        //Picasso.with(getContext()).load(book.getImagePath()).placeholder(R.drawable.tipsbackground).into(holder.image);
        Log.e("image",""+book.getImagePath());

        Picasso.with(getContext()).load(book.getImagePath()).placeholder(R.drawable.tipsbackground).into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView title;
        TextView speclization;
        TextView catagoeryName;

        public ViewHolder(View v) {
            image = (ImageView)v.findViewById(R.id.tipImage);
            title = (TextView)v.findViewById(R.id.list_item_title);
           // speclization =(TextView)v.findViewById(R.id.txtSpecialization);
            catagoeryName = (TextView) v.findViewById(R.id.textViewCatagoeryName);

        }
    }

}

