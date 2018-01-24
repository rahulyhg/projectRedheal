package com.medoske.www.redheal_d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Adapters.MyClinicsAdapter;
import com.medoske.www.redheal_d.Controllers.FileUtils;
import com.medoske.www.redheal_d.Helpers.JSONParser;
import com.medoske.www.redheal_d.Helpers.Utility;
import com.medoske.www.redheal_d.Items.EditClinicItem;
import com.medoske.www.redheal_d.Items.MyClinicItem;
import com.medoske.www.redheal_d.Items.PackageEditItem;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.medoske.www.redheal_d.Controllers.FileUtils.getPath;

public class EditClinicActivity extends AppCompatActivity implements View.OnClickListener {
    List<NameValuePair> param = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    private String URL;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private final int REQUEST_CODE_PLACEPICKER = 3;
    int i = 0;
    int j = 0;
    int k = 0;
    int l = 0;
    int m = 0;
    int n = 0;
    int p = 0;

    Button MbuttonSun, MbuttonMon, MbuttonTue, MbuttonWed, MbuttonThu, MbuttonFri, MbuttonSat,
            AbuttonSun, AbuttonMon, AbuttonTue, AbuttonWed, AbuttonThu, AbuttonFri, AbuttonSat,
            EbuttonSun, EbuttonMon, EbuttonTue, EbuttonWed, EbuttonThu, EbuttonFri, EbuttonSat;
    EditText etFromTime1, etToTime1, etFromTime2, etToTime2, etFromTime3, etToTime3 ,etClinicName,etDoorNo,etPrimeryNo,etSecondaryNo,etColony,etPincode,etLandMark,etAddress,etCounsultationFee,etPremiumFee;
    TextView MtxtSunday,MtxtMonday,MtxtTuesday,MtxtWednsday,MtxtThursday,MtxtFriday,MtxtSaturday,
            AtxtSunday,AtxtMonday,AtxtTuesday,AtxtWednsday,AtxtThursday,AtxtFriday,AtxtSaturday,
            EtxtSunday,EtxtMonday,EtxtTuesday,EtxtWednsday,EtxtThursday,EtxtFriday,EtxtSaturday;
    private static final int SELECT_PDF = 2;
    String selectedImagePath;
    Uri selectedImageUri;
    String pathfile;
    ImageView imageView;
    String Msun,Mmon,Mtue,Mwed,Mthu,Mfri,Msat, Asun,Amon,Atue,Awed,Athu,Afri,Asat,Esun,Emon,Etue,Ewed,Ethu,Efri,Esat;
    Spinner timeSlotSpinner,citySpinner;
    String doctorRedhealId,clinicName,clinicId,primeryNo,secondaryNo,city,cityId,address,fromTime1,toTime1,fromTime2,toTime2,fromTime3,toTime3,latitude,longitude,timeSlot,counsultaionFee,premiumFee;
    private RelativeLayout locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clinic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Clinic");

         clinicId = getIntent().getStringExtra("clinicId");
        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId", "1");

        URL = Apis.EDIT_CLINIC_BINDING_URL + clinicId;
        Log.e("urllll", "" + URL);

        etClinicName = (EditText) findViewById(R.id.etClinicName);
        etPrimeryNo = (EditText) findViewById(R.id.etPrimeryNo);
        etSecondaryNo = (EditText) findViewById(R.id.etSeconNo);

        etAddress = (EditText) findViewById(R.id.etState);
        etCounsultationFee = (EditText) findViewById(R.id.etCounsultaionFee);
        etPremiumFee = (EditText) findViewById(R.id.etPremiumFee);


        //  roundend Morning Buttons
        MbuttonSun = (Button) findViewById(R.id.MsunText);
        MbuttonMon = (Button) findViewById(R.id.MmonText);
        MbuttonTue = (Button) findViewById(R.id.MtueText);
        MbuttonWed = (Button) findViewById(R.id.MwedText);
        MbuttonThu = (Button) findViewById(R.id.MthuText);
        MbuttonFri = (Button) findViewById(R.id.MfriText);
        MbuttonSat = (Button) findViewById(R.id.MsatText);

        //  roundend Afternoon Buttons
        AbuttonSun = (Button) findViewById(R.id.AsunText);
        AbuttonMon = (Button) findViewById(R.id.AmonText);
        AbuttonTue = (Button) findViewById(R.id.AtueText);
        AbuttonWed = (Button) findViewById(R.id.AwedText);
        AbuttonThu = (Button) findViewById(R.id.AthuText);
        AbuttonFri = (Button) findViewById(R.id.AfriText);
        AbuttonSat = (Button) findViewById(R.id.AsatText);

        //  roundend Evening Buttons
        EbuttonSun = (Button) findViewById(R.id.EsunText);
        EbuttonMon = (Button) findViewById(R.id.EmonText);
        EbuttonTue = (Button) findViewById(R.id.EtueText);
        EbuttonWed = (Button) findViewById(R.id.EwedText);
        EbuttonThu = (Button) findViewById(R.id.EthuText);
        EbuttonFri = (Button) findViewById(R.id.EfriText);
        EbuttonSat = (Button) findViewById(R.id.EsatText);

        // Morning textviews
        MtxtSunday = (TextView) findViewById(R.id.Msunday);
        MtxtMonday = (TextView) findViewById(R.id.Mmonday);
        MtxtTuesday = (TextView) findViewById(R.id.Mthuseday);
        MtxtWednsday = (TextView) findViewById(R.id.Mwednesday);
        MtxtThursday = (TextView) findViewById(R.id.Mthursday);
        MtxtFriday = (TextView) findViewById(R.id.Mfriday);
        MtxtSaturday = (TextView) findViewById(R.id.Msaturday);

        // Afternoon textviews
        AtxtSunday = (TextView) findViewById(R.id.Asunday);
        AtxtMonday = (TextView) findViewById(R.id.Amonday);
        AtxtTuesday = (TextView) findViewById(R.id.Athuseday);
        AtxtWednsday = (TextView) findViewById(R.id.Awednesday);
        AtxtThursday = (TextView) findViewById(R.id.Athursday);
        AtxtFriday = (TextView) findViewById(R.id.Afriday);
        AtxtSaturday = (TextView) findViewById(R.id.Asaturday);

        // Evening textviews
        EtxtSunday = (TextView) findViewById(R.id.Esunday);
        EtxtMonday = (TextView) findViewById(R.id.Emonday);
        EtxtTuesday = (TextView) findViewById(R.id.Ethuseday);
        EtxtWednsday = (TextView) findViewById(R.id.Ewednesday);
        EtxtThursday = (TextView) findViewById(R.id.Ethursday);
        EtxtFriday = (TextView) findViewById(R.id.Efriday);
        EtxtSaturday = (TextView) findViewById(R.id.Esaturday);


        citySpinner = (Spinner) findViewById(R.id.etCity);
        //------------------------spinnerTimeSlot-------------
        List<String> city = new ArrayList<String>();
        city.add("Select City");
        city.add("Hyderabad");
        city.add("Vizag");
        final int[] actualValues = {0, 1, 2};

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>(this, R.layout.spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        citySpinner.setAdapter(dataAdapterCity);


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String cityItem = parent.getItemAtPosition(position).toString();

                // count = Integer.parseInt("0"+position);

                cityId = "0" + actualValues[position];

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + cityId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        timeSlotSpinner = (Spinner) findViewById(R.id.timeSlotSpinner);

        //------------------------spinnerTimeSlot-------------
        List<String> gender = new ArrayList<String>();
        gender.add("10 minutes");
        gender.add("15 minutes");
        gender.add("20 minutes");
        gender.add("30 minutes");
        gender.add("45 minutes");
        gender.add("60 minutes");
        gender.add("90 minutes");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterTimeSlot = new ArrayAdapter<String>(this, R.layout.spinner_item, gender);

        // Drop down layout style - list view with radio button
        dataAdapterTimeSlot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        timeSlotSpinner.setAdapter(dataAdapterTimeSlot);

        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Image Button
        ((Button) findViewById(R.id.submit_Button)).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.clinicImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("*/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), SELECT_PDF);
                selectImage();
            }
        });

        // MORNING BUTTONS
        MbuttonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    //Single click
                    MbuttonSun.setBackgroundResource(R.drawable.circle_1);
                    MtxtSunday.setText("Sun");
                    Log.e("sunday123", "" + MtxtSunday);

                } else if (i == 2) {
                    //Double click
                    i = 0;
                    MbuttonSun.setBackgroundResource(R.drawable.circle);
                    MtxtSunday.setText(null);
                    Log.e("sunday123456", "" + MtxtSunday);

                }


            }
        });

        MbuttonMon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                j++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        j = 0;
                    }
                };

                if (j == 1) {
                    //Single click
                    MbuttonMon.setBackgroundResource(R.drawable.circle_1);
                    MtxtMonday.setText("Mon");
                    Log.e("monday123", "" + MtxtMonday);

                } else if (j == 2) {
                    //Double click
                    j = 0;
                    MbuttonMon.setBackgroundResource(R.drawable.circle);
                    MtxtMonday.setText(null);
                    Log.e("monday123456", "" + MtxtMonday);

                }


            }
        });

        MbuttonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                k++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        k = 0;
                    }
                };

                if (k == 1) {
                    //Single click
                    MbuttonTue.setBackgroundResource(R.drawable.circle_1);
                    MtxtTuesday.setText("Tue");
                    Log.e("Tuesday123", "" + MtxtTuesday);

                } else if (k == 2) {
                    //Double click
                    k = 0;
                    MbuttonTue.setBackgroundResource(R.drawable.circle);
                    MtxtTuesday.setText(null);
                    Log.e("txtTuesday123456", "" + MtxtTuesday);

                }


            }
        });

        MbuttonWed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                l++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        l = 0;
                    }
                };

                if (l == 1) {
                    //Single click
                    MbuttonWed.setBackgroundResource(R.drawable.circle_1);
                    MtxtWednsday.setText("Wed");
                    Log.e("wed123", "" + MtxtTuesday);

                } else if (l == 2) {
                    //Double click
                    l = 0;
                    MbuttonWed.setBackgroundResource(R.drawable.circle);
                    MtxtWednsday.setText(null);
                    Log.e("wed123456", "" + MtxtWednsday);
                    // ShowDailog();
                }


            }
        });

        MbuttonThu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                m++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        m = 0;
                    }
                };

                if (m == 1) {
                    //Single click
                    MbuttonThu.setBackgroundResource(R.drawable.circle_1);
                    MtxtThursday.setText("Thu");
                    Log.e("Tuesday123", "" + MtxtTuesday);

                } else if (m == 2) {
                    //Double click
                    m = 0;
                    MbuttonThu.setBackgroundResource(R.drawable.circle);
                    MtxtThursday.setText(null);
                    Log.e("thursday123456", "" + MtxtTuesday);
                }


            }
        });

        MbuttonFri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                n++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        n = 0;
                    }
                };

                if (n == 1) {
                    //Single click
                    MbuttonFri.setBackgroundResource(R.drawable.circle_1);
                    MtxtFriday.setText("Fri");
                    Log.e("friday123", "" + MtxtFriday);

                } else if (n == 2) {
                    //Double click
                    n = 0;
                    MbuttonFri.setBackgroundResource(R.drawable.circle);
                    MtxtFriday.setText(null);
                    Log.e("friday123456", "" + MtxtFriday);

                }


            }
        });

        MbuttonSat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                p++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        p = 0;
                    }
                };

                if (p == 1) {
                    //Single click
                    MbuttonSat.setBackgroundResource(R.drawable.circle_1);
                    MtxtSaturday.setText("Sat");
                    Log.e("sat123", "" + MtxtSaturday);

                } else if (p == 2) {
                    //Double click
                    p = 0;
                    MbuttonSat.setBackgroundResource(R.drawable.circle);
                    MtxtSaturday.setText(null);
                    Log.e("sat123456", "" + MtxtSaturday);

                }


            }
        });


        // AFTER NOON BUTTONS

        AbuttonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    //Single click
                    AbuttonSun.setBackgroundResource(R.drawable.circle_1);
                    AtxtSunday.setText("Sun");
                    Log.e("sunday123", "" + AtxtSunday);

                } else if (i == 2) {
                    //Double click
                    i = 0;
                    AbuttonSun.setBackgroundResource(R.drawable.circle);
                    AtxtSunday.setText(null);
                    Log.e("sunday123456", "" + AtxtSunday);

                }


            }
        });

        AbuttonMon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                j++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        j = 0;
                    }
                };

                if (j == 1) {
                    //Single click
                    AbuttonMon.setBackgroundResource(R.drawable.circle_1);
                    AtxtMonday.setText("Mon");
                    Log.e("monday123", "" + AtxtMonday);

                } else if (j == 2) {
                    //Double click
                    j = 0;
                    AbuttonMon.setBackgroundResource(R.drawable.circle);
                    AtxtMonday.setText(null);
                    Log.e("monday123456", "" + AtxtMonday);

                }


            }
        });

        AbuttonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                k++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        k = 0;
                    }
                };

                if (k == 1) {
                    //Single click
                    AbuttonTue.setBackgroundResource(R.drawable.circle_1);
                    AtxtTuesday.setText("Tue");
                    Log.e("Tuesday123", "" + AtxtTuesday);

                } else if (k == 2) {
                    //Double click
                    k = 0;
                    AbuttonTue.setBackgroundResource(R.drawable.circle);
                    AtxtTuesday.setText(null);
                    Log.e("txtTuesday123456", "" + AtxtTuesday);

                }


            }
        });

        AbuttonWed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                l++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        l = 0;
                    }
                };

                if (l == 1) {
                    //Single click
                    AbuttonWed.setBackgroundResource(R.drawable.circle_1);
                    AtxtWednsday.setText("Wed");
                    Log.e("wed123", "" + AtxtTuesday);

                } else if (l == 2) {
                    //Double click
                    l = 0;
                    AbuttonWed.setBackgroundResource(R.drawable.circle);
                    AtxtWednsday.setText(null);
                    Log.e("wed123456", "" + AtxtWednsday);
                    // ShowDailog();
                }


            }
        });

        AbuttonThu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                m++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        m = 0;
                    }
                };

                if (m == 1) {
                    //Single click
                    AbuttonThu.setBackgroundResource(R.drawable.circle_1);
                    AtxtThursday.setText("Thu");
                    Log.e("Tuesday123", "" + AtxtTuesday);

                } else if (m == 2) {
                    //Double click
                    m = 0;
                    AbuttonThu.setBackgroundResource(R.drawable.circle);
                    AtxtThursday.setText(null);
                    Log.e("thursday123456", "" + AtxtTuesday);
                }


            }
        });

        AbuttonFri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                n++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        n = 0;
                    }
                };

                if (n == 1) {
                    //Single click
                    AbuttonFri.setBackgroundResource(R.drawable.circle_1);
                    AtxtFriday.setText("Fri");
                    Log.e("friday123", "" + AtxtFriday);

                } else if (n == 2) {
                    //Double click
                    n = 0;
                    AbuttonFri.setBackgroundResource(R.drawable.circle);
                    AtxtFriday.setText(null);
                    Log.e("friday123456", "" + AtxtFriday);

                }


            }
        });

        AbuttonSat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                p++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        p = 0;
                    }
                };

                if (p == 1) {
                    //Single click
                    AbuttonSat.setBackgroundResource(R.drawable.circle_1);
                    AtxtSaturday.setText("Sat");
                    Log.e("sat123", "" + AtxtSaturday);

                } else if (p == 2) {
                    //Double click
                    p = 0;
                    AbuttonSat.setBackgroundResource(R.drawable.circle);
                    AtxtSaturday.setText(null);
                    Log.e("sat123456", "" + AtxtSaturday);

                }


            }
        });


        // EVENING BUTTONS
        EbuttonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    //Single click
                    EbuttonSun.setBackgroundResource(R.drawable.circle_1);
                    EtxtSunday.setText("Sun");
                    Log.e("sunday123", "" + EtxtSunday);

                } else if (i == 2) {
                    //Double click
                    i = 0;
                    EbuttonSun.setBackgroundResource(R.drawable.circle);
                    EtxtSunday.setText(null);
                    Log.e("sunday123456", "" + EtxtSunday);

                }


            }
        });

        EbuttonMon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                j++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        j = 0;
                    }
                };

                if (j == 1) {
                    //Single click
                    EbuttonMon.setBackgroundResource(R.drawable.circle_1);
                    EtxtMonday.setText("Mon");
                    Log.e("monday123", "" + EtxtMonday);

                } else if (j == 2) {
                    //Double click
                    j = 0;
                    EbuttonMon.setBackgroundResource(R.drawable.circle);
                    EtxtMonday.setText(null);
                    Log.e("monday123456", "" + EtxtMonday);

                }


            }
        });

        EbuttonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                k++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        k = 0;
                    }
                };

                if (k == 1) {
                    //Single click
                    EbuttonTue.setBackgroundResource(R.drawable.circle_1);
                    EtxtTuesday.setText("Tue");
                    Log.e("Tuesday123", "" + EtxtTuesday);

                } else if (k == 2) {
                    //Double click
                    k = 0;
                    EbuttonTue.setBackgroundResource(R.drawable.circle);
                    EtxtTuesday.setText(null);
                    Log.e("txtTuesday123456", "" + EtxtTuesday);

                }


            }
        });

        EbuttonWed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                l++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        l = 0;
                    }
                };

                if (l == 1) {
                    //Single click
                    EbuttonWed.setBackgroundResource(R.drawable.circle_1);
                    EtxtWednsday.setText("Wed");
                    Log.e("wed123", "" + EtxtTuesday);

                } else if (l == 2) {
                    //Double click
                    l = 0;
                    EbuttonWed.setBackgroundResource(R.drawable.circle);
                    EtxtWednsday.setText(null);
                    Log.e("wed123456", "" + EtxtWednsday);
                    // ShowDailog();
                }


            }
        });

        EbuttonThu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                m++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        m = 0;
                    }
                };

                if (m == 1) {
                    //Single click
                    EbuttonThu.setBackgroundResource(R.drawable.circle_1);
                    EtxtThursday.setText("Thu");
                    Log.e("Tuesday123", "" + EtxtTuesday);

                } else if (m == 2) {
                    //Double click
                    m = 0;
                    EbuttonThu.setBackgroundResource(R.drawable.circle);
                    EtxtThursday.setText(null);
                    Log.e("thursday123456", "" + EtxtTuesday);
                }


            }
        });

        EbuttonFri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                n++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        n = 0;
                    }
                };

                if (n == 1) {
                    //Single click
                    EbuttonFri.setBackgroundResource(R.drawable.circle_1);
                    EtxtFriday.setText("Fri");
                    Log.e("friday123", "" + EtxtFriday);

                } else if (n == 2) {
                    //Double click
                    n = 0;
                    EbuttonFri.setBackgroundResource(R.drawable.circle);
                    EtxtFriday.setText(null);
                    Log.e("friday123456", "" + EtxtFriday);

                }


            }
        });

        EbuttonSat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                p++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        p = 0;
                    }
                };

                if (p == 1) {
                    //Single click
                    EbuttonSat.setBackgroundResource(R.drawable.circle_1);
                    EtxtSaturday.setText("Sat");
                    Log.e("sat123", "" + EtxtSaturday);

                } else if (p == 2) {
                    //Double click
                    p = 0;
                    EbuttonSat.setBackgroundResource(R.drawable.circle);
                    EtxtSaturday.setText(null);
                    Log.e("sat123456", "" + EtxtSaturday);

                }


            }
        });


        // etFromTime1 Format

        etFromTime1 = (EditText) findViewById(R.id.etTime1);
        etFromTime1.setInputType(InputType.TYPE_NULL);
        etFromTime1.requestFocus();
        etFromTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etFromTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etFromTime1.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // time picker 2
        etToTime1 = (EditText) findViewById(R.id.etTime2);
        etToTime1.setInputType(InputType.TYPE_NULL);
        etToTime1.requestFocus();
        etToTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etToTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etToTime1.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // time picker 3
        etFromTime2 = (EditText) findViewById(R.id.etTime3);
        etFromTime2.setInputType(InputType.TYPE_NULL);
        etFromTime2.requestFocus();
        etFromTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etFromTime2.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etFromTime2.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        // time picker 4
        etToTime2 = (EditText) findViewById(R.id.etTime4);
        etToTime2.setInputType(InputType.TYPE_NULL);
        etToTime2.requestFocus();
        etToTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etToTime2.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etToTime2.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        // time picker 5
        etFromTime3 = (EditText) findViewById(R.id.etTime5);
        etFromTime3.setInputType(InputType.TYPE_NULL);
        etFromTime3.requestFocus();
        etFromTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etFromTime3.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etFromTime3.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        // time picker 6
        etToTime3 = (EditText) findViewById(R.id.etTime6);
        etToTime3.setInputType(InputType.TYPE_NULL);
        etToTime3.requestFocus();
        etToTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            etToTime3.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            etToTime3.setText(selectedHour + ":" + selectedMinute + " " + " PM");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        locationButton = (RelativeLayout) findViewById(R.id.location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlacePickerActivity();
            }
        });

        new ReaderdateList().execute();
    }

        // google place picker
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(EditClinicActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(EditClinicActivity.this);

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
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
            else if (requestCode == REQUEST_CODE_PLACEPICKER){
                displaySelectedPlaceFromPlacePicker(data);
            }
        }
    }


    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, this);
        Log.e("placeSelected",""+placeSelected);

        String name = placeSelected.getName().toString();
        String address = placeSelected.getAddress().toString();
        Log.e("address",""+address);

        final TextView locationTxt =(TextView)findViewById(R.id.addClinic_location) ;
        locationTxt.setText(address);

        // places latitude
        LatLng qLoc = placeSelected.getLatLng();
        Double lat =qLoc.latitude;
        Log.e("lat", "Place: " + lat);
        latitude = String.valueOf(lat);

        // places longitude
        Double lon =qLoc.longitude;
        Log.e("lon", "Place: " + lon);
        longitude =String.valueOf(lon);


      /*  TextView enterCurrentLocation = (TextView) findViewById(R.id.textView9);
        enterCurrentLocation.setText(name + ", " + address);*/
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








    @Override
    public void onClick(View v) {
        checkValidation();
    }



    public class ReaderdateList extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Create a new progress dialog
            progressDialog = new ProgressDialog(EditClinicActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            // progressDialog.setTitle("Chargement...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Fetching ...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(true);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            // showing refresh animation before making http call
            //swipeRefreshLayout.setRefreshing(true);

        }


        @Override
        protected JSONObject doInBackground(String... params) {

            // appending offset to url
           // String url = URL +"/"+offSet;

            //Getting product details from making HTTP Request
            JSONObject json = jsonParser.makeHttpRequest(URL, "GET", param);

            Log.e("url", URL);

            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            progressDialog.dismiss();

            // stopping swipe refresh
           // swipeRefreshLayout.setRefreshing(false);

            Log.e("data", String.valueOf(json));

            String data = "";
            try {

               // postArrayList.clear();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = json.optJSONArray("clinic_edit");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String clinicName = jsonObject.optString("clinicName").toString();
                    Log.e("clinicName12",""+clinicName);
                    String address = jsonObject.optString("address").toString();
                    String primeryNo = jsonObject.optString("primaryNumber").toString();
                    String seconNo = jsonObject.optString("secondaryNumber").toString();
                    String fromtime1 = jsonObject.optString("fromtime1").toString();
                    String totime1 = jsonObject.optString("totime1").toString();
                    String fromtime2 = jsonObject.optString("fromtime2").toString();
                    String totime2 = jsonObject.optString("totime2").toString();
                    String fromtime3 = jsonObject.optString("fromtime3").toString();
                    String totime3 = jsonObject.optString("totime3").toString();
                    String clinicRedhealId = jsonObject.optString("clinic_redhealId").toString();
                    String consultationFee = jsonObject.optString("consultationFee").toString();
                    String premiumConsultationFee = jsonObject.optString("premiumConsultationFee").toString();

                    String imageUrl =jsonObject.optString("imagePath").toString();

                    Picasso.with(getBaseContext()).load(imageUrl).into(imageView);
                    etClinicName.setText(clinicName);

                    etPrimeryNo.setText(primeryNo);
                    etSecondaryNo.setText(seconNo);
                    etAddress.setText(address);
                    etCounsultationFee.setText(consultationFee);
                    etPremiumFee.setText(premiumConsultationFee);
                    etFromTime1.setText(fromtime1);
                    etFromTime2.setText(fromtime2);
                    etFromTime3.setText(fromtime3);
                    etToTime1.setText(totime1);
                    etToTime2.setText(totime2);
                    etToTime3.setText(totime3);

                   // postArrayList.add(new MyClinicItem(clinicName,address,primeryNo,seconNo,morningTimings,morningDays,afterNoonTimings,afterNoonDays,eveningTimings,eveningDays,clinicRedhealId,imageUrl));

                }
                // output.setText(data);
            } catch (JSONException e) {e.printStackTrace();}

        }
    }



    private void checkValidation() {
        clinicName = etClinicName.getText().toString();
        primeryNo = etPrimeryNo.getText().toString();
        secondaryNo = etSecondaryNo.getText().toString();
        city = citySpinner.getSelectedItem().toString();
        address = etAddress.getText().toString();
        counsultaionFee = etCounsultationFee.getText().toString();
        premiumFee = etPremiumFee.getText().toString();
        timeSlot = timeSlotSpinner.getSelectedItem().toString();

        fromTime1 =etFromTime1.getText().toString();
        toTime1 =etToTime1.getText().toString();
        fromTime2 =etFromTime2.getText().toString();
        toTime2 =etToTime2.getText().toString();
        fromTime3 =etFromTime3.getText().toString();
        toTime3 =etToTime3.getText().toString();

        Msun=MtxtSunday.getText().toString();
        Mmon= MtxtMonday.getText().toString();
        Mtue= MtxtTuesday.getText().toString();
        Mwed= MtxtWednsday.getText().toString();
        Mthu= MtxtThursday.getText().toString();
        Mfri= MtxtFriday.getText().toString();
        Msat= MtxtSaturday.getText().toString();

        Asun=AtxtSunday.getText().toString();
        Amon= AtxtMonday.getText().toString();
        Atue= AtxtTuesday.getText().toString();
        Awed= AtxtWednsday.getText().toString();
        Athu= AtxtThursday.getText().toString();
        Afri= AtxtFriday.getText().toString();
        Asat= AtxtSaturday.getText().toString();

        Esun=EtxtSunday.getText().toString();
        Emon= EtxtMonday.getText().toString();
        Etue= EtxtTuesday.getText().toString();
        Ewed= EtxtWednsday.getText().toString();
        Ethu= EtxtThursday.getText().toString();
        Efri= EtxtFriday.getText().toString();
        Esat= EtxtSaturday.getText().toString();

        // Check for both field is empty or not
        if (clinicName.equals("")  || primeryNo.equals("") || secondaryNo.equals("") || city.equals("") || address.equals("") || counsultaionFee.equals("") || premiumFee.equals("") || timeSlot.equals("") ||
                fromTime1.equals("") || toTime1.equals("")) {
//            validationLayout.startAnimation(shakeAnimation);
            Toast.makeText(EditClinicActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }
        else if (Msun.equals("") && Mmon.equals("") && Mtue.equals("") && Mwed.equals("") && Mthu.equals("") && Mfri.equals("") && Msat.equals("")){

            Toast.makeText(EditClinicActivity.this,"Please Select Availability Days",Toast.LENGTH_LONG).show();
        }
        // Check if mobile id is valid or not
        else if (primeryNo.isEmpty() || !Patterns.PHONE.matcher(primeryNo).matches() || primeryNo.length()<9){
            etPrimeryNo.setError("Please Enter Valid No");
            return;
        }
        else if (secondaryNo.isEmpty() || !Patterns.PHONE.matcher(secondaryNo).matches() || secondaryNo.length()<9){
            etSecondaryNo.setError("Please Enter Valid No");
            return;
        }

        else if (address.isEmpty() || address.length()<3) {
            etAddress.setError("Please Enter Min 3 characters");
            return;
        }

        else {

            //init new Product object
            EditClinicItem product = new EditClinicItem(clinicId, doctorRedhealId,clinicName,primeryNo,secondaryNo,city,address,latitude,longitude,timeSlot,premiumFee,counsultaionFee,
                    fromTime1,fromTime2,fromTime3,toTime1,toTime2,toTime3,Msun,Mmon,Mtue,Mwed,Mthu,Mfri,Msat,
                    Asun,Amon,Atue,Awed,Athu,Afri,Asat,Esun,Emon,Etue,Ewed,Ethu,Efri,Esat);

            //convert product => json array
            JSONArray jProducts = new JSONArray();
            JSONObject jProduct = new JSONObject();

            try {

                jProduct.put("clinic_redhealId",product.getClinicId());
                Log.e("clinic_redhealId",""+product.getClinicId());
                jProduct.put("doctor_redhealId",product.getDoctorRedhealId());
                jProduct.put("clinicName",product.getClinicName());
                jProduct.put("primaryNumber",product.getPrimeryNo());
                jProduct.put("secondaryNumber",product.getSecondaryNo());
                jProduct.put("address",product.getAddress());
                jProduct.put("latitude",product.getLatitude());
                jProduct.put("longitude",product.getLangitude());
                jProduct.put("fromtime1",product.getFromTime1());
                jProduct.put("totime1",product.getToTime1());
                jProduct.put("fromtime2",product.getFromTime2());
                jProduct.put("totime2",product.getToTime2());
                jProduct.put("fromtime3",product.getFromTime3());
                jProduct.put("totime3",product.getToTime3());
                jProduct.put("consultationTime",product.getTimeSlot());
                jProduct.put("consultationFee",product.getConsultationFee());
                jProduct.put("premiumConsultationFee",product.getPrimumFee());

                jProduct.put("day1",product.getDay1());
                jProduct.put("day2",product.getDay2());
                jProduct.put("day3",product.getDay3());
                jProduct.put("day4",product.getDay4());
                jProduct.put("day5",product.getDay5());
                jProduct.put("day6",product.getDay6());
                jProduct.put("day7",product.getDay7());
                jProduct.put("day8",product.getDay8());
                jProduct.put("day9",product.getDay9());
                jProduct.put("day10",product.getDay10());
                jProduct.put("day11",product.getDay11());
                jProduct.put("day12",product.getDay12());
                jProduct.put("day13",product.getDay13());
                jProduct.put("day14",product.getDay14());
                jProduct.put("day15",product.getDay15());
                jProduct.put("day16",product.getDay16());
                jProduct.put("day17",product.getDay17());
                jProduct.put("day18",product.getDay18());
                jProduct.put("day19",product.getDay19());
                jProduct.put("day20",product.getDay20());
                jProduct.put("day21",product.getDay21());



                //add to json array
                jProducts.put(jProduct);
                Log.d("json api", "Json array converted from Product: " + jProducts.toString());

                String jsonData = jProduct.toString();
                Log.e("jsonData",""+jsonData);
                new DoUpdateProfile().execute(jsonData);

                Intent editIntent = new Intent(EditClinicActivity.this,MyClinicsActivity.class);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(editIntent);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    class DoUpdateProfile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("params_test",""+params);
            String jsonData = params[0];

            Log.e("jsonData_test_doinback",""+jsonData);

            try {
                java.net.URL url = new URL(Apis.ADD_CLINIC_URL);
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

                is.close();


//                osw.flush();
                osw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // startActivity(new Intent(MyAppointmentsActivity.this,Home.class));
        }*/

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Intent intent =new Intent(EditClinicActivity.this, MyClinicsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

}
