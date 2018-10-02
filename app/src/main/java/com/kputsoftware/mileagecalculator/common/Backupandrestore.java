package com.kputsoftware.mileagecalculator.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kputsoftware.mileagecalculator.R;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.kputsoftware.mileagecalculator.library.variables.t;

public class Backupandrestore extends AppCompatActivity implements View.OnClickListener {

    Button btnbackup,btnrestore,btnclose;
    TextView bkplocation;

    String TAG = "permissionresulttag";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_and_restore);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


    findViewById(R.id.btnbackup).setOnClickListener(this);
    findViewById(R.id.btnrestore).setOnClickListener(this);
    findViewById(R.id.btnclose).setOnClickListener(this);

    bkplocation=findViewById(R.id.bkplocation);


        isStoragePermissionGranted();




    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        switch (i){
            case R.id.btnbackup:
                backupdb(this);
                break;

            case R.id.btnrestore:
                restoredb(this);
                break;

            case R.id.btnclose:
                finish();
        }


    }
    public void restoredb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();          // sd card patch
            if (sd.canWrite()) {
              //  String mcfolder =  getString(R.string.app_name);//"Mileage Calculator";
                String mcfolder = getResources().getString(R.string.app_name);
                File f = new File(Environment.getExternalStorageDirectory(),mcfolder);
                File bkp = new File(Environment.getExternalStorageDirectory()+"/"+mcfolder,"History.backup");
                if (f.exists()){
                    if (!bkp.exists()) {
                        t.ctoast(context, "backup not yet taken so far");
                    }
                }else{
                    t.ctoast(context, "backup not yet taken so far");

                }


                File backupDB = context.getDatabasePath(DatabaseHandler.getDBName());  //default database path  and file

                //  String backupDBPath = String.format("%s.bak", DatabaseHandler.getDBName());
                String backupDBPath = "History.backup";
                // File currentDB = new File(sd, backupDBPath);

                File currentDB = new File(sd+"/"+mcfolder,backupDBPath);  // backup file location

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                // MyApplication.toastSomething(context, "Import Successful!");
                t.ctoast(context,"restore Successfull");
                bkplocation.setText("Restored from  : "+currentDB);
            }else
            {
                t.ctoast(context,"Permission denied! Allow permission to read/write from  storage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void backupdb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();  // to get sd card path
            File data = Environment.getDataDirectory();   // to get data directory on  the mobile

            if (sd.canWrite()) {

                // for creating mileage calculator dirctory
              //  String mcfolder = "Mileage Calculator";
                String mcfolder = getResources().getString(R.string.app_name);
                File f = new File(Environment.getExternalStorageDirectory(),mcfolder);
                if (!f.exists()){
                    f.mkdirs();
                }



                //     String backupDBPath = String.format("%s.bak", DatabaseHandler.getDBName());
                String backupDBPath = "History.backup";           // backup filename

                File currentDB = context.getDatabasePath(DatabaseHandler.getDBName());   // original database path and name
                File backupDB = new File(sd+"/"+mcfolder, backupDBPath);          // backup location and backup file name

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                //   MyApplication.toastSomething(context, "Backup Successful!");
                t.ctoast(context,"Backup Successfull");
                bkplocation.setText("Backup Stored at : "+backupDB);
                MediaScannerConnection.scanFile(this,new String[]{f.toString()},null,null);

            }else
            {
                t.ctoast(context,"Permission denied! Allow permission to write on storage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
