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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.medoske.www.redheal_d.APIS.Apis;
import com.medoske.www.redheal_d.Controllers.FileUtils;
import com.medoske.www.redheal_d.Controllers.Utils;
import com.medoske.www.redheal_d.Helpers.ServiceHandler;
import com.medoske.www.redheal_d.Helpers.Utility;
import com.medoske.www.redheal_d.Items.AddClinicItem;
import com.medoske.www.redheal_d.Items.AllCitiesItem;
import com.medoske.www.redheal_d.Items.AreaItems;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.medoske.www.redheal_d.Controllers.FileUtils.getPath;

public class AddClinicActivity extends AppCompatActivity implements View.OnClickListener {
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
    EditText etFromTime1, etToTime1, etFromTime2, etToTime2, etFromTime3, etToTime3 ,etClinicName,etDoorNo,etPrimeryNo,etSecondaryNo,etPincode,etLandMark,etCounsultationFee,etPremiumFee,etPassword;
    TextView MtxtSunday,MtxtMonday,MtxtTuesday,MtxtWednsday,MtxtThursday,MtxtFriday,MtxtSaturday,
    AtxtSunday,AtxtMonday,AtxtTuesday,AtxtWednsday,AtxtThursday,AtxtFriday,AtxtSaturday,
    EtxtSunday,EtxtMonday,EtxtTuesday,EtxtWednsday,EtxtThursday,EtxtFriday,EtxtSaturday;
    private static final int SELECT_PDF = 2;
    String selectedImagePath;
    Uri selectedImageUri;
    String pathfile;
    ImageView imageView;
    String Msun,Mmon,Mtue,Mwed,Mthu,Mfri,Msat, Asun,Amon,Atue,Awed,Athu,Afri,Asat,Esun,Emon,Etue,Ewed,Ethu,Efri,Esat;
    Spinner timeSlotSpinner,citySpinner,areaSpinner;
    String doctorRedhealId,clinicName,primeryNo,secondaryNo,password,doorNo,landmark,pincode,city,cityId,area,areaId,address,fromTime1,toTime1,fromTime2,toTime2,fromTime3,toTime3,latitude,longitude,timeSlot,counsultaionFee,premiumFee;
    Toolbar toolbar;
    private static LinearLayout validationLayout;
    private static Animation shakeAnimation;
    private RelativeLayout locationButton;
    int count = 1;

    int cityCode,areaCode;
    String URL_AREA;
    ArrayAdapter<String> citySpinnerAdapter;
    ArrayAdapter<String> areaSpinnerAdapter;
    ArrayList<AllCitiesItem> allCitiesItems=new ArrayList<AllCitiesItem>();
    ArrayList<AreaItems> areaItems=new ArrayList<AreaItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" Add Clinic");


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", 0); // 0 for private mode
        doctorRedhealId = sp.getString("RedhealId","1");

        validationLayout=(LinearLayout)findViewById(R.id.validationLayout);

        etClinicName=(EditText)findViewById(R.id.etClinicName);
        etPrimeryNo=(EditText)findViewById(R.id.etPrimeryNo);
        etSecondaryNo=(EditText)findViewById(R.id.etSeconNo);

        etPassword=(EditText)findViewById(R.id.etPassword);
        etDoorNo=(EditText)findViewById(R.id.etStreet);
        etLandMark=(EditText)findViewById(R.id.etLandMark);
        etPincode=(EditText)findViewById(R.id.etPincode);


        etCounsultationFee=(EditText)findViewById(R.id.etCounsultaionFee);
        etPremiumFee=(EditText)findViewById(R.id.etPremiumFee);

        //  roundend Morning Buttons
    MbuttonSun =(Button)findViewById(R.id.MsunText);
    MbuttonMon =(Button)findViewById(R.id.MmonText);
    MbuttonTue =(Button)findViewById(R.id.MtueText);
    MbuttonWed =(Button)findViewById(R.id.MwedText);
    MbuttonThu =(Button)findViewById(R.id.MthuText);
    MbuttonFri =(Button)findViewById(R.id.MfriText);
    MbuttonSat =(Button)findViewById(R.id.MsatText);

    //  roundend Afternoon Buttons
    AbuttonSun =(Button)findViewById(R.id.AsunText);
    AbuttonMon =(Button)findViewById(R.id.AmonText);
    AbuttonTue =(Button)findViewById(R.id.AtueText);
    AbuttonWed =(Button)findViewById(R.id.AwedText);
    AbuttonThu =(Button)findViewById(R.id.AthuText);
    AbuttonFri =(Button)findViewById(R.id.AfriText);
    AbuttonSat =(Button)findViewById(R.id.AsatText);

    //  roundend Evening Buttons
    EbuttonSun =(Button)findViewById(R.id.EsunText);
    EbuttonMon =(Button)findViewById(R.id.EmonText);
    EbuttonTue =(Button)findViewById(R.id.EtueText);
    EbuttonWed =(Button)findViewById(R.id.EwedText);
    EbuttonThu =(Button)findViewById(R.id.EthuText);
    EbuttonFri =(Button)findViewById(R.id.EfriText);
    EbuttonSat =(Button)findViewById(R.id.EsatText);

    // Morning textviews
    MtxtSunday=(TextView)findViewById(R.id.Msunday);
    MtxtMonday=(TextView)findViewById(R.id.Mmonday);
    MtxtTuesday=(TextView)findViewById(R.id.Mthuseday);
    MtxtWednsday=(TextView)findViewById(R.id.Mwednesday);
    MtxtThursday=(TextView)findViewById(R.id.Mthursday);
    MtxtFriday=(TextView)findViewById(R.id.Mfriday);
    MtxtSaturday=(TextView)findViewById(R.id.Msaturday);

    // Afternoon textviews
    AtxtSunday=(TextView)findViewById(R.id.Asunday);
    AtxtMonday=(TextView)findViewById(R.id.Amonday);
    AtxtTuesday=(TextView)findViewById(R.id.Athuseday);
    AtxtWednsday=(TextView)findViewById(R.id.Awednesday);
    AtxtThursday=(TextView)findViewById(R.id.Athursday);
    AtxtFriday=(TextView)findViewById(R.id.Afriday);
    AtxtSaturday=(TextView)findViewById(R.id.Asaturday);

    // Evening textviews
    EtxtSunday=(TextView)findViewById(R.id.Esunday);
    EtxtMonday=(TextView)findViewById(R.id.Emonday);
    EtxtTuesday=(TextView)findViewById(R.id.Ethuseday);
    EtxtWednsday=(TextView)findViewById(R.id.Ewednesday);
    EtxtThursday=(TextView)findViewById(R.id.Ethursday);
    EtxtFriday=(TextView)findViewById(R.id.Efriday);
    EtxtSaturday=(TextView)findViewById(R.id.Esaturday);


    citySpinner=(Spinner)findViewById(R.id.etCity);
    areaSpinner=(Spinner)findViewById(R.id.etArea);



    timeSlotSpinner=(Spinner)findViewById(R.id.timeSlotSpinner);

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
        ((Button)findViewById(R.id.submit_Button)).setOnClickListener(this);
    imageView =(ImageView)findViewById(R.id.clinicImage);
        imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent();
//            intent.setType("*/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), SELECT_PDF);
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
                Log.e("sunday123",""+MtxtSunday);

            } else if (i == 2) {
                //Double click
                i = 0;
                MbuttonSun.setBackgroundResource(R.drawable.circle);
                MtxtSunday.setText(null);
                Log.e("sunday123456",""+MtxtSunday);

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
                Log.e("monday123",""+MtxtMonday);

            } else if (j == 2) {
                //Double click
                j = 0;
                MbuttonMon.setBackgroundResource(R.drawable.circle);
                MtxtMonday.setText(null);
                Log.e("monday123456",""+MtxtMonday);

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
                Log.e("Tuesday123",""+MtxtTuesday);

            } else if (k == 2) {
                //Double click
                k = 0;
                MbuttonTue.setBackgroundResource(R.drawable.circle);
                MtxtTuesday.setText(null);
                Log.e("txtTuesday123456",""+MtxtTuesday);

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
                Log.e("wed123",""+MtxtTuesday);

            } else if (l == 2) {
                //Double click
                l = 0;
                MbuttonWed.setBackgroundResource(R.drawable.circle);
                MtxtWednsday.setText(null);
                Log.e("wed123456",""+MtxtWednsday);
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
                Log.e("Tuesday123",""+MtxtTuesday);

            } else if (m == 2) {
                //Double click
                m = 0;
                MbuttonThu.setBackgroundResource(R.drawable.circle);
                MtxtThursday.setText(null);
                Log.e("thursday123456",""+MtxtTuesday);
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
                Log.e("friday123",""+MtxtFriday);

            } else if (n == 2) {
                //Double click
                n = 0;
                MbuttonFri.setBackgroundResource(R.drawable.circle);
                MtxtFriday.setText(null);
                Log.e("friday123456",""+MtxtFriday);

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
                Log.e("sat123",""+MtxtSaturday);

            } else if (p == 2) {
                //Double click
                p = 0;
                MbuttonSat.setBackgroundResource(R.drawable.circle);
                MtxtSaturday.setText(null);
                Log.e("sat123456",""+MtxtSaturday);

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
                Log.e("sunday123",""+AtxtSunday);

            } else if (i == 2) {
                //Double click
                i = 0;
                AbuttonSun.setBackgroundResource(R.drawable.circle);
                AtxtSunday.setText(null);
                Log.e("sunday123456",""+AtxtSunday);

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
                Log.e("monday123",""+AtxtMonday);

            } else if (j == 2) {
                //Double click
                j = 0;
                AbuttonMon.setBackgroundResource(R.drawable.circle);
                AtxtMonday.setText(null);
                Log.e("monday123456",""+AtxtMonday);

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
                Log.e("Tuesday123",""+AtxtTuesday);

            } else if (k == 2) {
                //Double click
                k = 0;
                AbuttonTue.setBackgroundResource(R.drawable.circle);
                AtxtTuesday.setText(null);
                Log.e("txtTuesday123456",""+AtxtTuesday);

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
                Log.e("wed123",""+AtxtTuesday);

            } else if (l == 2) {
                //Double click
                l = 0;
                AbuttonWed.setBackgroundResource(R.drawable.circle);
                AtxtWednsday.setText(null);
                Log.e("wed123456",""+AtxtWednsday);
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
                Log.e("Tuesday123",""+AtxtTuesday);

            } else if (m == 2) {
                //Double click
                m = 0;
                AbuttonThu.setBackgroundResource(R.drawable.circle);
                AtxtThursday.setText(null);
                Log.e("thursday123456",""+AtxtTuesday);
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
                Log.e("friday123",""+AtxtFriday);

            } else if (n == 2) {
                //Double click
                n = 0;
                AbuttonFri.setBackgroundResource(R.drawable.circle);
                AtxtFriday.setText(null);
                Log.e("friday123456",""+AtxtFriday);

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
                Log.e("sat123",""+AtxtSaturday);

            } else if (p == 2) {
                //Double click
                p = 0;
                AbuttonSat.setBackgroundResource(R.drawable.circle);
                AtxtSaturday.setText(null);
                Log.e("sat123456",""+AtxtSaturday);

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
                Log.e("sunday123",""+EtxtSunday);

            } else if (i == 2) {
                //Double click
                i = 0;
                EbuttonSun.setBackgroundResource(R.drawable.circle);
                EtxtSunday.setText(null);
                Log.e("sunday123456",""+EtxtSunday);

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
                Log.e("monday123",""+EtxtMonday);

            } else if (j == 2) {
                //Double click
                j = 0;
                EbuttonMon.setBackgroundResource(R.drawable.circle);
                EtxtMonday.setText(null);
                Log.e("monday123456",""+EtxtMonday);

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
                Log.e("Tuesday123",""+EtxtTuesday);

            } else if (k == 2) {
                //Double click
                k = 0;
                EbuttonTue.setBackgroundResource(R.drawable.circle);
                EtxtTuesday.setText(null);
                Log.e("txtTuesday123456",""+EtxtTuesday);

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
                Log.e("wed123",""+EtxtTuesday);

            } else if (l == 2) {
                //Double click
                l = 0;
                EbuttonWed.setBackgroundResource(R.drawable.circle);
                EtxtWednsday.setText(null);
                Log.e("wed123456",""+EtxtWednsday);
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
                Log.e("Tuesday123",""+EtxtTuesday);

            } else if (m == 2) {
                //Double click
                m = 0;
                EbuttonThu.setBackgroundResource(R.drawable.circle);
                EtxtThursday.setText(null);
                Log.e("thursday123456",""+EtxtTuesday);
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
                Log.e("friday123",""+EtxtFriday);

            } else if (n == 2) {
                //Double click
                n = 0;
                EbuttonFri.setBackgroundResource(R.drawable.circle);
                EtxtFriday.setText(null);
                Log.e("friday123456",""+EtxtFriday);

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
                Log.e("sat123",""+EtxtSaturday);

            } else if (p == 2) {
                //Double click
                p = 0;
                EbuttonSat.setBackgroundResource(R.drawable.circle);
                EtxtSaturday.setText(null);
                Log.e("sat123456",""+EtxtSaturday);

            }

        }
    });



    // etFromTime1 Format

    etFromTime1 =(EditText)findViewById(R.id.etTime1);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etFromTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etFromTime1.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    });

    // time picker 2
    etToTime1 =(EditText)findViewById(R.id.etTime2);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etToTime1.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etToTime1.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    });

    // time picker 3
    etFromTime2 =(EditText)findViewById(R.id.etTime3);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etFromTime2.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etFromTime2.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    });
    // time picker 4
    etToTime2 =(EditText)findViewById(R.id.etTime4);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etToTime2.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etToTime2.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    });
    // time picker 5
    etFromTime3 =(EditText)findViewById(R.id.etTime5);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etFromTime3.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etFromTime3.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    });
    // time picker 6
    etToTime3 =(EditText)findViewById(R.id.etTime6);
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

            mTimePicker = new TimePickerDialog(AddClinicActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if(selectedHour < 12 && selectedHour >= 0)
                    {
                        etToTime3.setText(selectedHour + ":" + selectedMinute + " " + " AM");
                    }
                    else {
                        selectedHour -= 12;
                        if(selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                        etToTime3.setText(selectedHour + ":" + selectedMinute + " "  + " PM");
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
    });


    locationButton =(RelativeLayout)findViewById(R.id.location);
        locationButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startPlacePickerActivity();
        }
    });

        new GetAllCities().execute();


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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddClinicActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddClinicActivity.this);

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


    // spinner one

    private class GetAllCities extends AsyncTask<Void, Void, Void> {
        ArrayList<String> list;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... params) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(Apis.ALL_CITIES_URL,ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("cities");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            AllCitiesItem cat = new AllCitiesItem(catObj.getString("cityId"),
                                    catObj.getString("cityName"));
                            allCitiesItems.add(cat);
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

        protected void onPostExecute(Void result) {
            Log.d("spinner", "date");
            populateSpinner();
        }
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < allCitiesItems.size(); i++) {
            lables.add(allCitiesItems.get(i).getCityName());
        }

        // Creating adapter for spinner
        citySpinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lables);

        // Drop down layout style - list view with radio button
        citySpinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        citySpinner.setAdapter(citySpinnerAdapter);


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                // dn = adapterView.getSelectedItem().toString().trim();
                cityCode = citySpinner.getSelectedItemPosition();
                Log.e("citycode", String.valueOf(cityCode));

                for (int i = 0; i < allCitiesItems.size(); i++) {
                    if (i == cityCode) {
                        cityId = allCitiesItems.get(i).getCitYId();
                        Log.e("cityyyyid",""+cityId);
                    }

                    URL_AREA = Apis.AREA_URL+ cityId.replace(" ", "%20").trim();
                    new GetCategories().execute(URL_AREA);
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //------------------------spinner two-----------------------------------//

    private class GetCategories extends AsyncTask<String, Void, Void> {
     ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddClinicActivity.this);
            pDialog.setMessage("Fetching ....");
            pDialog.setCancelable(false);

        }


        @Override
        protected Void doInBackground(String... urls) {
            ServiceHandler jsonParser = new ServiceHandler();

            Log.i("section url",urls[0]);
            String json = jsonParser.makeServiceCall(urls[0], ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("areas");
                        areaItems.clear();
                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            AreaItems cat = new AreaItems(catObj.getString("areaId"),
                                    catObj.getString("areaName"));
                            areaItems.add(cat);
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinnertwo();
            GetAllCities getCoursesFac = new GetAllCities();
            getCoursesFac.getClass().toString();

        }

    }

    /**
     * Adding spinner data
     */
    private void populateSpinnertwo() {
        List<String> lables = new ArrayList<String>();


        lables.clear();
        //txtCategory.setText("");

        for (int i = 0; i < areaItems.size(); i++) {
            lables.add(areaItems.get(i).getAreaName());
            Log.i("label name",areaItems.get(i).getAreaName());
        }

        Log.i("labels",lables.toString());
        // Creating adapter for spinner
        areaSpinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lables);

        // Drop down layout style - list view with radio button
        areaSpinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        areaSpinner.setAdapter(areaSpinnerAdapter);
        citySpinnerAdapter.notifyDataSetChanged();
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long l) {

                areaCode=areaSpinner.getSelectedItemPosition();
                for (int i = 0; i < areaItems.size(); i++) {
                    if (i == areaCode) {
                        areaId = areaItems.get(i).getAreaId();
                        Log.e("areaaaid",""+areaId);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    @Override
    public void onClick(View v) {
        checkValidation();
    }


    // register post
    public String POST(String[] params, AddClinicItem addClinicItem){
        InputStream inputStream = null;
        String result = "";
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.ADD_CLINIC_URL);

            try {

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("doctor_redhealId", new StringBody(doctorRedhealId));
                entity.addPart("clinicName", new StringBody(addClinicItem.getClinicName()));
                Log.e("clinicName+++",""+addClinicItem.getClinicName());
                entity.addPart("primaryNumber", new StringBody(addClinicItem.getPrimeryNo()));
                entity.addPart("secondaryNumber", new StringBody(addClinicItem.getSecondaryNo()));


                entity.addPart("cityId", new StringBody(addClinicItem.getCityId()));
                Log.e("city+++",""+cityId);
                entity.addPart("areaId", new StringBody(addClinicItem.getAreaId()));
                entity.addPart("password", new StringBody(addClinicItem.getPassword()));
                entity.addPart("street", new StringBody(addClinicItem.getStreetName()));
                entity.addPart("pincode", new StringBody(addClinicItem.getPincode()));
                entity.addPart("landmark", new StringBody(addClinicItem.getLandMark()));

                entity.addPart("latitude", new StringBody(latitude));
                entity.addPart("longitude", new StringBody(longitude));
                entity.addPart("consultationTime", new StringBody(addClinicItem.getTimeSlot()));
                entity.addPart("consultationFee", new StringBody(addClinicItem.getConsultationFee()));
                entity.addPart("premiumConsultationFee", new StringBody(addClinicItem.getPrimumFee()));

                entity.addPart("fromtime1", new StringBody(addClinicItem.getFromTime1()));
                entity.addPart("totime1", new StringBody(addClinicItem.getToTime1()));
                entity.addPart("fromtime2", new StringBody(addClinicItem.getFromTime2()));
                entity.addPart("totime2", new StringBody(addClinicItem.getToTime2()));
                entity.addPart("fromtime3", new StringBody(addClinicItem.getFromTime3()));
                entity.addPart("totime3", new StringBody(addClinicItem.getToTime3()));

                entity.addPart("day1", new StringBody(Msun));
                entity.addPart("day2", new StringBody(Mmon));
                entity.addPart("day3", new StringBody(Mtue));
                entity.addPart("day4", new StringBody(Mwed));
                entity.addPart("day5", new StringBody(Mthu));
                entity.addPart("day6", new StringBody(Mfri));
                entity.addPart("day7", new StringBody(Msat));

                entity.addPart("day8", new StringBody(Asun));
                entity.addPart("daY9", new StringBody(Amon));
                entity.addPart("day10", new StringBody(Atue));
                entity.addPart("day11", new StringBody(Awed));
                entity.addPart("day12", new StringBody(Athu));
                entity.addPart("day13", new StringBody(Afri));
                entity.addPart("day14", new StringBody(Asat));

                entity.addPart("day15", new StringBody(Esun));
                entity.addPart("day16", new StringBody(Emon));
                entity.addPart("day17", new StringBody(Etue));
                entity.addPart("day18", new StringBody(Ewed));
                entity.addPart("day19", new StringBody(Ethu));
                entity.addPart("day20", new StringBody(Efri));
                entity.addPart("day21", new StringBody(Esat));

                File file = new File(selectedImagePath);
                entity.addPart("imagePath", new FileBody(file));

                Log.e("sample1234",""+ file);

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

    // convert input stream to string
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    // Async task
    private class Asyncclass extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Create a new progress dialog
            progressDialog = new ProgressDialog(AddClinicActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            // progressDialog.setTitle("Chargement...");
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

            AddClinicItem addClinicItem = new AddClinicItem();
            addClinicItem.setClinicName(clinicName);
            addClinicItem.setPrimeryNo(primeryNo);
            addClinicItem.setSecondaryNo(secondaryNo);

            addClinicItem.setPassword(password);
            addClinicItem.setCityId(cityId);
            Log.e("cityId1234",""+ cityId);
            addClinicItem.setAreaId(areaId);
            addClinicItem.setStreetName(doorNo);
            addClinicItem.setLandMark(landmark);
            addClinicItem.setPincode(pincode);

            addClinicItem.setLatitude(latitude);
            addClinicItem.setLangitude(longitude);
            addClinicItem.setTimeSlot(timeSlot);
            addClinicItem.setPrimumFee(premiumFee);
            addClinicItem.setConsultationFee(counsultaionFee);

            addClinicItem.setFromTime1(fromTime1);
            addClinicItem.setToTime1(toTime1);
            addClinicItem.setFromTime2(fromTime2);
            addClinicItem.setToTime2(toTime2);
            addClinicItem.setFromTime3(fromTime3);
            addClinicItem.setToTime3(toTime3);
            addClinicItem.setDay1(Msun);
            addClinicItem.setDay2(Mmon);
            addClinicItem.setDay3(Mtue);
            addClinicItem.setDay4(Mwed);
            addClinicItem.setDay5(Mthu);
            addClinicItem.setDay6(Mfri);
            addClinicItem.setDay7(Msat);

            addClinicItem.setDay8(Asun);
            addClinicItem.setDay9(Amon);
            addClinicItem.setDay10(Atue);
            addClinicItem.setDay11(Awed);
            addClinicItem.setDay12(Athu);
            addClinicItem.setDay13(Afri);
            addClinicItem.setDay14(Asat);

            addClinicItem.setDay15(Esun);
            addClinicItem.setDay16(Emon);
            addClinicItem.setDay17(Etue);
            addClinicItem.setDay18(Ewed);
            addClinicItem.setDay19(Ethu);
            addClinicItem.setDay20(Efri);
            addClinicItem.setDay21(Esat);
            return POST(params,addClinicItem);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            progressDialog.dismiss();
            super.onPostExecute(jsonObject);

            Intent in = new Intent(AddClinicActivity.this, MyClinicsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(AddClinicActivity.this,"Clinic Added Successfully",Toast.LENGTH_SHORT).show();
            startActivity(in);

        }
    }


    // Check Validation before login
    private void checkValidation() {
        clinicName = etClinicName.getText().toString();
        primeryNo = etPrimeryNo.getText().toString();
        secondaryNo = etSecondaryNo.getText().toString();
        counsultaionFee = etCounsultationFee.getText().toString();
        premiumFee = etPremiumFee.getText().toString();
        password=etPassword.getText().toString();
        doorNo=etDoorNo.getText().toString();
        landmark=etLandMark.getText().toString();
        pincode=etPincode.getText().toString();

        //spinners
        /*cityId = citySpinner.getSelectedItem().toString();
        areaId = areaSpinner.getSelectedItem().toString();*/
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
        if (clinicName.equals("")  || primeryNo.equals("") || secondaryNo.equals("")  || counsultaionFee.equals("")  || timeSlot.equals("") ||
                fromTime1.equals("") || toTime1.equals("") || password.equals("") || doorNo.equals("") || pincode.equals("")) {
//            validationLayout.startAnimation(shakeAnimation);
            Toast.makeText(AddClinicActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
        }
        else if (Msun.equals("") && Mmon.equals("") && Mtue.equals("") && Mwed.equals("") && Mthu.equals("") && Mfri.equals("") && Msat.equals("")){

            Toast.makeText(AddClinicActivity.this,"Please Select Availability Days",Toast.LENGTH_LONG).show();
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
        else if (pincode.isEmpty() || pincode.length()<=6){
            etPincode.setError("please enter Valid Pincode");
            return;
        }


        else {
            new Asyncclass().execute();
        }

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
                Intent intent =new Intent(AddClinicActivity.this, MyClinicsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

