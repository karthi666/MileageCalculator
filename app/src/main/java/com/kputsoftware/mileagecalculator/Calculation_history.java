package com.kputsoftware.mileagecalculator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;
import com.kputsoftware.mileagecalculator.library.method;

import static com.kputsoftware.mileagecalculator.library.variables.*;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by duraiswa on 8/16/2017.
 */

public class Calculation_history extends AppCompatActivity {

    DatabaseHandler db = new DatabaseHandler(this);
    String currency;

    public ListView lv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TextView bartext = (TextView)findViewById(R.id.bartext);
        bartext.setText("Calculation History");

        // Getting listview from xml
        lv = (ListView) findViewById(android.R.id.list);
        Button clearall =(Button)findViewById(R.id.clearall);
        clearall.setVisibility(View.GONE);

        ads();
        displaylistview();


    }

    // Response from Edit Product Activity
    @Override
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
        }else {finish();}

    }

    private void ads() {
        final AdView adview = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.setVisibility(View.GONE);
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

    private void displaylistview() {

        ArrayList<HashMap<String, String>> datatolist = db.fetchcalculationhistory();
        ListAdapter adapter = new MyAdapter(
                Calculation_history.this, datatolist,
                R.layout.calculation_history_content, new String[]{KEY_ID,
                KEY_DATE, KEY_FUELPRICE,KEY_TRAVELKILOMETER,KEY_FUELUSED,KEY_MILEAGE,KEY_TOTALPRICE,KEY_CURRENCY,KEY_CURRENCY,KEY_FMETRIC,KEY_LMETRIC,KEY_FANDL},
                new int[]{R.id.id, R.id.date,R.id.fuelprice,R.id.travelkilometer,R.id.fuelused,R.id.mileage,R.id.totalprice,R.id.vfuelprice,R.id.vtotalfuelprice,R.id.vtotalfuelused,R.id.vtravelkm,R.id.vvechiclemileage});

        // updating listview
        lv.setAdapter(adapter);

    }

    public class MyAdapter extends SimpleAdapter {

        public MyAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);

            View btndelete =view.findViewById(R.id.delete);
            final String deleteid = ((TextView) view.findViewById(R.id.id)).getText().toString();

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Calculation_history.this);
                    alertDialog2.setTitle("Do want to Delete ?");
                    alertDialog2.setCancelable(false);
                    alertDialog2.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // db command to clear the db.
                                    // t.stoast(getApplicationContext(),"You clicked  -  No");
                                    db.calculation_delete(deleteid);
                                    t.ctoast(getApplicationContext(),"Deleted Successfully");
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                }
                            });

                    // Setting Negative "NO" Btn
                    alertDialog2.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    t.btoast(getApplicationContext(),"You clicked  -  No");
                                    dialog.cancel();

                                }
                            });

                    // Showing Alert Dialog
                    alertDialog2.show();


                }});


            return view;
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.clearall:
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Calculation_history.this);
                alertDialog2.setTitle("Do want to clear Calculation History ?");
                alertDialog2.setCancelable(false);
                alertDialog2.setMessage("This will clear all  Calculation  information");
                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // db command to clear the db.
                                // t.stoast(getApplicationContext(),"You clicked  -  No");
                                db.calculation_clearall();
                                t.ctoast(getApplicationContext(),"All Calculated  Information Cleared");
                                dialog.cancel();
                                finish();

                            }
                        });

                // Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                t.btoast(getApplicationContext(),"You clicked  -  No");
                                dialog.cancel();

                            }
                        });

                // Showing Alert Dialog
                alertDialog2.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
