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
    private TextView newAzimuth, newTilt, newRoll, success, percentage_error;
    public int error;

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

        percentage_error = findViewById(R.id.percentage_error);
        percentage_error.setTextColor(Color.parseColor("#444444"));


        try {
            percentage_error.setText("Accuracy: " + percentage_error(az, ti, ro) + "%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        percentage_error.setTypeface(montserrat_medium);

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

    public float percentage_error(float az, float ti, float ro) throws JSONException {

        float float_error;
        float float_error1;
        float float_error2;


        StringBuilder test = findValue(jsonObj, "azimuth");
        String test2 = test.toString();
        String[] parts = test2.split(" ");
        String string_azimuth = parts[3];
        double d = Double.parseDouble(string_azimuth);
        float azimuth_float = (float) d;

        StringBuilder test3 = findValue(jsonObj, "tilt");
        String test4 = test3.toString();
        String[] parts1 = test4.split(" ");
        String string_tilt = parts1[3];
        double d1 = Double.parseDouble(string_tilt);
        float tilt_float = (float) d1;

        StringBuilder test5 = findValue(jsonObj, "roll");
        String test6 = test5.toString();
        String[] parts2 = test6.split(" ");
        String string_roll = parts2[3];
        double d3 = Double.parseDouble(string_roll);
        float roll_float = (float) d3;

        float_error = Math.abs(azimuth_float - az);
        float_error = float_error/360;
        float_error = (float) (((float) (1.0 - float_error)) * 100.0);

        float_error1 = Math.abs(tilt_float - ti);
        float_error1 = float_error1/360;
        float_error1 = (float) (((float) (1.0 - float_error1)) * 100.0);

        float_error2 = Math.abs(roll_float - ro);
        float_error2 = float_error2/360;
        float_error2 = (float) (((float) (1.0 - float_error2)) * 100.0);


        float_error = (float_error + float_error1 + float_error2)/3;
        return Math.round(float_error);
    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}