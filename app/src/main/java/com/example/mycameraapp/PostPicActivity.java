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
    private TextView newAzimuth, newTilt, newRoll, success, percentage_accuracy, percentage_accuracy2, percentage_accuracy3;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpic);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Getting bundles
        Bundle bundle = getIntent().getExtras();
        Bundle bundleSelectedPhoto =  getIntent().getExtras();

        //Extracting data from bundle
        Float az = bundle.getFloat("azimuth");
        Float ti = bundle.getFloat("tilt");
        Float ro = bundle.getFloat("roll");

        Float azimuthOld = null;
        Float tiltOld = null;
        Float rollOld = null;

        // Loading in old az, ti, roll from old selected photo (dia vs. test, based on photo name stored in bundle)
        if (bundleSelectedPhoto.getString("oldPhoto").equals("st_roch_test")) {
            azimuthOld = bundleSelectedPhoto.getFloat("azimuth_test");
            tiltOld = bundleSelectedPhoto.getFloat("tilt_test");
            rollOld = bundleSelectedPhoto.getFloat("roll_test");
        } else if (bundleSelectedPhoto.getString("oldPhoto").equals("dia_303_12172")) {
            azimuthOld = bundleSelectedPhoto.getFloat("azimuth_dia");
            tiltOld = bundleSelectedPhoto.getFloat("tilt_dia");
            rollOld = bundleSelectedPhoto.getFloat("roll_dia");
        }

        success = findViewById(R.id.success);
        success.setTextColor(Color.parseColor("#444444"));
        success.setTypeface(montserrat_medium);

        azimuth = findViewById(R.id.json1);
        azimuth.setTextColor(Color.parseColor("#444444"));
        azimuth.setTypeface(montserrat_medium);
        azimuth.setText("Azimuth old photo: " + azimuthOld);

        tilt = findViewById(R.id.json2);
        tilt.setTextColor(Color.parseColor("#444444"));
        tilt.setTypeface(montserrat_medium);
        tilt.setText("Tilt old photo: " + tiltOld);

        roll = findViewById(R.id.json3);
        roll.setTextColor(Color.parseColor("#444444"));
        roll.setTypeface(montserrat_medium);
        roll.setText("Roll old photo: " + rollOld);

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

        percentage_accuracy = findViewById(R.id.percentage_accuracy);
        percentage_accuracy.setTextColor(Color.parseColor("#444444"));

        percentage_accuracy2 = findViewById(R.id.percentage_accuracy2);
        percentage_accuracy2.setTextColor(Color.parseColor("#444444"));

        percentage_accuracy3 = findViewById(R.id.percentage_accuracy3);
        percentage_accuracy3.setTextColor(Color.parseColor("#444444"));

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
            percentage_accuracy.setText("Azimuth Accuracy: " + percentage_accuracy(az, ti, ro, "azimuth") + "%" + " " + instruction_az);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            percentage_accuracy2.setText("Tilt Accuracy: " + percentage_accuracy(az, ti, ro, "tilt") + "%" + " " + instruction_tilt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            percentage_accuracy3.setText("Roll Accuracy: " + percentage_accuracy(az, ti, ro, "roll") + "%" + " " + instruction_roll);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        percentage_accuracy.setTypeface(montserrat_medium);
        percentage_accuracy2.setTypeface(montserrat_medium);
        percentage_accuracy3.setTypeface(montserrat_medium);


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
    public float percentage_accuracy(float az, float ti, float ro, String orientation) throws JSONException {

        float float_error = 0, float_error1 = 0, float_error2 = 0;

        float azimuth_old = sbToFloatAngles(jsonObj, "azimuth");
        float tilt_old = sbToFloatAngles(jsonObj, "tilt");
        float roll_old = sbToFloatAngles(jsonObj, "roll");


        float_error = accuracyCalc(azimuth_old, az);
        float_error1 = accuracyCalc(tilt_old, ti);
        float_error2 = accuracyCalc(roll_old, ro);


        if(orientation.equals("azimuth")) {
            return Math.round(float_error);
        } else if (orientation.equals("tilt")) {
            return Math.round(float_error1);
        } else if (orientation.equals("roll")) {
            return Math.round(float_error2);
        }


        return 0;
    }
    
    public float accuracyCalc(float angle_old, float angle_new) {

        float error;
        float x = 360 - angle_old;

        //For the cases where the old angle is around 0 degrees (example: 356 or 4 degrees)
        if(angle_new - x < Math.abs(angle_old - angle_new)) {
            error = angle_new + x;
        } else {
            error = Math.abs(angle_old - angle_new);
        }

        error = error /360;
        error = (float) (((float) (1.0 - error)) * 100.0);
        return Math.abs(error);
    }


    public String instructUser(float old_angle, float new_angle, String angle_type) throws JSONException {

        float x = 360 - old_angle;

        //for the cases where the angle is around 0 degrees.
        boolean b = new_angle - x < Math.abs(old_angle - new_angle);

        if(angle_type.equals("azimuth")) {
            if (old_angle < new_angle) {
                if(b) {
                    return ", turn device " + String.valueOf(Math.abs(new_angle - x) +  "° east");
                }
                return ", turn device " + String.valueOf(Math.abs(old_angle - new_angle) + "° west");
            } else if (old_angle == new_angle) {
                return ", Perfect !";
            } else {
                return ", turn device " +  String.valueOf(Math.abs(old_angle - new_angle) + "° east");
            }
        }

        if(angle_type.equals("tilt")) {
            if(old_angle > new_angle) {
                if(b) {
                    return ", tilt device " + String.valueOf(Math.abs(new_angle - x) +  "° down");
                }
               return ", tilt device " + String.valueOf(Math.abs(old_angle - new_angle) + "° up");
            } else if (old_angle == new_angle) {
                return ", Perfect !";
            } else {
                return ", tilt device " + String.valueOf(Math.abs(old_angle - new_angle) + "° down");
            }
        }

        if(angle_type.equals("roll")) {
           if(old_angle < new_angle) {
               if(b) {
                   return ", roll device " + String.valueOf(Math.abs(new_angle - x) +  "° right");
               }
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