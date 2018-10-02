package com.kputsoftware.mileagecalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.kputsoftware.mileagecalculator.library.variables.*;

/**
 * Created by duraiswa on 8/19/2017.
 */

public class Fuel_refill extends AppCompatActivity {

    DatabaseHandler db = new DatabaseHandler(this);

    static final int DATE_DIALOG_ID1 = 0;
    int cur =0;
    private int pYear;
    private int pMonth;
    private int pDay;

    EditText emeterreading;
    EditText etotalamount;
    EditText efuelprice;
    EditText efuelquantity;
    EditText edate;
    Button btnhistory;
    Button btnsave;

    String meterreading;
    String totalamount;
    String fuelprice;
    String fuelquantity;
    String currency;
    String date;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_refill);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TextView bartext = (TextView)findViewById(R.id.bartext);
        bartext.setText("Fuel log History");

        edate = (EditText) findViewById(R.id.date);
        emeterreading = (EditText) findViewById(R.id.meterreading);
        etotalamount = (EditText) findViewById(R.id.totalamount);
        efuelprice = (EditText) findViewById(R.id.fuelprice);
        efuelquantity = (EditText) findViewById(R.id.fuelquantity);
        btnhistory = (Button) findViewById(R.id.history);
        btnsave = (Button)findViewById(R.id.save);
        efuelprice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


               if (start < count){
                    nullify();
                }
            }

            private void nullify() {
                if (emeterreading.length() > 0 && etotalamount.length() > 0 && efuelprice.length() > 0) {
                    meterreading = emeterreading.getText().toString();
                    totalamount = df.format(Float.valueOf(etotalamount.getText().toString()));
                    fuelprice = df.format(Float.valueOf(efuelprice.getText().toString()));
                    fuelquantity = df.format(Float.valueOf(etotalamount.getText().toString()) / Float.valueOf(efuelprice.getText().toString()));
                }
                    efuelquantity.setText(null);
            }
        });

        efuelquantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
             //

            }

            public void nullify() {
                efuelprice.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(start<count){
                    nullify();
                }

            }
        });


        upcurdate();



        /** Listener for click event of the button */
        edate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID1);

            }
        });

       // SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        // final String date = format.format(new Date());

        //onclick save
        btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               // currency = db.getcurrency();
                currency  = getSharedPreferences(MC,0).getString(CURRENCY,null);
                date = sdate(edate.getText().toString());


                if (emeterreading.length() > 0 && etotalamount.length() > 0 && efuelprice.length() > 0) {
                    meterreading = emeterreading.getText().toString();
                    totalamount =  df.format(Float.valueOf(etotalamount.getText().toString()));
                    fuelprice = df.format(Float.valueOf(efuelprice.getText().toString()));
                    fuelquantity = df.format(Float.valueOf(etotalamount.getText().toString())  / Float.valueOf(efuelprice.getText().toString()));

                }else if(emeterreading.length() > 0 && etotalamount.length() > 0 && efuelquantity.length() > 0) {
                    meterreading = emeterreading.getText().toString();
                    totalamount =  df.format(Float.valueOf(etotalamount.getText().toString()));
                    fuelquantity = df.format(Float.valueOf(efuelquantity.getText().toString()));
                    fuelprice = df.format(Float.valueOf(etotalamount.getText().toString())  / Float.valueOf(efuelquantity.getText().toString()));


                }else
                {
                    t.bltoast(getApplicationContext()," Please Enter the Values.");

                    return;
                }


              /*  final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);
                sharedpref.edit().putString(STARTKM,meterreading).commit();*/
                db.addrefillhistory(date,meterreading,totalamount,fuelquantity,fuelprice,currency);
                t.ctoast(getApplicationContext(),"Calculation Saved");
                Intent history = new Intent(getApplicationContext(), Refill_history.class);
                startActivity(history);

            }
        });

        //onclick history
        btnhistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent history = new Intent(getApplicationContext(), Refill_history.class);
                startActivity(history);
            }});

        ads();
    }

    private String sdate(String sdate) {

        String inputPattern = "dd/M/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            Date newdate = inputFormat.parse(sdate);
            sdate = outputFormat.format(newdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return sdate;
    }

    private void ads() {
        final AdView adview = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.setVisibility(View.GONE);
        DatabaseHandler db = new DatabaseHandler(this);
        final SharedPreferences sharedpref= getSharedPreferences("mcpref", 0);

        String adsenable = sharedpref.getString(ADS,"no");
        if (adsenable.equals("yes")) {

            adview.loadAd(adRequest);
            adview.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    // Log.i("Ads", "onAdLoaded");
                    adview.setVisibility(View.VISIBLE);
                }

            });
        }
    }


    /** Create a new dialog for date picker */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID1:
                cur = DATE_DIALOG_ID1;
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);

        }

        return null;
    }
    /** this for picking the date.
     /** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    if (cur== DATE_DIALOG_ID1 )
                    {

                        displaydate();
                    }

                }
            };

    private void displaydate() {
        edate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("/")
                        .append(pMonth + 1).append("/")
                        .append(pYear).append(" "));
    }

    private void upcurdate() {
        /** Get the current date  */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        /** Display the current date in the TextView */
        //  updateDisplay();
        displaydate();
    }

}
