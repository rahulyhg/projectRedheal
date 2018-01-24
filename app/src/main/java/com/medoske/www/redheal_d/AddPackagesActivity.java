package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
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
import com.medoske.www.redheal_d.Controllers.FileUtils;
import com.medoske.www.redheal_d.Controllers.Utils;
import com.medoske.www.redheal_d.Helpers.ServiceHandler;
import com.medoske.www.redheal_d.Helpers.Utility;
import com.medoske.www.redheal_d.Items.AddPackageItem;
import com.medoske.www.redheal_d.Items.AddTipsItem;
import com.medoske.www.redheal_d.Items.CatagoeryItem;
import com.medoske.www.redheal_d.Items.ClinicListItem;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.medoske.www.redheal_d.Controllers.FileUtils.getPath;

public class AddPackagesActivity extends AppCompatActivity implements View.OnClickListener {
Toolbar toolbar;
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
    String packageName, actualPrice, discountPrice,discription,from,to,doctorRedhealId,specilizationId,clinicId,period,sittings,responseStatus;
    Spinner SpClinicName,SpSittings,SpPeriod;
    int clinicCode;
    ArrayList<ClinicListItem> appointItems=new ArrayList<ClinicListItem>();

    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_packages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Packages");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");
        specilizationId=sp.getString("specializationId","1");
        Log.e("packageId",""+specilizationId);

        URL =Apis.CLINIC_SPINNER_URL+doctorRedhealId;

        etPackageName= (EditText) findViewById(R.id.etPackageName);
        etActualPrice= (EditText) findViewById(R.id.etActualPrice);
        etDiscountPrice= (EditText) findViewById(R.id.etDiscountPrice);
        etDiscription= (EditText) findViewById(R.id.etDescription);

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


        submit=(Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        loginLayout=(LinearLayout)findViewById(R.id.validationLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(AddPackagesActivity.this,
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

        // spinner response
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPackagesActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddPackagesActivity.this);

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


    public String POST(String[] params, AddPackageItem addPackageItem){
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.PACKAGES_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("doctor_redhealId", new StringBody(doctorRedhealId));
                entity.addPart("specializationId", new StringBody(specilizationId));
                entity.addPart("packageName", new StringBody(addPackageItem.getPackageName()));
                entity.addPart("actualPrice", new StringBody(addPackageItem.getActualPrice()));
                entity.addPart("discountPrice", new StringBody(addPackageItem.getDiscountPrice()));
                entity.addPart("description", new StringBody(addPackageItem.getDiscription()));
                entity.addPart("toTime", new StringBody(addPackageItem.getTo()));
                entity.addPart("fromTime", new StringBody(addPackageItem.getFrom()));
                entity.addPart("period", new StringBody(addPackageItem.getPeriod()));
                entity.addPart("sittings", new StringBody(addPackageItem.getSittings()));
                entity.addPart("clinic_redhealId", new StringBody(addPackageItem.getClinicId()));


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
                JSONObject response1 = new JSONObject(String.valueOf(s));
                Log.e("result",""+response1.getString("status"));

                responseStatus = response1.getString("status");


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


    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etFrom);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etTo);
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
    public void onClick(View v) {

        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }

    }

    private class Asyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Create a new progress dialog
            progressDialog = new ProgressDialog(AddPackagesActivity.this);
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

            AddPackageItem addPackageItem = new AddPackageItem();
            addPackageItem.setPackageName(packageName);
            addPackageItem.setDiscountPrice(discountPrice);
            addPackageItem.setActualPrice(actualPrice);
            addPackageItem.setDiscription(discription);
            addPackageItem.setFrom(from);
            addPackageItem.setTo(to);
            addPackageItem.setDoctorId(doctorRedhealId);
            addPackageItem.setPackageId(specilizationId);
            addPackageItem.setPeriod(period);
            addPackageItem.setSittings(sittings);
            addPackageItem.setClinicId(clinicId);

            return POST(params,addPackageItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            progressDialog.hide();
            super.onPostExecute(jsonObject);

            if (responseStatus.equals("failed")){
                Toast.makeText(AddPackagesActivity.this,"package name already existed! Thank You",Toast.LENGTH_LONG).show();
            }
            else {

                Intent in = new Intent(AddPackagesActivity.this, MyPackagesActivity.class);
                Toast.makeText(AddPackagesActivity.this, "Package Added Successfully", Toast.LENGTH_SHORT).show();
                startActivity(in);

            }

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
            Toast.makeText(AddPackagesActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }

        else{
            new Asyncclass().execute();
        }

    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(AddPackagesActivity.this,MyPackagesActivity.class));
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.package_menu, menu);
        return true;
    }*/
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
                startActivity(new Intent(AddPackagesActivity.this,MyPackagesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

}
