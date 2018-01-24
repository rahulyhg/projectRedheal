package com.medoske.www.redheal_d.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.medoske.www.redheal_d.Items.PackageLeadsItem;
import com.medoske.www.redheal_d.Items.PackageSittingsItem;
import com.medoske.www.redheal_d.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 2.8.17.
 */

public class PackageSittingsAdapter extends RecyclerView.Adapter<PackageSittingsAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<PackageSittingsItem> superHeroes;

    public PackageSittingsAdapter(List<PackageSittingsItem> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.packagesittings_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PackageSittingsItem superHero =  superHeroes.get(position);

     /*   imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
*/
        //holder.imageView.setImageUrl(superHero.getImageUrl(), imageLoader);
        holder.txtPatientName.setText(superHero.getPatientName());
        holder.txtPackageName.setText(superHero.getPackageName()+" | "+superHero.getClinicName());
        holder.txtPaymentMode.setText("Payment Mode : "+superHero.getPaymentMode());
        holder.txtPrice.setText("Actual Price : "+superHero.getActualPrice()+"  Discount Price : "+superHero.getDiscountPrice());
        holder.txtSittingNo.setText("Sitting No : "+superHero.getSittingN0());
        holder.txtStatus.setText("Sitting Status : "+superHero.getSittingStatus());

        if (superHero.getSittingStatus().equals("completed")){

            // loading album cover using Glide library
            Glide.with(context).load(R.drawable.ic_action_success).into(holder.imageView);
        }
        else if (superHero.getSittingStatus().equals("pending")){
           // loading album cover using Glide library
            Glide.with(context).load(R.drawable.ic_action_error).into(holder.imageView);
        }
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
        public TextView txtSittingNo;
        public TextView txtStatus;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            //imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            txtPatientName = (TextView) itemView.findViewById(R.id.txtPatientName);
            txtPackageName= (TextView) itemView.findViewById(R.id.txtPackageName);
            txtPaymentMode= (TextView) itemView.findViewById(R.id.txtPaymentMode);
            txtPrice= (TextView) itemView.findViewById(R.id.txtPrice);
            txtSittingNo= (TextView) itemView.findViewById(R.id.txtSittingNo);
            txtStatus= (TextView) itemView.findViewById(R.id.txtStatus);
            imageView =(ImageView)itemView.findViewById(R.id.imageViewStatus);

        }
    }
}