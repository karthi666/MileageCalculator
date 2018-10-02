package com.kputsoftware.mileagecalculator;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kputsoftware.mileagecalculator.common.Affiliate;
import com.kputsoftware.mileagecalculator.common.Backupandrestore;
import com.kputsoftware.mileagecalculator.common.Howtouse;
import com.kputsoftware.mileagecalculator.common.about;
import com.kputsoftware.mileagecalculator.common.help;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;
import com.kputsoftware.mileagecalculator.library.JSONParser;
import com.kputsoftware.mileagecalculator.library.method;

import static com.kputsoftware.mileagecalculator.firebase.Firebase_database_Path_and_Query.FBP;
import static com.kputsoftware.mileagecalculator.firebase.Firebase_database_Path_and_Query.QUERYADS;
import static com.kputsoftware.mileagecalculator.library.variables.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AdView adview;


    //this is for add:
    String uid,price,product,buybutton,detail,productlink,imagelink;
    LinearLayout laads;
    ImageView imagebuy;
    ImageView productimage;
    TextView tproduct,tproductdetail, tprice;
    Bitmap pimage;
    Bitmap bimage;


    DatabaseHandler db = new DatabaseHandler(this);
    JSONParser jsonParser = new JSONParser();
    int currentversion= BuildConfig.VERSION_CODE;
    String currency = "Rs";
    String country;
    final Context context = this;
    int calcount = 0;
    String notif ;



    EditText efuelprice;
    private  EditText etravelkilometer;
    EditText efuelused;
    EditText emileage;
    EditText etotalprice;
    Button btncalculate;
    Button btnsave;
    Button btnclear;
    Button btnrefill;
    Button btnaffiliate;
    TextView tvalue1;
    TextView tvalue2;
    TextView tvalue3;

    //normal for hide
    TextView tfuelprice,ttravelkm,ttotalfuelused,tvechiclemileage,ttotalfuelprice;
    TextView cfuelprice,ctravelkm,ctotalfuelused,cvechiclemileage,ctotalfuelprice;
    TextView vfuelprice,vtravelkm,vtotalfuelused,vvechiclemileage,vtotalfuelprice;



    double fuelprice;
    double travelkilometer;
    double fuelused;
    double mileage;
    double totalprice;
    double perkm;

    String ffuelprice;
    String ftravelkilometer;
    String ffuelused;
    String fmileage;
    String ftotalprice;

    String fandl;
    String fmetric,lmetric;
    int fuelmetric,lengthmetric;

    //localvariables
    TextView field;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        t.updateclick("totalopen");
        queryadsyesorno();
        displayads();


        TelephonyManager tm = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        country = tm.getNetworkCountryIso();
        getSharedPreferences(MC,0).edit().putString("country",country).commit();

        //this if for add:
        //locating the value
        laads = (LinearLayout)findViewById(R.id.aads);
        imagebuy = (ImageView) findViewById(R.id.imagebuy);
        productimage= (ImageView) findViewById(R.id.pimage);
        tproduct = (TextView)findViewById(R.id.product);
        tproductdetail = (TextView)findViewById(R.id.productdetail);
        tprice = (TextView)findViewById(R.id.price);
        laads.setVisibility(View.GONE);
        tproduct.setSelected(true);
        new loadadd().execute();
        laads.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                t.updateclick("customaddclicked");
                String plink = getSharedPreferences(MC,0).getString("productlink","http://amzn.to/2xNzyD0");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(plink));
                startActivity(i);
            }
        });





        efuelprice = (EditText) findViewById(R.id.fuelprice);
        etravelkilometer = (EditText) findViewById(R.id.travelkilometer);
        efuelused  = (EditText) findViewById(R.id.fuelused);
        emileage = (EditText) findViewById(R.id.mileage);
        etotalprice  = (EditText) findViewById(R.id.totalprice);
        btncalculate = (Button) findViewById(R.id.calculate);
        btnsave = (Button) findViewById(R.id.save);
        btnclear = (Button) findViewById(R.id.clear);
        btnrefill = (Button) findViewById(R.id.refill);
        btnaffiliate = (Button)findViewById(R.id.affiliate);
        tvalue1 = (TextView)findViewById(R.id.value1);
        tvalue2 = (TextView)findViewById(R.id.value2);
        tvalue3 = (TextView)findViewById(R.id.value3);


        tfuelprice = (TextView)findViewById(R.id.tfuelprice);
        ttravelkm = (TextView)findViewById(R.id.ttravelkm);
        ttotalfuelused = (TextView)findViewById(R.id.ttotalfuelused);
        tvechiclemileage = (TextView)findViewById(R.id.tvechiclemileage);
        ttotalfuelprice = (TextView)findViewById(R.id.ttotalfuelprice);

        cfuelprice = (TextView)findViewById(R.id.cfuelprice);
        ctravelkm = (TextView)findViewById(R.id.ctravelkm);
        ctotalfuelused = (TextView)findViewById(R.id.ctotalfuelused);
        cvechiclemileage = (TextView)findViewById(R.id.cvechiclemileage);
        ctotalfuelprice = (TextView)findViewById(R.id.ctotalfuelprice);

        vfuelprice = (TextView)findViewById(R.id.vfuelprice);
        vtravelkm = (TextView)findViewById(R.id.vtravelkm);
        vtotalfuelused = (TextView)findViewById(R.id.vtotalfuelused);
        vvechiclemileage = (TextView)findViewById(R.id.vvechiclemileage);
        vtotalfuelprice = (TextView)findViewById(R.id.vtotalfuelprice);



        fieldinvisible();

        fuelmetric =  getSharedPreferences(MC,0).getInt(FUELMETRIC,0);
        lengthmetric = getSharedPreferences(MC,0).getInt(LENGTHMETRIC,0);
        updatemetric();

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        //   btnaffiliate.startAnimation(animation);
        btnaffiliate.setVisibility(View.INVISIBLE);


        //onclick btnaffiliate
        btnaffiliate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean nccheck = method.nccheck();
                if (nccheck) {
                    t.ctoast(getApplicationContext(), t.ncmsg());
                    return;
                }
                Intent i = new Intent(MainActivity.this, Affiliate_listview.class);
                startActivity(i);
            }});


        //onclick save
        btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                String date = format.format(new Date());
                String fuelprice;
                String travelkilometer ;
                String fuelused;
                String mileage;
                String totalprice;

                if (efuelprice.length() >0){
                    fuelprice = df.format(fuelprice());
                }else
                {
                    fuelprice = ffuelprice;
                }

                if (etravelkilometer.length() >0){
                    travelkilometer = df.format(Double.valueOf(etravelkilometer.getText().toString()));

                }else
                {

                    travelkilometer = ftravelkilometer;

                }

                if (efuelused.length() >0){
                    fuelused = df.format(Double.valueOf(efuelused.getText().toString()));
                }else
                {
                    fuelused = ffuelused;
                }

                if (emileage.length() >0){
                    mileage = df.format(mileage());
                }else
                {
                    mileage = fmileage;
                }
                if (etotalprice.length() >0){
                    totalprice = df.format(totalprice());
                }else
                {
                    totalprice = ftotalprice;
                }

                db.saveinfo(date,currency,fuelprice,travelkilometer,fuelused,mileage,totalprice,fmetric,lmetric,fandl);
                t.ctoast(getApplicationContext(),"Calculation Saved");
                btnsave.setText("Saved ");
                btnsave.setClickable(false);
                final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);
                sharedpref.edit().putInt(HISTORYCOUNT,sharedpref.getInt(HISTORYCOUNT,0) +1 ).commit();

            }});

        //onclick save
        btnclear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //  Intent intent = getIntent();      finish();            startActivity(intent);
                cleartext();
                clearcolor();
                //fieldinvisible();
                btnsave.setText("SAVE");
                btnsave.setClickable(true);
                btnsave.setVisibility(View.GONE);
                btnclear.setVisibility(View.GONE);
                visibleall();
            }});

        //onclick calculate
        btncalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                calcount = getSharedPreferences(MC,0).getInt(CALCOUNT,0);
                calcount = calcount + 1;
                getSharedPreferences(MC,0).edit().putInt(CALCOUNT,calcount).commit();
                //   db.updatecalcount(calcount);
                clearkeyboard();
                clearcolor();
                //  fieldinvisible();
                calculate();
                nullredcolor();




                new updatedb().execute();
                t.updateclick("calculation");



            }


        });

        //onclick to get travel km from meter reading
        ttravelkm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gettravelkm();

            }});

        //onclick refill detail
        btnrefill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(getApplicationContext(), Fuel_refill.class));

            }});


/*
        findViewById(R.id.onlinedeals).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(getApplicationContext(), demo.class));

            }});
*/


//        if(country.equals("in") && !getSharedPreferences(MC,0).getString(AFFILIATEURL,"empty").equals("empty"))
        if(country.equals("in") )
        {
            btnaffiliate.setVisibility(View.INVISIBLE);
        }

        currencyupdate(country);




/*
//        adsenable = "no";
//        db.updateadsenable(adsenable);

     //   displaynote();
        final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);
        if  (db.appsettingcheck() != 0) {
            calcount = db.getcallcount();
            if (calcount < 10) {
                if (sharedpref.getInt(FIRSTOPEN,1) == 0) {
                      displaynote();
                  //  db.updatefirstopen(1);
                    sharedpref.edit().putInt(FIRSTOPEN,1).commit();
                }
            }
        }else
        {
            displaynote();
        }*/

     //   if (getSharedPreferences(MC,0).getInt(CALCOUNT,0) < 2){

            if (getSharedPreferences(MC,0).getInt(FIRSTOPEN,0) == 0) {
                getSharedPreferences(MC,0).edit().putInt(FIRSTOPEN,1).commit();

               // displaynote();
                Intent howtouse = new Intent(getApplicationContext(), Howtouse.class);
                startActivity(howtouse);

                //  db.updatefirstopen(1);

            }
       // }

    }

    private void updatemetric() {


         fmetric ="Ltr";
         lmetric ="KM";
        String f="L" ;
        String l="KM";

        if (fuelmetric ==1){
            fmetric = "Gal";
            f = "G";
        }else if(fuelmetric==2){
            fmetric = "ImpG";
            f = "I";
        }
        if (lengthmetric ==1){
            lmetric = "Mile";
            l = "M";
        }else if(lengthmetric==2){
            lmetric = "Nuti";
            l = "N";        }

        fandl = l+"/"+f;
        vtravelkm.setText(lmetric);
        vtotalfuelused.setText(fmetric);
        vvechiclemileage.setText(fandl);
        ttravelkm.setText("Travel "+lmetric);

    }


    private void displaynote() {
        TextView note =(TextView)findViewById(R.id.tnote);
        note.setVisibility(View.GONE);
        findViewById(R.id.ttravelnote).setVisibility(View.VISIBLE);


        t.cltoast( getApplicationContext(), " Enter Any Two/Three Value.  \n To get other values ");



    }


    private void currencyupdate(String country) {
        //to check currency is updated or not.
        if (!getSharedPreferences(MC,0).getString(CURRENCY,"empty").equals("empty"))
        //   if  (db.appsettingcheck() != 0) {
        {
            //   currency = db.getcurrency();
            currency =  getSharedPreferences(MC,0).getString(CURRENCY,null);
            vfuelprice.setText(currency);
            vtotalfuelprice.setText(currency);
            notif = getSharedPreferences(MC,0).getString(NOTIFICATION,TRUE);
            //   notif = db.getnotification();
            new updatedb().execute();

        }else  if(country.equals("in")) {
            currency = "RS";
            //   db.addsetting(currency, "true");
            getSharedPreferences(MC,0).edit().putString(CURRENCY,currency).commit();

            vfuelprice.setText(currency);
            vtotalfuelprice.setText(currency);
            new updatedb().execute();

        } else if(country.equals("my")) {
            currency = "MYR";
            getSharedPreferences(MC,0).edit().putString(CURRENCY,currency).commit();
            //  db.addsetting(currency, "true");
            vfuelprice.setText(currency);
            vtotalfuelprice.setText(currency);
            new updatedb().execute();
        }else if(country.equals("us")) {
            currency = "USD";
            getSharedPreferences(MC,0).edit().putString(CURRENCY,currency).commit();
            //db.addsetting(currency, "true");
            vfuelprice.setText(currency);
            vtotalfuelprice.setText(currency);
            new updatedb().execute();
        }
        else{

            askcurrency();
        }


    }

    private void gettravelkm() {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.travel_kilometer, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        String l = "KM";
        if (lengthmetric ==1){
            lmetric = "Mile";
            l = "M";
        }else if(lengthmetric==2){
            lmetric = "Nuti";
            l = "N";        }

        final TextView tstartkm = (TextView) promptsView.findViewById(R.id.tstartkm);
        final TextView tendkm = (TextView) promptsView.findViewById(R.id.tendkm);
        tstartkm.setText("Start "+lmetric);
        tendkm.setText("End "+lmetric);


        final EditText startkm = (EditText) promptsView.findViewById(R.id.startkm);
        final EditText endkm = (EditText) promptsView.findViewById(R.id.endkm);


        final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);

        VALUE = sharedpref.getString(STARTKM, "");
        startkm.setText(VALUE);
        VALUE = sharedpref.getString(ENDKM,"");
        endkm.setText(VALUE);

        startkm.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                VALUE = startkm.getText().toString();
                sharedpref.edit().putString(STARTKM, VALUE).commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {



            }
        });
        endkm.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                VALUE = endkm.getText().toString();
                sharedpref.edit().putString(ENDKM, VALUE).commit();

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                if (startkm.length() > 0 && endkm.length() > 0) {
                                    int start = Integer.parseInt(startkm.getText().toString());
                                    int end = Integer.parseInt(endkm.getText().toString());
                                    if (start > end) {
                                        t.cltoast(getApplicationContext(), "End KM is should be higher than Start KM.");
                                        gettravelkm();

                                    } else {
                                        int travel = end - start;
                                        t.bltoast(getApplicationContext(), "Travel Kilometer  =  " + travel);

                                        etravelkilometer.setText(Integer.toString(travel));
                                    }
                                    //   result.setText(userInput.getText());
                                }else{
                                    t.ctoast(getApplicationContext()," Enter The Value .");
                                    gettravelkm();
                                }
                            }
                        })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }}

    private void askcurrency() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.get_currency, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText ecurrency = (EditText) promptsView.findViewById(R.id.currency);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                if (ecurrency.length() > 0 && ecurrency.length() < 6) {
                                    currency = ecurrency.getText().toString();
                                    //     db.addsetting(currency,"true");
                                    getSharedPreferences(MC,0).edit().putString(CURRENCY,currency).commit();
                                    vfuelprice.setText(currency);
                                    vtotalfuelprice.setText(currency);
                                    new updatedb().execute();
                                }else
                                {
                                    t.cltoast(getApplicationContext(),"Enter your Country Currency Code - Max 5 Letters  allowed");
                                    askcurrency();
                                }

                                //   result.setText(userInput.getText());
                            }
                        });

        /**       .setNegativeButton("Cancel",
         new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog,int id) {
         dialog.cancel();
         }
         });
         */

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    class updatedb extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {



            final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);
            SystemClock.sleep(2000);
            String token = FirebaseInstanceId.getInstance().getToken();
            String manufacturer = Build.MANUFACTURER;
            String brand        = Build.BRAND;
            String product      = Build.PRODUCT;
            String model        = Build.MODEL;
            String serialnumber = Build.SERIAL;
            int androidversion = android.os.Build.VERSION.SDK_INT;;
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            double dpi = getResources().getDisplayMetrics().density;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("currency", currency));
            params.add(new BasicNameValuePair("country", country));
            params.add(new BasicNameValuePair("notification", notif));
            params.add(new BasicNameValuePair("manufacturer", manufacturer));
            params.add(new BasicNameValuePair("brand", brand));
            params.add(new BasicNameValuePair("product", product));
            params.add(new BasicNameValuePair("model", model));
            params.add(new BasicNameValuePair("SERIALNUMBER", serialnumber));
            params.add(new BasicNameValuePair("androidversion", Integer.toString(androidversion) ));
            params.add(new BasicNameValuePair("width",String.valueOf(width)));
            params.add(new BasicNameValuePair("height", String.valueOf(height)));
            params.add(new BasicNameValuePair("dpi", String.valueOf(dpi)));
            params.add(new BasicNameValuePair("opencount", String.valueOf(getSharedPreferences(MC,0).getInt("opencount",1))));
            params.add(new BasicNameValuePair("calcount", Integer.toString(calcount)));
            params.add(new BasicNameValuePair("historycount", Integer.toString(sharedpref.getInt(HISTORYCOUNT,0))));
            params.add(new BasicNameValuePair("currentversion", String.valueOf(currentversion)));
            params.add(new BasicNameValuePair("numberoflaunch", String.valueOf(getSharedPreferences(MC,0).getInt("NOOFLAUNCH",0))));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

      //      adsenable = "no";

            try {

              //  adsenable = json.getString("appads");
                String affiliateu = json.getString("affiliateurl");
                getSharedPreferences(MC,0).edit().putString(AFFILIATEURL,affiliateu).commit();


            } catch (JSONException e) {
                JSONObject json2 = jsonParser.makeHttpRequest(url, "POST", params);

            }
            return null;
        }
        protected void onPostExecute(String result) {
            final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);

            if(country.equals("in") && !getSharedPreferences(MC,0).getString(AFFILIATEURL,"empty").equals("empty"))
            {
                btnaffiliate.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void nullredcolor() {

        int a = efuelprice.getCurrentTextColor();
        int b = etravelkilometer.getCurrentTextColor();
        int c = efuelused.getCurrentTextColor();
        int d = emileage.getCurrentTextColor();
        int e = etotalprice.getCurrentTextColor();
        int v = -65536 ;

        if (a == v){
            efuelprice.setText(null);
        }
        if (b == v){
            etravelkilometer.setText(null);
        }
        if (c == v){
            efuelused.setText(null);
        }
        if (d == v){
            emileage.setText(null);
        }
        if (e == v){
            etotalprice.setText(null);
        }
        String aa = String.valueOf(efuelprice.getText());
        String bb = String.valueOf(etravelkilometer.getText());
        String cc = String.valueOf(efuelused.getText());
        String dd = String.valueOf(emileage.getText());
        String ee  = String.valueOf(etotalprice.getText());

        btnsave.setText("SAVE ");
        btnsave.setClickable(true);
    }

    private void visibleall() {
        tfuelprice.setVisibility(View.VISIBLE);
        ttravelkm.setVisibility(View.VISIBLE);
        ttotalfuelused.setVisibility(View.VISIBLE);
        tvechiclemileage.setVisibility(View.VISIBLE);
        ttotalfuelprice.setVisibility(View.VISIBLE);

        cfuelprice.setVisibility(View.VISIBLE);
        ctravelkm.setVisibility(View.VISIBLE);
        ctotalfuelused.setVisibility(View.VISIBLE);
        cvechiclemileage.setVisibility(View.VISIBLE);
        ctotalfuelprice.setVisibility(View.VISIBLE);

        efuelprice.setVisibility(View.VISIBLE);
        etravelkilometer.setVisibility(View.VISIBLE);
        efuelused.setVisibility(View.VISIBLE);
        emileage.setVisibility(View.VISIBLE);
        etotalprice.setVisibility(View.VISIBLE);

        vfuelprice.setVisibility(View.VISIBLE);
        vtravelkm.setVisibility(View.VISIBLE);
        vtotalfuelused.setVisibility(View.VISIBLE);
        vvechiclemileage.setVisibility(View.VISIBLE);
        vtotalfuelprice.setVisibility(View.VISIBLE);

    }

    //this is to make the fieldinvisible
    private void fieldinvisible() {
        btnclear.setVisibility(View.GONE);
        tvalue1.setVisibility(View.GONE);
        tvalue2.setVisibility(View.GONE);
        tvalue3.setVisibility(View.GONE);
        btnsave.setVisibility(View.GONE);

    }
    //this is to clear text color
    private void clearcolor() {
        btnclear.setVisibility(View.VISIBLE);
        efuelprice.setTextColor(Color.BLACK);
        etravelkilometer.setTextColor(Color.BLACK);
        efuelused.setTextColor(Color.BLACK);
        emileage.setTextColor(Color.BLACK);
        etotalprice.setTextColor(Color.BLACK);

    }
    //this is to clear text
    private void cleartext() {
        efuelprice.setText(null);
        etravelkilometer.setText(null);
        efuelused.setText(null);
        emileage.setText(null);
        etotalprice.setText(null);
    }

    //this to get value from ui
    private double fuelprice() {
        double  value = Double.valueOf(efuelprice.getText().toString());
        return value;
    }
    private double travelkilometer(){
        double  value = Double.valueOf(etravelkilometer.getText().toString());
        if (lengthmetric == 1){
            value  = value * 1.60934;
        }else if(lengthmetric == 2){
            value = value * 1.852;
        }
        return value;
    }
    private double fuelused(){
        double  value = Double.valueOf(efuelused.getText().toString());

        if (fuelmetric == 1){
            value  = value * 3.78541;
        }else if(fuelmetric == 2){
            value = value * 4.54609;
        }
        return value;
    }
    private double mileage(){
        double  value = Double.valueOf(emileage.getText().toString());
        return value;
    }
    private double totalprice(){
        double  value = Double.valueOf(etotalprice.getText().toString());
        return value;
    }



    //to calculate and  set and display value
    private void cfuelprice(){
        fuelprice = totalprice() / fuelused();
        if (fuelmetric == 1){
            fuelprice  = totalprice * 3.78541;
        }else if(fuelmetric == 2){
            fuelprice = totalprice * 4.54609;
        }
        efuelprice.setText(String.format ("%.2f", fuelprice));
        efuelprice.setTextColor(Color.RED);
        fc().setText("Fuel price = " + String.format ("%.2f", fuelprice) + " " + currency);

        ffuelprice = String.format ("%.2f", fuelprice);
        efuelprice.setVisibility(View.GONE);
        tfuelprice.setVisibility(View.GONE);
        cfuelprice.setVisibility(View.GONE);
        vfuelprice.setVisibility(View.GONE);
    }
    private void ctravelkilometer(){
        travelkilometer = fuelused() * mileage();
        if (fuelmetric == 1){
            travelkilometer  = travelkilometer / 3.78541;
        }else if(fuelmetric == 2){
            travelkilometer = travelkilometer / 4.54609;
        }
        etravelkilometer.setText(String.format ("%.2f", travelkilometer));
        etravelkilometer.setTextColor(Color.RED);
        fc().setText("Total Kilometer = " + String.format ("%.2f", travelkilometer) + " "+lmetric);

        ftravelkilometer = String.format ("%.2f", travelkilometer);
        etravelkilometer.setVisibility(View.GONE);
        ttravelkm.setVisibility(View.GONE);
        ctravelkm.setVisibility(View.GONE);
        vtravelkm.setVisibility(View.GONE);

    }
    private void cfuelused(){

        if (etravelkilometer.length() > 0 && emileage.length() > 0) {
            fuelused = travelkilometer() / mileage();

            if (lengthmetric == 1){
                fuelused  = fuelused / 1.60934;
            }else if(lengthmetric == 2){
                fuelused = fuelused / 1.852;
            }

        }else
        {
            fuelused = totalprice()/fuelprice();
        }
        efuelused.setText(String.format ("%.2f", fuelused));
        efuelused.setTextColor(Color.RED);
        fc().setText(" Fuel Used = " + String.format ("%.2f", fuelused) + " "+fmetric);

        ffuelused =  String.format ("%.2f", fuelused);
        efuelused.setVisibility(View.GONE);
        ttotalfuelused.setVisibility(View.GONE);
        ctotalfuelused.setVisibility(View.GONE);
        vtotalfuelused.setVisibility(View.GONE);
    }
    private void cmileage() {
        mileage = travelkilometer() / fuelused();
            double f = 1,l=1;

        if (lengthmetric == 1){;
            l = 1.60934;
        }else if(lengthmetric == 2){
            l= 1.852;
        }

        if (fuelmetric == 1){
            f  = 3.78541;
        }else if(fuelmetric == 2){
            f =  4.54609;
        }
        mileage = mileage*(f/l);
        emileage.setText(String.format ("%.2f", mileage));
        emileage.setTextColor(Color.RED);
        fc().setText(" Mileage = " + String.format ("%.2f", mileage) + " "+fandl);

        fmileage = String.format ("%.2f", mileage);
        emileage.setVisibility(View.GONE);
        tvechiclemileage.setVisibility(View.GONE);
        cvechiclemileage.setVisibility(View.GONE);
        vvechiclemileage.setVisibility(View.GONE);
    }
    private void ctotalprice(){
        totalprice = fuelprice() * fuelused();
        if (fuelmetric == 1){
            totalprice  = totalprice / 3.78541;
        }else if(fuelmetric == 2){
            totalprice = totalprice / 4.54609;
        }
        etotalprice.setText(String.format ("%.2f", totalprice));
        etotalprice.setTextColor(Color.RED);
        fc().setText("Total fuel price = " + String.format ("%.2f", totalprice) +" " + currency);

        ftotalprice = String.format ("%.2f", totalprice);
        etotalprice.setVisibility(View.GONE);
        ttotalfuelprice.setVisibility(View.GONE);
        ctotalfuelprice.setVisibility(View.GONE);
        vtotalfuelprice.setVisibility(View.GONE);
    }
    private void perkm() {


        final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);
        sharedpref.edit().remove(STARTKM).commit();
        sharedpref.edit().remove(ENDKM).commit();
        perkm = fuelprice() / mileage();
        tvalue3.setText("1 "+lmetric+" = " + String.format ("%.2f", perkm)+ " " + currency);
        tvalue3.setVisibility(View.VISIBLE);
        t.ctoast(getApplicationContext(),"Calculation Done");
    }

    //to check which field empty
    private TextView fc() {
        if (tvalue1.length() > 0) {
            field = tvalue2;
            perkm();
    //        t.ctoast(getApplicationContext(),"Calculation Done");

        } else {
            field = tvalue1;
    //        t.ctoast(getApplicationContext(),"Calculation Done");
        }
        field.setVisibility(View.VISIBLE);
        btnsave.setVisibility(View.VISIBLE);

        return field;
    }
    //this to close the keyboard
    private void clearkeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        tvalue1.setText(null);
        tvalue2.setText(null);
        tvalue3.setText(null);

        tvalue1.setVisibility(View.GONE);
        tvalue2.setVisibility(View.GONE);
        tvalue3.setVisibility(View.GONE);
    }
    private void pricezero() {
        efuelprice.setText("0");
        etotalprice.setText("0");
    }

    //main progaram
    private void calculate() {
        if (efuelprice.length() > 0 && etravelkilometer.length() > 0 && efuelused.length() > 0) { //scenario 1
            ctotalprice();
            cmileage();

        } else if (efuelprice.length() > 0 && etravelkilometer.length() > 0 && emileage.length() > 0) { //scenario 2
            cfuelused();
            ctotalprice();

        } else if (efuelprice.length() > 0 && emileage.length() > 0 && etotalprice.length() > 0) { //scenario 3
            cfuelused();
            ctravelkilometer();

        } else if (efuelprice.length() > 0 && efuelused.length() > 0 && etotalprice.length() > 0) { //scenario 4

            //wont work toast
            t.ctoast(getApplicationContext(),"Enter Travel KM or Mileage");

        } else if (efuelprice.length() > 0 && etravelkilometer.length() > 0 && emileage.length() > 0) { //scenario 5
            cfuelused();
            ctotalprice();


        } else if (efuelprice.length() > 0 && efuelused.length() > 0 && emileage.length() > 0) { //scenario 6
            ctravelkilometer();
            ctotalprice();


        } else if (etravelkilometer.length() > 0 && efuelused.length() > 0 && emileage.length() > 0) { //scenario 7
            //wont work

            if (etotalprice.length() >0){
                cfuelprice();
            }else
            {
                t.ctoast(getApplicationContext(),"Enter Fuel price or Total Fuel price");
            }

        } else if (etravelkilometer.length() > 0 && efuelused.length() > 0 && etotalprice.length() > 0) { //scenario 8
            cfuelprice();
            cmileage();


        } else if (etravelkilometer.length() > 0 && emileage.length() > 0 && etotalprice.length() > 0) { //scenario 9
            cfuelused();
            cfuelprice();

        } else if (efuelused.length() > 0 && emileage.length() > 0 && etotalprice.length() > 0) { //scenario 10
            cfuelprice();
            ctravelkilometer();

        } else if (efuelprice.length() > 0 && etravelkilometer.length() > 0 && etotalprice.length() > 0) { //scenario 11
            cfuelused();
            cmileage();

        }else if (etravelkilometer.length() > 0 && efuelused.length() > 0 ) { //scenario 12
            cmileage();
            pricezero();
        }  else if (etravelkilometer.length() > 0 && emileage.length() > 0 ) { //scenario 13
            cfuelused();
            pricezero();
        }else if (efuelused.length() > 0 && emileage.length() > 0 ) { //scenario 14
            ctravelkilometer();
            pricezero();

        } else{
            t.ctoast(getApplicationContext(),"Enter any three Value");

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.history:
                Intent history = new Intent(getApplicationContext(), Calculation_history.class);
                startActivity(history);
                return true;

            case R.id.refillhistory:
                Intent refillhistory = new Intent(getApplicationContext(), Refill_history.class);
                startActivity(refillhistory);
                return true;

            case R.id.backupandrestore:
                Intent backupandrestore = new Intent(getApplicationContext(), Backupandrestore.class);
                startActivity(backupandrestore);
                return true;

            case R.id.setting:
                Intent setting = new Intent(getApplicationContext(), Setting.class);
                startActivityForResult(setting, 100);
                return true;

            case R.id.about:
                startActivity(new Intent(getApplicationContext(), about.class));
                return true;

            case R.id.share:
                t.updateclick("selfshare");
                share();
                return true;

            case R.id.moreapps:
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(kputsoftwareapps));
                    startActivity(i);
                } catch (Exception e) {
                    t.ctoast(getApplicationContext(),"Unable to Connect Try Again...");
                }
                return true;

            case R.id.rateus:
                t.updateclick("selfrate");
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appmarket));
                    getSharedPreferences(MC,0).edit().putString("RATED","yes").commit();
                    startActivity(i);
                } catch (Exception e) {
                    t.ctoast(getApplicationContext(),"Unable to Connect Try Again...");
                }
                return true;

            case R.id.help:

                startActivity(new Intent(getApplicationContext(), help.class));
                return true;

            case R.id.howtouse:

                startActivity(new Intent(getApplicationContext(), Howtouse.class));
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mileage Calculator");
        String shareBody = "\n Try this Mileage Calculator App. \n  It is Very Useful \n \n " ;
        shareBody = shareBody + urlmarket+ " \n\n";
        //sharingIntent.setPackage("com.whatsapp");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    //double back for close app
    private boolean doubleBackToExitPressedOnce;
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
            // return;
        }
        this.doubleBackToExitPressedOnce = true;

        if ( calcount > 10) {
            if(getSharedPreferences(MC,0).getInt("SHARED",0)<3) {
                opensharescreen();
                t.updateclick("sharescreen");
            }else   if (!getSharedPreferences(MC,0).getString("RATED","no").equals("yes")) {

                if ( calcount > 15) {
                    ratescreen();
                    t.updateclick("ratescreen");
                }
            }else   {
                Toast.makeText(MainActivity.this, " Please click Back again to exit", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(MainActivity.this, " Please click Back again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = true;
            }
        }, 2000);
    }

    private void opensharescreen() {
        int share = getSharedPreferences(MC,0).getInt("SHARED",0);
        getSharedPreferences(MC,0).edit().putInt("SHARED",share + 1).apply();

        android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialog2.setTitle("Thanks for Using");
        alertDialog2.setMessage(" Like to  Share this App with your Friends  ?");
        alertDialog2.setPositiveButton("Share now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        share();
                        t.updateclick("projectedshare");
                    }
                });

/*        // newtural
        alertDialog2.setNeutralButton("Rate App",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appmarket));
                            startActivity(i);
                        } catch (Exception e) {
                            t.ctoast(getApplicationContext(),"Unable to Connect Try Again...");
                        }
                    }
                });*/
        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("Later",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.cancel();

                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }


    private void ratescreen()    {
        android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialog2.setTitle("Rate the App");
        alertDialog2.setMessage(" We working very hard to give you the best free application. Your 5-Star review is worth more than all money.");
        alertDialog2.setPositiveButton("Rate Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences(MC,0).edit().putString("RATED","yes").apply();
                        t.updateclick("projectedrate");
                        try {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appmarket));
                            startActivity(i);
                        } catch (Exception e) {
                            t.ctoast(getApplicationContext(),"Unable to Connect Try Again...");
                        }
                    }
                });

/*        // newtural
        alertDialog2.setNeutralButton("Rate App",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appmarket));
                            startActivity(i);
                        } catch (Exception e) {
                            t.ctoast(getApplicationContext(),"Unable to Connect Try Again...");
                        }
                    }
                });*/
        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("Later",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.cancel();

                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }

    private class loadadd extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {



            int opencount = getSharedPreferences(MC,0).getInt("opencount",1);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("opencount", String.valueOf(opencount)));
            params.add(new BasicNameValuePair("country", country));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(adsurl, "POST", params);

            String success = "no";

            try {
                if (json.getInt("success") == 1 ){
                    uid=json.getString("uid");
                    price=json.getString("price");
                    product=json.getString("product");
                    buybutton=json.getString("buybutton");
                    detail=json.getString("detail");
                    productlink=json.getString("productlink");
                    imagelink=json.getString("imagelink");

                    try {
                        // Download Image from URL
                        InputStream pinput = new java.net.URL(imagelink).openStream();
                        // Decode Bitmap
                        pimage = BitmapFactory.decodeStream(pinput);

                        InputStream binput = new java.net.URL(buybutton).openStream();
                        bimage = BitmapFactory.decodeStream(binput);

                        success  = "yes";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("yes")) {
                tprice.setText(price);
                tproduct.setText(product);
                tproductdetail.setText(detail);
                productimage.setImageBitmap(pimage);
                imagebuy.setImageBitmap(bimage);
                getSharedPreferences(MC, 0).edit().putString("productlink", productlink).apply();
               /* int id = getResources().getIdentifier(getPackageName()+":drawable/" + buybutton, null, null);
                imagebuy.setImageResource(id);*/
                laads.setVisibility(View.VISIBLE);
            }
        }
    }

    //queryads yes or no
    private void queryadsyesorno() {
        //   FirebaseDatabase.getInstance().goOnline();
        QUERYADS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                String ads = String.valueOf(dataSnapshot.getValue(String.class));

             getSharedPreferences(MC,0).edit().putString(ADS,ads).apply();

             displayads();

                //         FirebaseDatabase.getInstance().goOffline();
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void displayads() {

        String adsenable  = getSharedPreferences(MC,0).getString(ADS,"no");


         if(adsenable.equals("yes")) {
            AdView adview = (AdView) findViewById(R.id.adview);
            AdRequest adRequest = new AdRequest.Builder().build();
            String c  = getSharedPreferences(MC,0).getString(CURRENCY,"RS");
          if(!BuildConfig.DEBUG && !c.equals("RSOP")) {
                adview.loadAd(adRequest);
            }
        }

    }
}
