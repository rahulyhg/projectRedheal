package com.medoske.www.redheal_d.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.medoske.www.redheal_d.Items.PackageLeadsItem;
import com.medoske.www.redheal_d.Items.PackageSittingsItem;
import com.medoske.www.redheal_d.PackageSittingsActivity;
import com.medoske.www.redheal_d.PatientsActivity;
import com.medoske.www.redheal_d.PatientsProfileActivity1;
import com.medoske.www.redheal_d.R;

import java.util.List;

/**
 * Created by USER on 2.8.17.
 */

public class PackageLeadsAdapter extends RecyclerView.Adapter<PackageLeadsAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<PackageLeadsItem> superHeroes;

    public PackageLeadsAdapter(List<PackageLeadsItem> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.packageleads_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        PackageLeadsItem superHero =  superHeroes.get(position);

     /*   imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
*/
        //holder.imageView.setImageUrl(superHero.getImageUrl(), imageLoader);
        holder.txtPatientName.setText(superHero.getPatientName());
        holder.txtPackageName.setText(superHero.getPackageName()+" | "+superHero.getClinicName());
        holder.txtPaymentMode.setText("Payment Mode : "+superHero.getPaymentMode());
        holder.txtPrice.setText("Actual Price : "+superHero.getActualPrice()+"  Discount Price : "+superHero.getDiscountPrice());
        holder.leadsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageLeadsItem packageLeadsItem =superHeroes.get(position);

                Intent i = new Intent(context,PackageSittingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("packageRefNo",packageLeadsItem.getPackageRefNo());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       // public NetworkImageView imageView;
        public TextView txtPatientName;
        public TextView txtPackageName;
        public TextView txtPaymentMode;
        public TextView txtPrice;
        public RelativeLayout leadsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            //imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            txtPatientName = (TextView) itemView.findViewById(R.id.txtPatientName);
            txtPackageName= (TextView) itemView.findViewById(R.id.txtPackageName);
            txtPaymentMode= (TextView) itemView.findViewById(R.id.txtPaymentMode);
            txtPrice= (TextView) itemView.findViewById(R.id.txtPrice);
            leadsLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLeads);

        }
    }
}