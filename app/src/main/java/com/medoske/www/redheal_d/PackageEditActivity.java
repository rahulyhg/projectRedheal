package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.AwardsAdapter;
import com.medoske.www.redheal_d.Controllers.FileUtils;
import com.medoske.www.redheal_d.Controllers.Utils;
import com.medoske.www.redheal_d.Helpers.ServiceHandler;
import com.medoske.www.redheal_d.Helpers.Utility;
import com.medoske.www.redheal_d.Items.AwardItem;
import com.medoske.www.redheal_d.Items.ClinicListItem;
import com.medoske.www.redheal_d.Items.PackageEditItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.medoske.www.redheal_d.Controllers.FileUtils.getPath;

public class PackageEditActivity extends AppCompatActivity implements View.OnClickListener {
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static View view;
    //UI References
    private EditText fromDateEtxt,toDateEtxt,etPackageName,etActualPrice,etDiscountPrice,etDiscription;
    private Button submit;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private static final int SELECT_PDF = 2;
    String selectedImagePath;
    Uri selectedImageUri;
    String pathfile;
    ImageView imageView;
    String packageName, actualPrice, discountPrice,discription,from,to,doctorRedhealId,specilizationId,packageId,clinicId,period,sittings,responseStatus;
    String txtPackageName,txtActualPrice,txtDiscountPrice,txtFrom,txtTo,txtDescription,imageUrl;
    Spinner SpClinicName,SpSittings,SpPeriod;
    int clinicCode;
    ArrayList<ClinicListItem> appointItems=new ArrayList<ClinicListItem>();
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Package");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");
        specilizationId=sp.getString("specializationId","1");
        Log.e("specializationId",""+specilizationId);

        URL =Apis.CLINIC_SPINNER_URL+doctorRedhealId;

        packageId = sp.getString("packageId","1");
        txtPackageName = sp.getString("packageName","1");
        txtActualPrice = sp.getString("actualPrice","1");
        txtDiscountPrice = sp.getString("discountPrice","1");
        txtFrom = sp.getString("fromTime","1");
        txtTo = sp.getString("toTime","1");
        txtDescription = sp.getString("description","1");
        imageUrl =sp.getString("imageUrl","1");

        /*packageId = getIntent().getExtras().getString("packageId","defaultKey");

        txtPackageName = getIntent().getExtras().getString("packageName","defaultKey");
        txtActualPrice = getIntent().getExtras().getString("actualPrice","defaultKey");
        txtDiscountPrice = getIntent().getExtras().getString("discountPrice","defaultKey");
        txtFrom = getIntent().getExtras().getString("fromTime","defaultKey");
        txtTo = getIntent().getExtras().getString("toTime","defaultKey");
        txtDescription = getIntent().getExtras().getString("description","defaultKey");*/



        etPackageName= (EditText) findViewById(R.id.etPackageName);
        etPackageName.setText(txtPackageName);

        etActualPrice= (EditText) findViewById(R.id.etActualPrice);
        etActualPrice.setText(txtActualPrice);

        etDiscountPrice= (EditText) findViewById(R.id.etDiscountPrice);
        etDiscountPrice.setText(txtDiscountPrice);

        etDiscription= (EditText) findViewById(R.id.etDescription);
        etDiscription.setText(txtDescription);

        SpClinicName=(Spinner)findViewById(R.id.spinner_clinicName);

        SpSittings=(Spinner)findViewById(R.id.spinner_Settings) ;

        // Spinner Drop down elements
        List<String> sittingsArray = new ArrayList<String>();
        sittingsArray.add("1");
        sittingsArray.add("2");
        sittingsArray.add("3");
        sittingsArray.add("4");
        sittingsArray.add("5");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sittingsArray);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpSittings.setAdapter(dataAdapter);
        SpSittings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sittings=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SpPeriod=(Spinner)findViewById(R.id.spinner_period) ;
        // Spinner Drop down elements
        List<String> periodArray = new ArrayList<String>();
        periodArray.add("1 month");
        periodArray.add("3 month");
        periodArray.add("6 month");
        periodArray.add("12 month");
        periodArray.add("18 month");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodArray);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpPeriod.setAdapter(dataAdapter1);
        SpPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                period=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imageView =(ImageView)findViewById(R.id.imageView_package);
        //Loading Image from URL
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.tipsbackground)   // optional
                .into(imageView);


        submit=(Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        loginLayout=(LinearLayout)findViewById(R.id.validationLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(PackageEditActivity.this,
                R.anim.shake);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();
        setDateTimeField();


        imageView =(ImageView)findViewById(R.id.imageView_package);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(PackageEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(PackageEditActivity.this);

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



    private class GetSubjectsFa extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected Void doInBackground(Void...params){
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("clinicsByDoctorId");
                        Log.e("categories",""+categories);

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            ClinicListItem cat = new ClinicListItem(catObj.getString("clinic_redhealId"),catObj.getString("clinicName"));
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
            ClinicListItem currCategory = appointItems.get(i);
            lables.add(currCategory.getClinicName());

        }

        lables.add("other");


        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpClinicName.setAdapter(spinnerAdapter);

        SpClinicName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // String catgeoryitem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etFrom);
        fromDateEtxt.setText(txtFrom);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etTo);
        toDateEtxt.setText(txtTo);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt.requestFocus();
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.edit_menu_button) {
            startActivity(new Intent(PackageDetailsActiviy.this,PackageEditActivity.class));
        }*/

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(new Intent(PackageEditActivity.this,PackageDetailsActiviy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    class DoUpdateProfile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                URL url = new URL(Apis.PACKAGES_URL);
                Log.e("update_package",""+url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonData);
                osw.flush();
                osw.close();


                InputStream is = connection.getInputStream();
                String result = "";
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;


                }
                Log.d("json_api", "DoUpdateProduct.doInBackground Json return: " + result);

                final JSONObject response = new JSONObject(result);

                responseStatus = response.getString("status");
                Log.e("responseStatus",""+responseStatus);

                /*runOnUiThread(new Runnable() {
                    public void run() {
                        if (responseStatus.equals("failed")){
                        Toast.makeText(PackageEditActivity.this, "package name already existed", Toast.LENGTH_LONG).show();
                    }else if(responseStatus.equals("success")){
                            Toast.makeText(PackageEditActivity.this, "package ", Toast.LENGTH_LONG).show();
                        }
                    }
                });*/



                is.close();


//                osw.flush();
                osw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        packageName = etPackageName.getText().toString();
        Log.e("packageName",""+packageName);
        actualPrice = etActualPrice.getText().toString();
        discountPrice = etDiscountPrice.getText().toString();
        discription = etDiscription.getText().toString();
        from=fromDateEtxt.getText().toString().trim();
        Log.e("from",""+from);
        to=toDateEtxt.getText().toString().trim();

        clinicCode=SpClinicName.getSelectedItemPosition();
        for (int i = 0; i < appointItems.size(); i++) {
            if (i == clinicCode) {
                clinicId = appointItems.get(i).getClinic_redhealId();
            }
        }

        period=SpPeriod.getSelectedItem().toString();
        sittings=SpSittings.getSelectedItem().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        // Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (packageName.equals("") || actualPrice.equals("") || discountPrice.equals("") || from.equals("") || to.equals("") || discription.equals("")) {
            loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(PackageEditActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }

        else{

            //init new Product object
            PackageEditItem product = new PackageEditItem(packageId, packageName,actualPrice,discountPrice,from,to,discription,specilizationId,doctorRedhealId,clinicId,sittings,period);

            //convert product => json array
            JSONArray jProducts = new JSONArray();
            JSONObject jProduct = new JSONObject();

            try {

                jProduct.put("packageId",product.getPackageId());
                Log.e("packageId",""+product.getPackageId());
                jProduct.put("packageName",product.getPackageName());
                jProduct.put("actualPrice",product.getActualPrice());
                jProduct.put("discountPrice",product.getDiscountPrice());
                jProduct.put("fromTime",product.getFrom());
                jProduct.put("toTime",product.getTo());
                jProduct.put("description",product.getDiscription());
                jProduct.put("specializationId",product.getSpecializationId());
                jProduct.put("doctor_redhealId",product.getDoctorRedhealId());
                jProduct.put("clinic_redhealId",product.getClinicId());
                jProduct.put("period",product.getPeriod());
                jProduct.put("sittings",product.getSittings());


                //add to json array
                jProducts.put(jProduct);
                Log.d("json api", "Json array converted from Product: " + jProducts.toString());

                String jsonData = jProduct.toString();
                Log.e("jsonData",""+jsonData);

                new DoUpdateProfile().execute(jsonData);

                startActivity(new Intent(PackageEditActivity.this,MyPackagesActivity.class));


               /* runOnUiThread(new Runnable() {
                    public void run() {
                        if (responseStatus.equals("failed")){
                            Toast.makeText(PackageEditActivity.this,"package name already existed",Toast.LENGTH_LONG).show();

                        }else {


                            startActivity(new Intent(PackageEditActivity.this,MyPackagesActivity.class));
                        }
                    }
                });*/




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }







}
