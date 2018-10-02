package com.kputsoftware.mileagecalculator.library;

import android.content.Context;
import android.content.SharedPreferences;


import java.text.DecimalFormat;
import java.util.Stack;

/**
 * Created by duraiswa on 8/23/2017.
 */

public class variables {

    //URL
    public final static String appmarket = "market://details?id=com.kputsoftware.mileagecalculator";
    public final static String urlmarket = "https://play.google.com/store/apps/details?id=com.kputsoftware.mileagecalculator";
    public final static String kputsoftwareapps = "https://play.google.com/store/apps/developer?id=KPUT+Software";
    public final static String url = "http://refillcanbooking.16mb.com/tools/mc_update_token.php";
    public final static  String adsurl = "http://refillcanbooking.16mb.com/tools/mc_ads.php";
    public final static  String alladsurl = "http://refillcanbooking.16mb.com/tools/mc_all_ads.php";



    public static method t = new method();
    public static DecimalFormat df = new DecimalFormat("#.##");



    //sharedprefrence key
    public static final String MC = "mcpref";
    public  static final  String STARTKM ="startkm";
    public  static final  String ENDKM ="endkm";
    public static  String VALUE="value";
    public static final String ADS = "ads";
    public static final String FIRSTOPEN ="firstopen";
    public static  final String HISTORYCOUNT = "historycount";
      public static String OPENCOUNT = "opencount";
    public  static final  String TRUE ="true";
    public  static final  String FALSE ="false";
    public static final String AFFILIATEURL = "affiliateurl";
    public static final String FUELMETRIC = "fuelmetric";
    public static final String LENGTHMETRIC = "lengthmetric";
    public static final String LANGUAGE = "language";

    //setting
    public static final String CURRENCY = "currency";
    public static final String NOTIFICATION = "notification";
    public static final String TIPSNOTI = "tipsnoti";
    public  static final  String CALCOUNT ="calcount";


    //Topics
    public static final String TIPS = "tips";




    //  calculation  table
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_FUELPRICE = "fuelprice";
    public static final String KEY_TRAVELKILOMETER = "travelkilometer";
    public static final String KEY_FUELUSED = "fuelused";
    public static final String KEY_MILEAGE = "mileage";
    public static final String KEY_TOTALPRICE = "totalprice";
    public static final String KEY_FMETRIC = "fmetric";
    public static final String KEY_LMETRIC = "lmetric";
    public static final String KEY_FANDL = "fandl";


    /// APP Setting
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_NOTIFICATION = "notification";
    public static final String KEY_ADS ="ads";
    public static final String KEY_TOTALCALL ="totalcall";
    public static final String KEY_FIRSTOPEN ="firstopen";

    ///refillhistory table
    public static final String KEY_METER_READING = "meterreading";

}
