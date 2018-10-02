package com.kputsoftware.mileagecalculator.common;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kputsoftware.mileagecalculator.R;

/**
 * Created by duraiswa on 2/3/2018.
 */

public class Howtouse extends AppCompatActivity {


    Button btnok;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_use);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();

        btnok = (Button) findViewById(R.id.ok);
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              finish();
            }});
    }
}
