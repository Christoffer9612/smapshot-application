package com.example.mycameraapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class ResultActivity extends MainActivity { //AppCompatActivity
    private Button btnGoBack, btnRetake;
    private TextView oldParams, newParams, success, percentage_accuracy;
    private ImageView newPhoto, oldPhoto;

    private Utils utils = new Utils(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        success = findViewById(R.id.success);
        oldParams = findViewById(R.id.oldParams);
        newParams = findViewById(R.id.newParams);
        oldPhoto = findViewById(R.id.oldPhoto);

        percentage_accuracy = findViewById(R.id.percentage_accuracy);

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
            oldPhoto.setImageResource(R.drawable.st_roch_test);
        } else if (bundleSelectedPhoto.getString("oldPhoto").equals("dia_303_12172")) {
            azimuthOld = bundleSelectedPhoto.getFloat("azimuth_dia");
            tiltOld = bundleSelectedPhoto.getFloat("tilt_dia");
            rollOld = bundleSelectedPhoto.getFloat("roll_dia");
            oldPhoto.setImageResource(R.drawable.dia_303_12172);
        }

        utils.setText(success, montserrat_medium);
        oldParams.setTypeface(montserrat_medium);
        utils.setText(newParams, montserrat_medium);

        oldParams.setText("Old azimuth: " + Math.round(azimuthOld) + "°" + "\n" + "Old tilt: " + Math.round(tiltOld) + "°" + "\n" + "Old roll: " + Math.round(rollOld) + "°");
        newParams.setText("New azimuth: " + Math.round(realTimeAzimuth)+ "°" + "\n" + "New tilt: " + Math.round(realTimeTilt) + "°" + "\n" + "New roll: " + Math.round(realTimeRoll) + "°");

        oldParams.getBackground().setAlpha(100);
        oldPhoto.getBackground().setAlpha(100);
        newParams.getBackground().setAlpha(140);

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

        percentage_accuracy.setText("Azimuth uncertainty estimation: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "azimuth") + "%"
         + "\n" + "Tilt uncertainty estimation: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "tilt") + "%"
        + "\n" + "Roll uncertainty estimation: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll,azimuthOld, tiltOld, rollOld, "roll") + "%" );



        percentage_accuracy.setTypeface(montserrat_medium);
        percentage_accuracy.getBackground().setAlpha(204);

        btnGoBack = findViewById(R.id.button);
        btnRetake = findViewById(R.id.btnRetake);
        utils.setButton(btnGoBack, montserrat_medium);
        utils.setButton(btnRetake, montserrat_medium);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(v);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //write your code here to be executed after 1 second
                loadPhoto();
            }
        }, 2000); //Delay so most recent photo can be displayed
    }

    public void loadPhoto() {
        //Get photo filenames
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File[] listFiles = storageDir.listFiles();
        Comparator c = new Comparator<File>(){ //File-specific Comparator

            public int compare(File file1, File file2){
                //this Comparator uses timestamps for orders
                long tsFile1 = file1.lastModified();
                long tsFile2 = file2.lastModified();
                return Long.valueOf(tsFile1).compareTo(tsFile2);
            }
        };

        //Apply the comparator on the array:
        Arrays.sort(listFiles, c);
        int i = 0;
        for (File f : listFiles) {
            Log.d("CREATEDDD", i + ": " + String.valueOf(f));
            i++;
        }
        //From the sorted array, the last one is the desired file
        String imgPath = listFiles[listFiles.length-1].getAbsolutePath();

        //Set new photo in result view:
        File imgFile = new File(imgPath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            newPhoto = (ImageView) findViewById(R.id.newPhoto);
            ObjectAnimator.ofFloat(newPhoto, View.ALPHA, 0.1f, 1.0f).setDuration(500).start(); //Animates (fade)
            newPhoto.setImageBitmap(RotateBitmap(myBitmap, 90)); //Rotate preview of photo 90 degrees
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //Calculates accuracy for each orientation angle (azimuth, tilt, roll)
    public int percentage_accuracy(float az, float ti, float ro, float azimuthOld, float tiltOld, float rollOld, String orientationAngle) {

        float azimuthUncertainty = 0, tiltUncertainty = 0, rollUncertainty = 0;

        if(orientationAngle.equals("azimuth")) {
            azimuthUncertainty = uncertaintyCalc(azimuthOld, az) * 100;
            return Math.round(azimuthUncertainty);
        } else if (orientationAngle.equals("tilt")) {
            tiltUncertainty = uncertaintyCalc(tiltOld, ti) * 100;
            return Math.round(tiltUncertainty);
        } else if (orientationAngle.equals("roll")) {
            rollUncertainty = uncertaintyCalc(rollOld, ro) * 100;
            return Math.round(rollUncertainty);
        }
        return 0;
    }
    
    public float uncertaintyCalc(float angle_old, float angle_new) {
        //compute three accuracies, going difference, clockwise and counterclockwise
        //return highest accuracy


        float uncertainty_diff;
        float uncertainty_clockwise;
        float uncertainty_counterclockwise;

        //Straight difference Diff
        uncertainty_diff = Math.abs(angle_old - angle_new);
        uncertainty_diff = (float) (uncertainty_diff /360.0);

        //clockwise up to 360 + old_angle
        float x = 360 - angle_new;
        uncertainty_clockwise = x + angle_old;
        uncertainty_clockwise = (float) (uncertainty_clockwise/360.0);


        //counterclockwise down to 0 degrees + (360-old_angle)
        float y = 360 - angle_old;
        uncertainty_counterclockwise = angle_new + y;
        uncertainty_counterclockwise = (float) (uncertainty_counterclockwise/360.0);


        //return the highest accuracy
        if(uncertainty_diff < uncertainty_clockwise && uncertainty_diff < uncertainty_counterclockwise) {
            Log.d("diff", "" + uncertainty_diff);
            return uncertainty_diff;
        } else if (uncertainty_clockwise < uncertainty_diff && uncertainty_clockwise < uncertainty_counterclockwise) {
            Log.d("diff", String.valueOf(uncertainty_clockwise));
            return uncertainty_clockwise;
        } else {
            Log.d("diff", String.valueOf(uncertainty_counterclockwise));
            return uncertainty_counterclockwise;

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

            if(diff < clockwise && diff < counterclockwise && new_angle < old_angle) {
                return ", turn device " + Math.round(diff) + "° clockwise";
            } else if (diff < clockwise && diff < counterclockwise && new_angle > old_angle){
                return ", turn device " + Math.round(diff) + "° counterclockwise";
            } else if (clockwise < diff && clockwise < counterclockwise) {
                return ", tilt device " + Math.round(clockwise) + "° clockwise";
            } else {
                return ", tilt device " + Math.round(counterclockwise) + "° counterclockwise";
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
                return ", tilt device " + Math.round(clockwise) + "° up";
            } else {
                return ", tilt device " + Math.round(counterclockwise) + "° down";
            }

        }

        if(angle_type.equals("roll")) {
            if(Math.round(diff) == 0.0) {
                return ", Perfect!";
            }
            if(diff < clockwise && diff < counterclockwise && new_angle < old_angle) {
                return ", roll device " + Math.round(diff) + "° right";
            } else if (diff < clockwise && diff < counterclockwise && new_angle > old_angle){
                return ", roll device " + Math.round(diff) + "° left";
            } else if (clockwise < diff && clockwise < counterclockwise) {
                return ", roll device " + Math.round(clockwise) + "° right";
            } else {
                return ", roll device " + Math.round(counterclockwise) + "° left";
            }

        }
        else {
            return "error";
        }

    }

    private void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openSelectPhoto(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

}