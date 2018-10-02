package com.kputsoftware.mileagecalculator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kputsoftware.mileagecalculator.library.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duraiswa on 7/14/2017.
 */

public class Tipspage extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    JSONParser jsonParser = new JSONParser();
    public final static String url = "http://refillcanbooking.16mb.com/tools/mc_tips_page_open.php";

    TextView ttitle;
    TextView tmessage;
    Button btnclose;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_page);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
       // ab.setDisplayHomeAsUpEnabled(true);




        ttitle = (TextView) findViewById(R.id.title);
        tmessage = (TextView)findViewById(R.id.message);
        ttitle.setVisibility(View.GONE);
        tmessage.setVisibility(View.GONE);



        Intent i = getIntent();

        String ads = i.getStringExtra("ads");
        String title = i.getStringExtra("title");
        String message = i.getStringExtra("message");

        ttitle.setText(title);
        tmessage.setText(message);
        ttitle.setVisibility(View.VISIBLE);
        tmessage.setVisibility(View.VISIBLE);

        AdView adView = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();

        String adunitid = getResources().getString(R.string.interstitial);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(adunitid);
        if (ads.equals("yes")) {
            adView.loadAd(adRequest);
             mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        btnclose = (Button) findViewById(R.id.close);
        btnclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    finish();
                } else
                {
                    finish();
                }
            }});
        new updatetipsopen().execute();
        }

    class updatetipsopen extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            SystemClock.sleep(3000);
            String token = FirebaseInstanceId.getInstance().getToken();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("open", "yes"));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            return null;
        }
    }

    //double back for close app
    private boolean doubleBackToExitPressedOnce;
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }else
            {
                finish();
            }
            // return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            finish();
        } else {
           // Log.d("TAG", "The interstitial wasn't loaded yet.");
            Toast.makeText(Tipspage.this," Please click Back again to exit", Toast.LENGTH_LONG).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = true;
            }
        }, 2000);
    }
}
