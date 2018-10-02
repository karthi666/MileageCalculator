package com.kputsoftware.mileagecalculator.library;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.kputsoftware.mileagecalculator.library.variables.t;

public class BackupAndRestore {

    public static void restoredb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();          // sd card patch
            if (sd.canWrite()) {
                String mcfolder = "Mileage Calculator";
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
            }else
            {
                t.ctoast(context,"Permission denied! Allow permission to read/write from  storage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void backupdb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();  // to get sd card path
            File data = Environment.getDataDirectory();   // to get data directory on  the mobile

           if (sd.canWrite()) {

               // for creating mileage calculator dirctory
               String mcfolder = "Mileage Calculator";
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
           }else
           {
               t.ctoast(context,"Permission denied! Allow permission to write on storage");
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
