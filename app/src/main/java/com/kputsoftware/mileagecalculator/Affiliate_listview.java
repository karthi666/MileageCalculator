package com.kputsoftware.mileagecalculator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kputsoftware.mileagecalculator.library.DatabaseHandler;
import com.kputsoftware.mileagecalculator.library.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.ProgressDialog.STYLE_HORIZONTAL;
import static com.kputsoftware.mileagecalculator.library.variables.MC;
import static com.kputsoftware.mileagecalculator.library.variables.alladsurl;

import com.kputsoftware.mileagecalculator.library.method;

/**
 * Created by duraiswa on 9/19/2017.
 */

public class Affiliate_listview extends AppCompatActivity {

    DatabaseHandler db = new DatabaseHandler(this);
    method t = new method();
    private ProgressDialog pDialog;
    String currency;
    JSONArray ads = null;
    ArrayList<HashMap<String, String>> adslist;

    public ListView lv;
    int a = 0;
    int noofads = 0;
    int b = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        TextView bartext = (TextView) findViewById(R.id.bartext);
        bartext.setText("Accessories ");

        Button clearall =(Button)findViewById(R.id.clearall);
        clearall.setVisibility(View.GONE);

        lv = (ListView) findViewById(android.R.id.list);
        adslist = new ArrayList<HashMap<String, String>>();

        new loadallorders().execute();
    }

    class loadallorders extends AsyncTask<String, Integer, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Affiliate_listview.this);
            pDialog.setMessage("Loading Accessories for you...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(STYLE_HORIZONTAL);
            pDialog.show();

        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            publishProgress(8);
           List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("country", getSharedPreferences(MC,0).getString("country",null)));

           publishProgress(15);
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(alladsurl, "POST", params);
           publishProgress(20);


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");


                if (success == 1) {

                    noofads = json.getInt("noofads");


                    ads = json.getJSONArray("ads");
                    adslist.clear();
                    String urlbuyimage = "empty";
                    String buyimage = "null";
                    // looping through All Products
                    for (int i = 0; i < ads.length(); i++) {
                        JSONObject c = ads.getJSONObject(i);

                        // Storing each json item in variable
                        String price = c.getString("price");
                        String product = c.getString("product");

                        String detail = c.getString("detail");
                        String productlink = c.getString("productlink");

/*                             String buybutton = c.getString("buybutton");
                        String imagelink = c.getString("imagelink");*/


                        String productimage = c.getString("productimage");
                        Bitmap pim = UrltoBitMap(productimage);
                        productimage = BitMapToString(pim);


                        if (!urlbuyimage.equals(c.getString("buyimage"))) {

                            urlbuyimage = c.getString("buyimage");
                            Bitmap bim = UrltoBitMap(urlbuyimage);
                            buyimage = BitMapToString(bim);

                        }

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("price", price);
                        map.put("product", product);
                        map.put("buyimage", buyimage);
                        map.put("detail", detail);
                        map.put("productlink", productlink);
                        map.put("productimage", productimage);
                        // adding HashList to ArrayList
                        adslist.add(map);
                       // lbtnv = lbtnv + 1;
                        b = b + 1;
                        publishProgress(20 + (b *10) );


                    }
                } else if (success == 8){


                    finish();
                }else if (success == 7){


                    finish();
                }
                else{
                    pDialog.dismiss();
                    t.bltoast(getApplicationContext(),"No Accessories right now. Try later");

                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {

             // dismiss the dialog after getting all products


            pDialog.setMessage("Arranging Accessories ...");


            // this is for disabling the loadmore button

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new MyAdapter(
                            Affiliate_listview.this, adslist,
                            R.layout.affiliate_content, new String[]{"price","product","buyimage","detail","productlink","productimage"},
                            new int[]{R.id.price, R.id.product, R.id.buybutton, R.id.productdetail, R.id.productlink,R.id.imagelink});


                    // updating listview
                    lv.setAdapter(adapter);

                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);

            pDialog.setProgress(values[0]);


        }


        @Override
        protected void onCancelled() {
            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            // Closing all previous activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
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

            final String productlink = ((TextView) view.findViewById(R.id.productlink)).getText().toString();

            View product = view.findViewById(R.id.product);
            product.setSelected(true);

            String imagelink = ((TextView) view.findViewById(R.id.imagelink)).getText().toString();
            String buybutton = ((TextView) view.findViewById(R.id.buybutton)).getText().toString();
            View productimage = view.findViewById(R.id.pimage);
            View buyimage = view.findViewById(R.id.imagebuy);

    /*        Bitmap pimage = null,bimage = null;

            String pos = "p"+position;


            try {
                // Download Image from URL
                InputStream pinput = new URL(imagelink).openStream();
               InputStream  binput = new java.net.URL(buybutton).openStream();

                // Decode Bitmap
                pimage = BitmapFactory.decodeStream(pinput);
                bimage = BitmapFactory.decodeStream(binput);


            } catch (Exception e) {
                e.printStackTrace();
            }*/

            Bitmap pimage = StringToBitMap(imagelink);
            Bitmap bimage = StringToBitMap(buybutton);

            BitmapDrawable dpimage = new BitmapDrawable(getResources(), pimage);
            BitmapDrawable dbimage = new BitmapDrawable(getResources(), bimage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                productimage.setBackground(dpimage);
                buyimage.setBackground(dbimage);
            }


            View laads = view.findViewById(R.id.aads);
            laads.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    String plink = productlink;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(plink));
                    startActivity(i);
                }
            });


            if(position == 4 || noofads == (position +1))  {

                pDialog.dismiss();
            }
            return view;
        }

    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public Bitmap UrltoBitMap(String url){
        try {
            InputStream pinput = new URL(url).openStream();
            // Decode Bitmap
           Bitmap bitmap = BitmapFactory.decodeStream(pinput);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}