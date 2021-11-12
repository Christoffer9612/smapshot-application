package com.example.mycameraapp;

import android.annotation.SuppressLint;
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
    private TextView newAzimuth, newTilt, newRoll, success, percentage_error, percentage_error2, percentage_error3;
    public int error;

    @SuppressLint("SetTextI18n")
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

        percentage_error2 = findViewById(R.id.percentage_error2);
        percentage_error2.setTextColor(Color.parseColor("#444444"));

        percentage_error3 = findViewById(R.id.percentage_error3);
        percentage_error3.setTextColor(Color.parseColor("#444444"));

        String instruction_az = null;
        String instruction_tilt = null;
        String instruction_roll = null;

        try {
            instruction_az = instructUser(sbToFloatAngles(jsonObj,"azimuth"), az, "azimuth");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            instruction_tilt = instructUser(sbToFloatAngles(jsonObj,"tilt"), ti, "tilt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            instruction_roll = instructUser(sbToFloatAngles(jsonObj, "roll"), ro, "roll");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            percentage_error.setText("Azimuth Accuracy: " + percentage_error(az, ti, ro, "azimuth") + "%" + " " + instruction_az);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            percentage_error2.setText("Tilt Accuracy: " + percentage_error(az, ti, ro, "tilt") + "%" + " " + instruction_tilt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            percentage_error3.setText("Roll Accuracy: " + percentage_error(az, ti, ro, "roll") + "%" + " " + instruction_roll);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        percentage_error.setTypeface(montserrat_medium);
        percentage_error2.setTypeface(montserrat_medium);
        percentage_error3.setTypeface(montserrat_medium);


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

    //calculates accuracy for each orientation angle (azimuth, tilt, roll)
    public float percentage_error(float az, float ti, float ro, String orientation) throws JSONException {

        float float_error = 0;
        float float_error1 = 0;
        float float_error2 = 0;

        float azimuth_old = sbToFloatAngles(jsonObj, "azimuth");
        float tilt_old = sbToFloatAngles(jsonObj, "tilt");
        float roll_old = sbToFloatAngles(jsonObj, "roll");


        float_error = errorCalc(azimuth_old, az, float_error);
        float_error1 = errorCalc(tilt_old, ti, float_error1);
        float_error2 = errorCalc(roll_old, ro, float_error2);

        
        if(orientation.equals("azimuth")) {
            return Math.round(float_error);
        } else if (orientation.equals("tilt")) {
            return Math.round(float_error1);
        } else if (orientation.equals("roll")) {
            return Math.round(float_error2);
        }


        return 0;
    }
    
    public float errorCalc(float angle_old, float angle_new, float error) {

        error = Math.abs(angle_old - angle_new);
        error = error/360;
        error = (float) (((float) (1.0 - error)) * 100.0);
        
        return error;
    }


    public String instructUser(float old_angle, float new_angle, String angle_type) throws JSONException {


        if(angle_type.equals("azimuth")) {
            if (old_angle < new_angle) {
                return ", turn device " + String.valueOf(Math.abs(old_angle - new_angle) + "° west");
            } else if (old_angle == new_angle) {
                return ", Perfect !";
            } else {
                return ", turn device " +  String.valueOf(Math.abs(old_angle - new_angle) + "° east");
            }
        }

        if(angle_type.equals("tilt")) {
            if(old_angle > new_angle) {
               return ", tilt device " + String.valueOf(Math.abs(old_angle - new_angle) + "° up");
            } else if (old_angle == new_angle) {
                return ", Perfect !";
            } else {
                return ", tilt device " + String.valueOf(Math.abs(old_angle - new_angle) + "° down");
            }
        }

        if(angle_type.equals("roll")) {
           if(old_angle < new_angle) {
               return ", roll device " + String.valueOf(Math.abs(old_angle - new_angle) + "° left");
           }    else if (old_angle == new_angle) {
               return ", Perfect!";
           }   else {
               return ", roll device " + String.valueOf(Math.abs(old_angle - new_angle) + "° right");
           }

        }
        else {
            return "error";
        }

    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}