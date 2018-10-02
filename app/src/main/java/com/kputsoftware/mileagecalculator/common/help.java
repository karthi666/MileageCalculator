 package com.kputsoftware.mileagecalculator.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kputsoftware.mileagecalculator.BuildConfig;
import com.kputsoftware.mileagecalculator.R;


 /**
  * Created by duraiswa on 5/16/2017.
  */

 public class help extends AppCompatActivity {


     Button btnemail;

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.help);

         Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(myChildToolbar);
         getSupportActionBar().setDisplayShowTitleEnabled(false);
         ActionBar ab = getSupportActionBar();
         ab.setDisplayHomeAsUpEnabled(true);

         // to get version name
         final String versionname= BuildConfig.VERSION_NAME;
         btnemail = (Button)findViewById(R.id.email);

             btnemail.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View view) {
                     Intent intent = new Intent(Intent.ACTION_SEND);
                     intent.setType("plain/text");
                     intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "kputsoftware@gmail.com" });
                     intent.putExtra(Intent.EXTRA_SUBJECT, "MC-"+versionname+" : ");
                     intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                     startActivity(Intent.createChooser(intent, "Email Via"));
                 }


             });
         }

     }

