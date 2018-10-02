 package com.kputsoftware.mileagecalculator.common;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.kputsoftware.mileagecalculator.BuildConfig;
import com.kputsoftware.mileagecalculator.R;

import java.util.Locale;

import static com.kputsoftware.mileagecalculator.library.variables.LANGUAGE;
import static com.kputsoftware.mileagecalculator.library.variables.MC;


 /**
 * Created by duraiswa on 5/16/2017.
 */

public class about extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // to get version name
        String versionname= BuildConfig.VERSION_NAME;

        TextView version=(TextView) findViewById(R.id.version);
        version.setText(versionname);

    /*    String language  = getSharedPreferences(MC,0).getString(LANGUAGE,"English");
        Resources res2 = getApplicationContext().getResources();
        DisplayMetrics dm2 = res2.getDisplayMetrics();
        android.content.res.Configuration conf2 = res2.getConfiguration();
        conf2.locale = new Locale(language);
        res2.updateConfiguration(conf2, dm2);
        version.setText(R.string.appname);
*/
       // version.setText(R.string.appname);

    }
}
