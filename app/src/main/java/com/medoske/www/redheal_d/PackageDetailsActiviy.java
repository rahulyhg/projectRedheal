package com.medoske.www.redheal_d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class PackageDetailsActiviy extends AppCompatActivity {
     String packageId,packageName,actualPrice,discountPrice,fromTime,toTime,description,imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details_activiy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Package Details");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        packageId = sp.getString("packageId","1");
        packageName = sp.getString("packageName","1");
        actualPrice = sp.getString("actualPrice","1");
        discountPrice = sp.getString("discountPrice","1");
        fromTime = sp.getString("fromTime","1");
        toTime = sp.getString("toTime","1");
        description = sp.getString("description","1");
        imageUrl =sp.getString("imageUrl","1");


        /* Intent intent=this.getIntent();
        // packageId = intent.getExtras().getString("packageId","1");
        packageId=intent.getStringExtra("packageId");
         packageName = intent.getStringExtra("packageName");
         actualPrice = intent.getStringExtra("actualPrice");
         discountPrice = intent.getStringExtra("discountPrice");
         fromTime = intent.getStringExtra("fromTime");
         toTime = intent.getStringExtra("toTime");
         String imageUrl = intent.getStringExtra("imageUrl");
         description = intent.getStringExtra("description");*/

        final TextView txtPackageName =(TextView)findViewById(R.id.txtPackageTitle);
        txtPackageName.setText(packageName);
        txtPackageName.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Regular.ttf"));

        final TextView txtPrice =(TextView)findViewById(R.id.txtPrice);
        txtPrice.setText("Actual Price : "+actualPrice+"  Discounted Price : "+discountPrice);
        txtPrice.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final TextView txtValidity =(TextView)findViewById(R.id.txtValidity);
        txtValidity.setText("Package Validity : "+fromTime+" to "+toTime);
        txtValidity.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final TextView txtDescription =(TextView)findViewById(R.id.txtDescription);
        txtDescription.setText(description);
        txtDescription.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Roboto-Light.ttf"));

        final ImageView packageImage =(ImageView)findViewById(R.id.packageImage);
        //Loading Image from URL
       // Picasso.with(this).load(imageUrl).placeholder(R.drawable.tipsbackground).into(packageImage);

        try {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.tipsbackground).into(packageImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.package_menu, menu);
            return true;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_menu_button) {
            Intent intent =new Intent(PackageDetailsActiviy.this,PackageEditActivity.class);
            intent.putExtra("packageId",packageId);
            intent.putExtra("packageName",packageName);
            intent.putExtra("actualPrice",actualPrice);
            intent.putExtra("discountPrice",discountPrice);
            intent.putExtra("fromTime",fromTime);
            intent.putExtra("toTime",toTime);
            intent.putExtra("description",description);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("packageId",packageId);// key_name is the name through which you can retrieve it later.
            editor.putString("packageName",packageName);
            editor.putString("actualPrice",actualPrice);
            editor.putString("discountPrice",discountPrice);
            editor.putString("fromTime",fromTime);
            editor.putString("toTime",toTime);
            editor.putString("imageUrl",imageUrl);
            editor.putString("description",description);
            editor.commit();

            startActivity(intent);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(new Intent(PackageDetailsActiviy.this,MyPackagesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

}
