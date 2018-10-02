
package com.kputsoftware.mileagecalculator.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kputsoftware.mileagecalculator.library.variables.*;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;  // 1 - upto 15  ,,,, 2- from 16,,, 3 --21 .,,, 4 from 22

    // Database Name
    private static final String DATABASE_NAME = "MC_DB";

    // Login table name
    private static final String TABLE_NAME = "SMC_TABLE";
    private static final String CALCULATION_HISTORY = "SMC_TABLE";
    private static final String APPSETTING = "SETTING_TABLE";
    private static final String REFIL_HISTORY = "REFIL_HISTORY";



    // table creation detail
    String CREATE_LOGIN_TABLE = "CREATE TABLE " + CALCULATION_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CURRENCY + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_FUELPRICE + " TEXT,"
            + KEY_TRAVELKILOMETER + " TEXT ,"
            + KEY_FUELUSED + " TEXT,"
            + KEY_MILEAGE + " TEXT,"
            + KEY_TOTALPRICE + " TEXT,"
            + KEY_FMETRIC + " TEXT,"
            + KEY_LMETRIC + " TEXT,"
            + KEY_FANDL + " TEXT " + ")";

    String CREATE_APPSETTING = "CREATE TABLE " + APPSETTING + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CURRENCY + " TEXT,"
            + KEY_NOTIFICATION + " TEXT,"
            + KEY_ADS + " TEXT,"
            + KEY_TOTALCALL + " INTEGER ,"
            + KEY_FIRSTOPEN + " INTEGER "+")";

    String CREATE_REFILL_HISTORY = "CREATE TABLE  IF NOT EXISTS " + REFIL_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_METER_READING + " TEXT,"
            + KEY_TOTALPRICE + " TEXT,"
            + KEY_FUELPRICE + " TEXT,"
            + KEY_FUELUSED + " TEXT ,"
            + KEY_CURRENCY + " TEXT "+")";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_APPSETTING);
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_REFILL_HISTORY);

    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        switch(oldVersion){

            case 1:

                db.execSQL(" ALTER TABLE " + APPSETTING + " ADD COLUMN "+ KEY_ADS + " TEXT ");
                db.execSQL(" ALTER TABLE " + APPSETTING + " ADD COLUMN "+ KEY_TOTALCALL + " INTEGER ");

            case 2: //app version 16
              //  db.execSQL("DROP TABLE IF EXISTS " + REFIL_HISTORY);
                db.execSQL(CREATE_REFILL_HISTORY);
             //   db.execSQL(" ALTER TABLE " + APPSETTING + " ADD COLUMN "+ KEY_FIRSTOPEN + " INTEGER ");


            case 3:
                db.execSQL(" ALTER TABLE " + CALCULATION_HISTORY + " ADD COLUMN "+ KEY_FMETRIC+ " TEXT ");
                db.execSQL(" ALTER TABLE " + CALCULATION_HISTORY + " ADD COLUMN "+ KEY_LMETRIC+ " TEXT ");
                db.execSQL(" ALTER TABLE " + CALCULATION_HISTORY + " ADD COLUMN "+ KEY_FANDL+ " TEXT ");

                db.execSQL("UPDATE " + CALCULATION_HISTORY + " SET "+ KEY_FMETRIC+ " = 'Ltr' ");
                db.execSQL(" UPDATE " + CALCULATION_HISTORY + " SET "+ KEY_LMETRIC+ " = 'KM' ");
                db.execSQL(" UPDATE " + CALCULATION_HISTORY + " SET "+ KEY_FANDL+ " = 'KM/L' ");

                break;


            default:
                throw new IllegalStateException("onUpgrade() with unknown oldVersion" + oldVersion);

        }

     /*   db.execSQL(" ALTER TABLE " + APPSETTING + " RENAME TO appsettingold");
        onCreate(db);
        db.execSQL("INSERT INTO " + APPSETTING + "( currency, notification ) SELECT currency,notification FROM appsettingold " );
        db.execSQL("DROP TABLE appsettingold");*/
    }

    private void three(SQLiteDatabase db) {
        // from old version 2 to new version --adding new db.
      //  db.execSQL("DROP TABLE IF EXISTS " + REFIL_HISTORY);
    }

    private void two(SQLiteDatabase db) {
        //from old version 1 to  newversion -- only adding ads column

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.delete(TABLE_NAME, null, null);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
    }

    public int historycount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void saveinfo(String date, String currency, String fuelprice, String travelkilometer, String fuelused, String mileage, String totalprice,String fmetric,String lmetric,String fandl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_CURRENCY, currency);
        values.put(KEY_FUELPRICE, fuelprice);
        values.put(KEY_TRAVELKILOMETER, travelkilometer);
        values.put(KEY_FUELUSED, fuelused);
        values.put(KEY_MILEAGE, mileage);
   //     values.put(KEY_ID,"1231");
        values.put(KEY_TOTALPRICE, totalprice);
        values.put(KEY_FMETRIC, fmetric);
        values.put(KEY_LMETRIC, lmetric);
        values.put(KEY_FANDL, fandl);

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
/*
    //this to get app Setting row count
    public int appsettingcheck() {
        String countQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }



    // this is to add currency
    public void addsetting (String currency, String notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENCY, currency);
        values.put(KEY_NOTIFICATION, notification);
        db.insert(APPSETTING, null, values);
        db.close(); // Closing database connection
    }
    // this to update currency
    public void updatesetting (String currency, String notification) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_CURRENCY, currency);
    values.put(KEY_NOTIFICATION, notification);
    db.update(APPSETTING,values,KEY_ID+"=1",null);
    db.close(); // Closing database connection
}
    // this is to get the currency
    public String getnotification() {
        String value = new String();
        String selectQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getString(2));
        cursor.close();
        db.close();
        return value;
    }
    // this is to get the currency
    public String getcurrency(){
        String value = new String();
        String selectQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getString(1));
        cursor.close();
        db.close();
        return value;
    }


    //this to update adsenabled
    public void updateadsenable (String enable) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_ADS, enable);
    db.update(APPSETTING,values,KEY_ID+"=1",null);
    db.close(); // Closing database connection
}
    //this is to get ads enabled or not.
    public String getadsenable(){
        String value = new String();
        String selectQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getString(3));
        cursor.close();
        db.close();
        return value;
    }



    //this to update adsenabled
    public void updatecalcount (Integer count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOTALCALL, count);
        db.update(APPSETTING,values,KEY_ID+"=1",null);
        db.close(); // Closing database connection
    }
    //this is to get ads enabled or not.
    public Integer getcallcount(){
        Integer value = new Integer(0);
        String selectQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getInt(4));
        cursor.close();
        db.close();
        return value;


    }*/

public static String getDBName(){

    String value = DATABASE_NAME;
    return value;
}

    // 5- first open
    //this to update adsenabled
    public void updatefirstopen (Integer count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTOPEN, count);
        db.update(APPSETTING,values,KEY_ID+"=1",null);
        db.close(); // Closing database connection
    }
    //this is to get ads enabled or not.
    public Integer getfirstopen(){
        Integer value = new Integer(0);
        String selectQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getInt(5));
        cursor.close();
        db.close();
        return value;


    }

//this is for calculation query
    //this is add calculation info
    public void addcalculationhistory(String date, String currency, String fuelprice, String travelkilometer, String fuelused, String mileage, String totalprice) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_DATE, date);
    values.put(KEY_CURRENCY, currency);
    values.put(KEY_FUELPRICE, fuelprice);
    values.put(KEY_TRAVELKILOMETER, travelkilometer);
    values.put(KEY_FUELUSED, fuelused);
    values.put(KEY_MILEAGE, mileage);
    values.put(KEY_TOTALPRICE, totalprice);
    // Inserting Row
    db.insert(CALCULATION_HISTORY, null, values);
    db.close(); // Closing database connection
}
    //this is fetch calculation history
    public ArrayList<HashMap<String, String>> fetchcalculationhistory() {

        ArrayList<HashMap<String, String>>  historylist = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + CALCULATION_HISTORY +" ORDER BY "+ KEY_ID+ "  DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        while(c.moveToNext()){
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(KEY_ID, c.getString(0));
            temp.put(KEY_CURRENCY, c.getString(1));
            temp.put(KEY_DATE, c.getString(2));
            temp.put(KEY_FUELPRICE, c.getString(3));
            temp.put(KEY_TRAVELKILOMETER, c.getString(4));
            temp.put(KEY_FUELUSED, c.getString(5));
            temp.put(KEY_MILEAGE, c.getString(6));
            temp.put(KEY_TOTALPRICE, c.getString(7));
            temp.put(KEY_FMETRIC, c.getString(8));
            temp.put(KEY_LMETRIC, c.getString(9));
            temp.put(KEY_FANDL, c.getString(10));
            historylist.add(temp);
        }
        c.moveToFirst();
        c.close();
        db.close();
        return historylist;
    }
    //delete paricualt history id
    public void calculation_delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(CALCULATION_HISTORY,KEY_ID +" = "+ id,null);
        db.close(); // Closing database connection

    }
    public void calculation_clearall() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(CALCULATION_HISTORY, null, null);
        db.close();
    }

    //this is to add the refillhistory
    public int refill_historycheck() {
        String countQuery = "SELECT  * FROM " + REFIL_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void addrefillhistory(String date,String meterreading, String totalprice, String fuelquantity, String fuelprice,String currency){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_METER_READING, meterreading);
        values.put(KEY_TOTALPRICE, totalprice);
        values.put(KEY_FUELPRICE, fuelprice);
        values.put(KEY_FUELUSED, fuelquantity);
        values.put(KEY_CURRENCY, currency);
        // Inserting Row
        db.insert(REFIL_HISTORY, null, values);
        db.close(); // Closing database connection
    }
   //fectchrefillhisory
    public ArrayList<HashMap<String, String>> fetchrefillhistory() {

        ArrayList<HashMap<String, String>>  historylist = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + REFIL_HISTORY +" ORDER BY "+ KEY_ID+ "  DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
           while(c.moveToNext()){
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(KEY_ID, c.getString(0));
            temp.put(KEY_DATE, c.getString(1));
            temp.put(KEY_METER_READING, c.getString(2));
            temp.put(KEY_TOTALPRICE, c.getString(3));
            temp.put(KEY_FUELPRICE, c.getString(4));
            temp.put(KEY_FUELUSED, c.getString(5));
            temp.put(KEY_CURRENCY, c.getString(6));
            historylist.add(temp);
        }
        c.moveToFirst();
        c.close();
        db.close();
        return historylist;
    }
    //delete paricualt history id
    public void refill_delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(REFIL_HISTORY,KEY_ID +" = "+ id,null);
        db.close(); // Closing database connection

    }
    public void refill_clearall() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(REFIL_HISTORY, null, null);
        db.close();
    }


    //get all detail from sqllite
    public Cursor fetchall() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(TABLE_NAME, new String[] {
                        KEY_ID,
                        KEY_DATE,
                        KEY_CURRENCY,
                        KEY_FUELPRICE,
                        KEY_TRAVELKILOMETER,
                        KEY_FUELUSED,
                        KEY_MILEAGE,
                        KEY_TOTALPRICE},
                null, null, null, null, KEY_ID +" DESC", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }





    //thsee are old:-


    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("phnumber", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }



    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + APPSETTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    // this is to get consumer  or distributor
    public String getusertype() {

        String value = new String();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        value = (cursor.getString(1));
        cursor.close();
        db.close();
        return value;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     */


}
