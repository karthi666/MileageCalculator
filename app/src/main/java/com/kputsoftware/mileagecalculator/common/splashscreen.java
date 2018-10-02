package com.kputsoftware.mileagecalculator.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kputsoftware.mileagecalculator.BuildConfig;
import com.kputsoftware.mileagecalculator.MainActivity;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;

import java.util.Locale;

import static com.kputsoftware.mileagecalculator.library.variables.*;

/**
 * Created by duraiswa on 5/16/2017.
 */

public class splashscreen extends AppCompatActivity {

//    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedpref= getSharedPreferences(MC, 0);

        getSharedPreferences(MC,0).edit().putInt("NOOFLAUNCH",getSharedPreferences(MC,0).getInt("NOOFLAUNCH",0)+1).commit();

        TelephonyManager tm = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
       String  country = tm.getNetworkCountryIso();



        if (getSharedPreferences(MC,0).getString("FIRSTTIMELAUCHINGAPP","true").equals("true")){
            FirebaseMessaging.getInstance().subscribeToTopic("ALLMCNOTIFICATION");
            FirebaseMessaging.getInstance().subscribeToTopic(TIPS);
            getSharedPreferences(MC,0).edit().putString("FIRSTTIMELAUCHINGAPP","false").commit();
            if(!country.equals(""))
            {
                FirebaseMessaging.getInstance().subscribeToTopic(country);

            }

        }

        if(getSharedPreferences(MC,0).getString(CURRENCY,"hai").equals("RSOP"))
        {
            FirebaseMessaging.getInstance().subscribeToTopic("rsop");
        }


        int language = getSharedPreferences(MC,0).getInt(LANGUAGE,0);
        String langu = "en";
        if (language == 1){
            updateResources(splashscreen.this,"ta");
        }

       sharedpref.edit().putInt("opencount",sharedpref.getInt("opencount",0)+ 1).commit();
    //    sharedpref.edit().putInt(FIRSTOPEN,0).commit();
    //    db.updatefirstopen(0);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }




    public static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }
}
