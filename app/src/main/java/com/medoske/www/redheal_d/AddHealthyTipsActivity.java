package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Controllers.FileUtils;
import com.medoske.www.redheal_d.Helpers.ServiceHandler;
import com.medoske.www.redheal_d.Helpers.Utility;
import com.medoske.www.redheal_d.Items.AddTipsItem;
import com.medoske.www.redheal_d.Items.CatagoeryItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.medoske.www.redheal_d.Controllers.FileUtils.getPath;

public class AddHealthyTipsActivity extends AppCompatActivity implements View.OnClickListener{
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Toolbar toolbar;
        private static final int SELECT_PDF = 2;
        String selectedImagePath;
        Uri selectedImageUri;
        ImageView imageView;
        String title,description,doctorRedhealId,catagoeryName,catageoryId;
        int categeoryCode;
        EditText etTitle,etDescription,etOthers;
    ArrayList<CatagoeryItem> appointItems=new ArrayList<CatagoeryItem>();
    Spinner catgeorySpinner;
    private int i;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_healthy_tips);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Health Tips");

            // To retrieve value from shared preference in another activity
            SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
            doctorRedhealId = sp.getString("RedhealId","1");

            etTitle=(EditText) findViewById(R.id.etTitle);
//            etSpecialization=(EditText) findViewById(R.id.etSpecilization);
            etDescription=(EditText) findViewById(R.id.etDescription);
            etOthers=(EditText)findViewById(R.id.etOther);
            catgeorySpinner=(Spinner)findViewById(R.id.spinnerCatagoery);


            ((Button)findViewById(R.id.submitButton)).setOnClickListener(this);
            imageView =(ImageView)findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setType("*/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Pdf"), SELECT_PDF);
                    selectImage();
                }
            });


            new GetSubjectsFa().execute();

        }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddHealthyTipsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddHealthyTipsActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);


        }
    }



    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
        Log.e("image",""+imageView);

        selectedImageUri = Uri.fromFile(destination);
        selectedImagePath=getPath(getBaseContext(),selectedImageUri);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
       // selectedImageUri = getImageUri(getApplicationContext(), thumbnail);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageBitmap(bm);
        Log.e("image123",""+imageView);

        selectedImageUri = data.getData();
        Log.e("pathuri",""+selectedImageUri);
        selectedImagePath = FileUtils.getPath(this, selectedImageUri);
        Log.e("path",""+selectedImagePath);
    }


    //   spinner AsyncTask

    private class GetSubjectsFa extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected Void doInBackground(Void...params){
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(Apis.ALL_CATEGEORY_URL, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("tip_categories");
                        Log.e("categories",""+categories);

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            CatagoeryItem cat = new CatagoeryItem(catObj.getString("categoryId"),catObj.getString("categoryName"));
                            appointItems.add(cat);



                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }
        protected void onPostExecute(Void result){
            Log.d("spinner","date");
            populateSpinner();
        }
    }
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < appointItems.size(); i++) {
            CatagoeryItem currCategory = appointItems.get(i);
            lables.add(currCategory.getCategoryName());

        }

        lables.add("other");


        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        catgeorySpinner.setAdapter(spinnerAdapter);

        catgeorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String catgeoryitem = parent.getItemAtPosition(position).toString();

                if (catgeoryitem.equals("other")){

                  etOthers.setVisibility(View.VISIBLE);
                }else {
                    etOthers.setVisibility(View.GONE);
                }

/*
                CatagoeryItem selectedCategory = appointItems.get(position);

               // categeoryId = String.valueOf(parent.getSelectedItemId());

               // categeoryId= String.valueOf(selectedCategory.getCategoryId());

                Toast.makeText(AddHealthyTipsActivity.this,""+categeoryId,Toast.LENGTH_LONG).show()*/;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public String POST(String[] params, AddTipsItem addTipsItem){
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.ADD_TIPS_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("doctor_redhealId", new StringBody(doctorRedhealId));
                entity.addPart("tipName", new StringBody(addTipsItem.getTipName()));
                Log.e("tipName+++",""+addTipsItem.getTipName());
                entity.addPart("categoryId", new StringBody(addTipsItem.getCategoryId()));
                Log.e("categoryId+++",""+addTipsItem.getCategoryId());
                entity.addPart("description", new StringBody(addTipsItem.getDescription()));
                entity.addPart("categoryName", new StringBody(addTipsItem.getCategoryName()));


                File file = new File(selectedImagePath);
                entity.addPart("imagePath", new FileBody(file));
                Log.e("imagePath1234",""+ file);

                httppost.setEntity(entity);
                response = httpclient.execute(httppost);

                Log.e("test", "SC:" + response.getStatusLine().getStatusCode());

                HttpEntity resEntity = response.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                Log.e("test", "Response: " + s);
            } catch (ClientProtocolException e) {


            } catch (IOException e) {
                e.printStackTrace();

            }

            // 9. receive response as inputStream
            inputStream = response.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    @Override
    public void onClick(View v) {

        checkValidation();

    }

    private class Asyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Create a new progress dialog
            progressDialog = new ProgressDialog(AddHealthyTipsActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Uploading ...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(true);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            AddTipsItem addTipsItem = new AddTipsItem();
            addTipsItem.setDoctor_redhealId(doctorRedhealId);
            addTipsItem.setTipName(title);
            addTipsItem.setCategoryId(catageoryId);
            Log.e("idcat",""+catageoryId);
            addTipsItem.setCategoryName(catagoeryName);
            addTipsItem.setDescription(description);

            return POST(params,addTipsItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            progressDialog.hide();
            super.onPostExecute(jsonObject);

            Intent in = new Intent(AddHealthyTipsActivity.this, HealthTipsActivtiy.class);
            Toast.makeText(AddHealthyTipsActivity.this,"Tips Added Successfully",Toast.LENGTH_SHORT).show();
            startActivity(in);

        }
    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        title = etTitle.getText().toString().trim();
        catagoeryName = etOthers.getText().toString().trim();
        description = etDescription.getText().toString().trim();

        categeoryCode = catgeorySpinner.getSelectedItemPosition();
        for (int i = 0; i < appointItems.size(); i++) {
            if (i == categeoryCode) {
                catageoryId = appointItems.get(i).getCategoryId();
            }
        }
        Log.e("cat324",""+categeoryCode);

        // Check for both field is empty or not
        if (title.equals("")  || description.equals("") ) {
           // loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(AddHealthyTipsActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();

        }
        else if (title.isEmpty() || title.length()<=4 ){
          etTitle.setError("Enter Minimum 4 Characters");
          return;
        }

        else if (description.isEmpty() || description.length()<=15){
            etDescription.setError("Enter Minimum 15 Characters ");
        }

        else{
            new Asyncclass().execute();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(AddHealthyTipsActivity.this,HealthTipsActivtiy.class));
    }
}

