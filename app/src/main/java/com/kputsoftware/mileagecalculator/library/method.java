package com.kputsoftware.mileagecalculator.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kputsoftware.mileagecalculator.BuildConfig;
import com.kputsoftware.mileagecalculator.R;

import java.net.InetAddress;

import static com.kputsoftware.mileagecalculator.firebase.Firebase_database_Path_and_Query.FBP;
import static com.kputsoftware.mileagecalculator.firebase.Firebase_database_Path_and_Query.myRef;

/**
 * Created by duraiswa on 6/6/2017.
 */

public class method extends AppCompatActivity {



    //long toast for connection time out
    public void ctoast (final Context context , final String message ) {
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast =   Toast.makeText(context,message, Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextSize(25);
                    toast.setGravity(Gravity.CENTER,0,0);
                    v.setPadding(20, 20, 20, 23);
                    v.setTextColor(Color.RED);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.timeout_toast);
                    toast.show();
                }
            });

        }}
    //long toast for connection time out
    public void cltoast (final Context context , final String message ) {
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast =   Toast.makeText(context,message, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextSize(25);
                    toast.setGravity(Gravity.CENTER,0,0);
                    v.setPadding(20, 20, 20, 23);
                    v.setTextColor(Color.RED);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.timeout_toast);
                    toast.show();
                }
            });

        }}
    //long toast for connection time out
    public void btoast (final Context context , final String message ) {
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Toast toast =   Toast.makeText(context,message, Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextSize(25);
                    v.setPadding(20, 20, 20, 23);
                    v.setTextColor(Color.RED);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.timeout_toast);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                }
            });

        }}

    //long toast on bottong
    public void bltoast (final Context context , final String message ) {
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Toast toast =   Toast.makeText(context,message, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextSize(25);
                    v.setPadding(20, 20, 20, 23);
                    v.setTextColor(Color.RED);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.timeout_toast);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                }
            });

        }}

    //long toast on bottong
    public void clong (final Context context , final String message ) {
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Toast toast =   Toast.makeText(context,message, Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextSize(25);
                    v.setPadding(20, 20, 20, 23);
                    toast.setGravity(Gravity.CENTER,0,0);
                    v.setTextColor(Color.RED);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.timeout_toast);
                    toast.show();

                }
            });

        }}
    //check if internet connnection is there or not.
    public static boolean nccheck() {
        boolean status = true;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            InetAddress address = InetAddress.getByName("www.google.com");

            if (address != null) {

                status = false;

            } else {
                status = true;

            }

       /*     Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if (reachable){

            }
*/

        } catch (Exception e) // Catch the exception
        {
            e.printStackTrace();
        }
        return status;
    }




    //no internet message
    public static String ncmsg() {

        String msg = "No internet conenction";
        return msg;

    }



    // update click
    public void updateclick(String  child) {
        //  FirebaseDatabase.getInstance().goOnline();

        if (BuildConfig.DEBUG){
            child="debugclicks";

        }

        final String finalChild = child;
        myRef.child(FBP).child("clicks").child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int h = dataSnapshot.getValue(Integer.class);
                    int r = h + 1;

                    myRef.child(FBP).child("clicks").child(finalChild).setValue(r);

                    //  FirebaseDatabase.getInstance().goOffline();
                }else{
                    myRef.child(FBP).child("clicks").child(finalChild).setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
