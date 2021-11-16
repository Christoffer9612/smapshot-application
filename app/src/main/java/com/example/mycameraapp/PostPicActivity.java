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

        success = findViewById(R.id.success);
        azimuth = findViewById(R.id.oldAz);
        tilt = findViewById(R.id.oldTilt);
        roll = findViewById(R.id.oldRoll);
        newAzimuth = findViewById(R.id.newAzimuth);
        newTilt = findViewById(R.id.newTilt);
        newRoll = findViewById(R.id.newRoll);
        percentage_accuracy = findViewById(R.id.percentage_accuracy);
        percentage_accuracy2 = findViewById(R.id.percentage_accuracy2);
        percentage_accuracy3 = findViewById(R.id.percentage_accuracy3);

        //Getting bundles
        Bundle bundle = getIntent().getExtras();
        Bundle bundleSelectedPhoto =  getIntent().getExtras();

        //Extracting data from bundle, values for real time params
        Float realTimeAzimuth = bundle.getFloat("azimuth");
        Float realTimeTilt = bundle.getFloat("tilt");
        Float realTimeRoll = bundle.getFloat("roll");

        //values for JSON-file params
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

        setText(success, montserrat_medium);
        setText(azimuth, montserrat_medium);
        setText(tilt, montserrat_medium);
        setText(roll, montserrat_medium);
        setText(newAzimuth, montserrat_medium);
        setText(newTilt, montserrat_medium);
        setText(newRoll, montserrat_medium);

        azimuth.setText("Old azimuth: " + Math.round(azimuthOld) + "°");
        tilt.setText("Old tilt: " + Math.round(tiltOld)+ "°");
        roll.setText("Old roll: " + Math.round(rollOld)+ "°");
        newAzimuth.setText("new azimuth: " + Math.round(realTimeAzimuth)+ "°");
        newTilt.setText("new tilt: " + Math.round(realTimeTilt)+ "°");
        newRoll.setText("new roll: " + Math.round(realTimeRoll)+ "°");

        percentage_accuracy.setTextColor(Color.parseColor("#444444"));
        percentage_accuracy2.setTextColor(Color.parseColor("#444444"));
        percentage_accuracy3.setTextColor(Color.parseColor("#444444"));

        String instruction_az = null;
        String instruction_tilt = null;
        String instruction_roll = null;

        try {
            instruction_az = instructUser(azimuthOld, realTimeAzimuth, "azimuth");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            instruction_tilt = instructUser(tiltOld, realTimeTilt, "tilt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            instruction_roll = instructUser(rollOld, realTimeRoll, "roll");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        percentage_accuracy.setText("Azimuth Accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "azimuth") + "%" + " " + instruction_az);
        percentage_accuracy2.setText("Tilt Accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "tilt") + "%" + " " + instruction_tilt);
        percentage_accuracy3.setText("Roll Accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll,azimuthOld, tiltOld, rollOld, "roll") + "%" + " " + instruction_roll);

        percentage_accuracy.setTypeface(montserrat_medium);
        percentage_accuracy2.setTypeface(montserrat_medium);
        percentage_accuracy3.setTypeface(montserrat_medium);

        btnGoBack = findViewById(R.id.button);
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

    //Calculates accuracy for each orientation angle (azimuth, tilt, roll)
    public int percentage_accuracy(float az, float ti, float ro, float azimuthOld, float tiltOld, float rollOld, String orientationAngle) {

        float azimuthAccuracy = 0, tiltAccuracy = 0, rollAccuracy = 0;

        if(orientationAngle.equals("azimuth")) {
            azimuthAccuracy = accuracyCalc(azimuthOld, az);
            return (int) Math.round(azimuthAccuracy);
        } else if (orientationAngle.equals("tilt")) {
            tiltAccuracy = accuracyCalc(tiltOld, ti);
            return (int) Math.round(tiltAccuracy);
        } else if (orientationAngle.equals("roll")) {
            rollAccuracy = accuracyCalc(rollOld, ro);
            return (int) Math.round(rollAccuracy);
        }
        return 0;
    }
    
    public float accuracyCalc(float angle_old, float angle_new) {
        //compute three accuracies, going difference, clockwise and counterclockwise
        //return highest accuracy

        float accuracy_diff;
        float accuracy_clockwise;
        float accuracy_counterclockwise;

        //Straight difference Diff
        accuracy_diff = Math.abs(angle_old - angle_new);
        accuracy_diff = accuracy_diff /360;
        accuracy_diff = (float) (((float) (1.0 - accuracy_diff)) * 100.0);

        //clockwise up to 360 + old_angle
        float x = 360 - angle_new;
        accuracy_clockwise = x + angle_old;
        accuracy_clockwise = accuracy_clockwise/360;
        accuracy_clockwise= (float) (((float) (1.0 - accuracy_clockwise)) * 100.0);

        //counterclockwise down to 0 degrees + (360-old_angle)
        float y = 360 - angle_old;
        accuracy_counterclockwise = angle_new + y;
        accuracy_counterclockwise = accuracy_counterclockwise/360;
        accuracy_counterclockwise= (float) (((float) (1.0 - accuracy_counterclockwise)) * 100.0);

        //return the highest accuracy
        if(accuracy_diff > accuracy_clockwise && accuracy_diff > accuracy_counterclockwise) {
            return accuracy_diff;
        } else if (accuracy_clockwise > accuracy_diff && accuracy_clockwise > accuracy_counterclockwise) {
            return accuracy_clockwise;
        } else {
            return accuracy_counterclockwise;
        }

        }


    public String instructUser(float old_angle, float new_angle, String angle_type) throws JSONException {
        //compute three rotations, diff, clockwise to 360 and then to old angle, counterclockwise to 0 and then to old angle backwards.
        //return the smallest rotation and in the right direction

        float diff = Math.abs(old_angle - new_angle);
        float clockwise = (360 - new_angle) + old_angle;
        float counterclockwise = new_angle + (360 - old_angle);



        if(angle_type.equals("azimuth")) {
            if(Math.round(diff) == 0.0) {
                return ", Perfect!";
            }

            if (diff < clockwise && diff < counterclockwise && new_angle < old_angle) {
                return ", turn device " + Math.round(diff) + "° east";
            } else if (diff < clockwise && diff < counterclockwise && new_angle > old_angle) {
                return ", turn device " + Math.round(diff) + "° west";
            } else if (clockwise < diff && clockwise < counterclockwise) {
                return ", turn device " + Math.round(clockwise) + "° east";
            } else {
                return ", turn device " + Math.round(counterclockwise) + "° west";
            }
        }

        if(angle_type.equals("tilt")) {
            if(Math.round(diff) == 0.0) {
                return ", Perfect!";
            }

            if(diff < clockwise && diff < counterclockwise && new_angle < old_angle) {
                return ", tilt device " + Math.round(diff) + "° up";
            } else if (diff < clockwise && diff < counterclockwise && new_angle > old_angle){
                return ", tilt device " + Math.round(diff) + "° down";
            } else if (clockwise < diff && clockwise < counterclockwise) {
                return ", turn device " + Math.round(clockwise) + "° up";
            } else {
                return ", turn device " + Math.round(counterclockwise) + "° down";
            }

        }

        if(angle_type.equals("roll")) {
            if(Math.round(diff) == 0.0) {
                return ", Perfect!";
            }
            if(diff < clockwise && diff < counterclockwise && new_angle < old_angle) {
                return ", roll device " + Math.round(diff) + "° right";
            } else if (diff < clockwise && diff < counterclockwise && new_angle > old_angle){
                return ", tilt device " + Math.round(diff) + "° left";
            } else if (clockwise < diff && clockwise < counterclockwise) {
                return ", turn device " + Math.round(clockwise) + "° right";
            } else {
                return ", turn device " + Math.round(counterclockwise) + "° left";
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

    private void setText(TextView txt, Typeface font) {
        txt.setTypeface(font);
        txt.setTextColor(Color.parseColor("#444444"));
    }

}