package com.kputsoftware.mileagecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kputsoftware.mileagecalculator.common.splashscreen;
import com.kputsoftware.mileagecalculator.library.BackupAndRestore;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;
import com.kputsoftware.mileagecalculator.library.method;


import java.util.List;

import static com.kputsoftware.mileagecalculator.common.splashscreen.updateResources;
import static com.kputsoftware.mileagecalculator.library.variables.*;

/**
 * Created by duraiswa on 7/8/2017.
 */

public class Setting extends AppCompatActivity  {

    EditText ecurrency;
    ToggleButton btnnotification;
    ToggleButton btntipsntoti;
    Button btnsave;
    Spinner sfuelmetric,slengthmetric,slanguage;

    DatabaseHandler db = new DatabaseHandler(this);
    method t = new method();
    Intent i;


    String currency;
    String  notification;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ecurrency = (EditText) findViewById(R.id.currency);
        btnnotification = (ToggleButton) findViewById(R.id.notification);
        btntipsntoti = (ToggleButton)findViewById(R.id.tipsnoti);
        btnsave = (Button) findViewById(R.id.save);
        sfuelmetric = (Spinner)findViewById(R.id.fuelmetric);
        slengthmetric = (Spinner)findViewById(R.id.lengthmetric);
        slanguage = (Spinner)findViewById(R.id.language);


        sfuelmetric.setSelection(getSharedPreferences(MC,0).getInt(FUELMETRIC,0));
        slengthmetric.setSelection(getSharedPreferences(MC,0).getInt(LENGTHMETRIC,0));
        slanguage.setSelection(getSharedPreferences(MC,0).getInt(LANGUAGE,0));



/*
        ecurrency.setText(db.getcurrency());
        if(getSharedPreferences(MC,0).getString(NOTIFICATION,TRUE).equals(TRUE))
              //  if (db.getnotification().equals("true"))
        {
            btnnotification.setChecked(true);
        }else
        {
            btnnotification.setChecked(false);

        }
*/

        ecurrency.setText(getSharedPreferences(MC,0).getString(CURRENCY,null));
        Boolean value = Boolean.parseBoolean( getSharedPreferences(MC,0).getString(TIPSNOTI,TRUE));
        btntipsntoti.setChecked(value);

        btnnotification.setChecked(Boolean.parseBoolean(getSharedPreferences(MC,0).getString(NOTIFICATION,TRUE)));

        btnnotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    btntipsntoti.setChecked(true);

                } else {
                    // The toggle is disabled
                    btntipsntoti.setChecked(false);
                }
            }
        });

        //onclick save


        btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             //   validator.validate();

                if (ecurrency.length() > 0 && ecurrency.length() < 6) {
                    currency = ecurrency.getText().toString();
                    getSharedPreferences(MC,0).edit().putString(CURRENCY,currency).commit();
                }else {
                    t.cltoast(getApplicationContext(), "Country Currency  - Max 5 Letters  allowed");
                    return;
                }

                if(btnnotification.isChecked()) {
                    notification = TRUE;
                    getSharedPreferences(MC,0).edit().putString(NOTIFICATION,TRUE).commit();
                }else
                {
                    getSharedPreferences(MC,0).edit().putString(NOTIFICATION,FALSE).commit();
                    notification = FALSE;
                }


                if(btntipsntoti.isChecked()) {
                    getSharedPreferences(MC,0).edit().putString(TIPSNOTI,TRUE).commit();
                    FirebaseMessaging.getInstance().subscribeToTopic(TIPS);
                }else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(TIPS);
                    getSharedPreferences(MC,0).edit().putString(TIPSNOTI,FALSE).commit();
                }

                int  fuelmetric = sfuelmetric.getSelectedItemPosition();
                int  lengthmetric = slengthmetric.getSelectedItemPosition();
                int language = slanguage.getSelectedItemPosition();


                getSharedPreferences(MC,0).edit().putInt(FUELMETRIC,fuelmetric).commit();
                getSharedPreferences(MC,0).edit().putInt(LENGTHMETRIC,lengthmetric).commit();
                getSharedPreferences(MC,0).edit().putInt(LANGUAGE,language).commit();

             //   db.updatesetting(currency,notification);
                t.btoast(getApplicationContext(),"Setting Saved");
                setResult(100, i);
                update();
                finish();


                }
            });
    }

    private void backup() {


    }

    private void update() {

        int language = getSharedPreferences(MC,0).getInt(LANGUAGE,0);
        String langu = "en";
        if (language == 1){
            updateResources(Setting.this,"ta");
        }else            {
            updateResources(Setting.this,"en");
        }

    }




}
