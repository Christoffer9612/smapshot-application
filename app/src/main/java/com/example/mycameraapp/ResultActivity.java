package com.example.mycameraapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
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
    private TextView oldParams, newParams, success, percentage_accuracy, percentage_accuracy2, percentage_accuracy3;
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

        percentage_accuracy.setText("Azimuth accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "azimuth") + "%" + " " + instruction_az);
        percentage_accuracy2.setText("Tilt accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "tilt") + "%" + " " + instruction_tilt);
        percentage_accuracy3.setText("Roll accuracy: " + percentage_accuracy(realTimeAzimuth, realTimeTilt, realTimeRoll,azimuthOld, tiltOld, rollOld, "roll") + "%" + " " + instruction_roll);

        percentage_accuracy.setTypeface(montserrat_medium);
        percentage_accuracy2.setTypeface(montserrat_medium);
        percentage_accuracy3.setTypeface(montserrat_medium);
        percentage_accuracy.getBackground().setAlpha(204);
        percentage_accuracy2.getBackground().setAlpha(179);
        percentage_accuracy3.getBackground().setAlpha(153);

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
        }, 1000); //Delay so most recent photo can be displayed
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