package com.kputsoftware.mileagecalculator.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kputsoftware.mileagecalculator.library.method;

/**
 * Created by duraiswa on 9/27/2017.
 */

public class Firebase_database_Path_and_Query {

    public static method t = new method();



//    Path
    public static final String FBP = "MC";
    public static final String ADS = "ads";



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();



//    QUERY
    public static final Query QUERYADS = myRef.child(FBP).child(ADS);
}
