package com.example.mycameraapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

public class PostPicActivity extends MainActivity { //AppCompatActivity
    private Button btnGoBack;
    private TextView newAzimuth, newTilt, newRoll, success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpic);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extracting data
        Float az = bundle.getFloat("azimuth");
        Float ti = bundle.getFloat("tilt");
        Float ro = bundle.getFloat("roll");

        success = findViewById(R.id.success);
        success.setTextColor(Color.parseColor("#444444"));
        success.setTypeface(montserrat_medium);

        azimuth = findViewById(R.id.json1);
        azimuth.setTextColor(Color.parseColor("#444444"));
        azimuth.setTypeface(montserrat_medium);
        try {
            azimuth.setText(findValue(jsonObj, "azimuth"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tilt = findViewById(R.id.json2);
        tilt.setTextColor(Color.parseColor("#444444"));
        tilt.setTypeface(montserrat_medium);
        try {
            tilt.setText(findValue(jsonObj, "tilt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        roll = findViewById(R.id.json3);
        roll.setTextColor(Color.parseColor("#444444"));
        roll.setTypeface(montserrat_medium);
        try {
            roll.setText(findValue(jsonObj, "roll"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        newAzimuth = findViewById(R.id.newAzimuth);
        newAzimuth.setTextColor(Color.parseColor("#444444"));
        newAzimuth.setText("new azimuth: " + String.valueOf(az));
        newAzimuth.setTypeface(montserrat_medium);

        newTilt = findViewById(R.id.newTilt);
        newTilt.setTextColor(Color.parseColor("#444444"));
        newTilt.setText("new tilt: " + String.valueOf(ti));
        newTilt.setTypeface(montserrat_medium);

        newRoll = findViewById(R.id.newRoll);
        newRoll.setTextColor(Color.parseColor("#444444"));
        newRoll.setText("new roll: " + String.valueOf(ro));
        newRoll.setTypeface(montserrat_medium);

        btnGoBack = (Button) findViewById(R.id.button);
        btnGoBack.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnGoBack.setTextColor(Color.parseColor("#444444"));
        btnGoBack.setTypeface(montserrat_medium);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(v);
            }
        });
    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}