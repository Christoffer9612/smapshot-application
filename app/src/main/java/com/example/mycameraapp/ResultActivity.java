package com.example.mycameraapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class ResultActivity extends MainActivity { //AppCompatActivity
    private Button btnGoBack, btnRetake;
    private TextView oldParams, newParams, success, percentageUncertainty, txtScore;
    private ShapeableImageView newPhoto, oldPhoto;
    private ToggleButton toggle;

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
        percentageUncertainty = findViewById(R.id.percentage_accuracy);
        txtScore = findViewById(R.id.txtScore);

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
        if (bundleSelectedPhoto.getString("oldPhoto").equals("photoOne")) {
            azimuthOld = utils.normaliseAngles(bundleSelectedPhoto.getFloat("azimuth_test"));
            tiltOld = utils.normaliseAngles180(bundleSelectedPhoto.getFloat("tilt_test"));
            rollOld = utils.normaliseAngles180(bundleSelectedPhoto.getFloat("roll_test"));
            oldPhoto.setImageResource(R.drawable.st_roch_test);
        } else if (bundleSelectedPhoto.getString("oldPhoto").equals("photoTwo")) {
            azimuthOld = utils.normaliseAngles(bundleSelectedPhoto.getFloat("azimuth_dia"));
            tiltOld = utils.normaliseAngles180(bundleSelectedPhoto.getFloat("tilt_dia"));
            rollOld = utils.normaliseAngles180(bundleSelectedPhoto.getFloat("roll_dia"));
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://smapshot.heig-vd.ch/api/v1/data/collections/31/images/500/185747.jpg", //Dia photo
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Picasso.get().load("https://smapshot.heig-vd.ch/api/v1/data/collections/31/images/500/185747.jpg").into(oldPhoto);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("URL ERROR", "URL link is broken or you don't have internet connection...");
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        utils.setText(success, montserrat_medium);
        oldParams.setTypeface(montserrat_medium);
        utils.setText(newParams, montserrat_medium);

        oldParams.setText("Old azimuth: " + Math.round(azimuthOld) + "°" + "\n" + "Old tilt: " + Math.round(tiltOld) + "°" + "\n" + "Old roll: " + Math.round(rollOld) + "°");
        newParams.setText("New azimuth: " + Math.round(realTimeAzimuth)+ "°" + "\n" + "New tilt: " + Math.round(realTimeTilt) + "°" + "\n" + "New roll: " + Math.round(realTimeRoll) + "°");

        oldParams.getBackground().setAlpha(100);
        oldPhoto.getBackground().setAlpha(100);
        newParams.getBackground().setAlpha(140);

        percentageUncertainty.setText("Azimuth uncertainty estimation: " + percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "azimuth") + "%"
         + "\n" + "Tilt uncertainty estimation: " + percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "tilt") + "%"
        + "\n" + "Roll uncertainty estimation: " + percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll,azimuthOld, tiltOld, rollOld, "roll") + "%" );

        percentageUncertainty.setTypeface(montserrat_medium);
        percentageUncertainty.getBackground().setAlpha(204);

        btnGoBack = findViewById(R.id.button);
        btnRetake = findViewById(R.id.btnRetake);
        utils.setButton(btnGoBack, montserrat_medium);
        utils.setButton(btnRetake, montserrat_medium);

        ///setScore, using the three percentageUncertainties of the the three orientation angles
        setScore(percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "azimuth"),
                percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll, azimuthOld, tiltOld, rollOld, "tilt"),
                percentageUncertainty(realTimeAzimuth, realTimeTilt, realTimeRoll,azimuthOld, tiltOld, rollOld, "roll"));

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

        oldParams.setVisibility(View.GONE);
        newParams.setVisibility(View.GONE);
        percentageUncertainty.setVisibility(View.GONE);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    oldParams.setVisibility(View.VISIBLE);
                    newParams.setVisibility(View.VISIBLE);
                    percentageUncertainty.setVisibility(View.VISIBLE);
                } else {
                    oldParams.setVisibility(View.GONE);
                    newParams.setVisibility(View.GONE);
                    percentageUncertainty.setVisibility(View.GONE);

                }
            }
        });

    }

    public void loadPhoto() {
        //Get photo filenames
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File[] listFiles = storageDir.listFiles();
        Comparator c = new Comparator<
                File>(){ //File-specific Comparator

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
            newPhoto = findViewById(R.id.newPhoto);
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
    public int percentageUncertainty(float az, float ti, float ro, float azimuthOld, float tiltOld, float rollOld, String orientationAngle) {

        float azimuthUncertainty = 0, tiltUncertainty = 0, rollUncertainty = 0;

        if(orientationAngle.equals("azimuth")) {
            azimuthUncertainty = utils.uncertaintyCalc(azimuthOld, az) * 100;
            return Math.round(azimuthUncertainty);
        } else if (orientationAngle.equals("tilt")) {
            tiltUncertainty = utils.uncertaintyCalc180(tiltOld, ti) * 100;
            return Math.round(tiltUncertainty);
        } else if (orientationAngle.equals("roll")) {
            rollUncertainty = utils.uncertaintyCalc180(rollOld, ro) * 100;
            return Math.round(rollUncertainty);
        }
        return 0;
    }
    //sets grade from meanUncertainty of the three orientation angles
    public void setScore(float azimuthUncertainty, float tiltUncertainty, float rollUncertainty) {

        float meanUncertainty = (azimuthUncertainty + tiltUncertainty + rollUncertainty)/3;

        if(meanUncertainty < 5) { //Change colors
            txtScore.setText("Your grade: A 🎉"); //Party emoji
            txtScore.setTextColor(Color.parseColor("#3CC0C5"));
        } else if (meanUncertainty < 15) {
            txtScore.setText("Your grade: B \uD83D\uDC4F"); //Clapping emoji
            txtScore.setTextColor(Color.parseColor("#78E8EC"));
        } else if (meanUncertainty < 25) {
            txtScore.setText("Your grade: C \uD83D\uDC4D"); //Thumbs up emoji
            txtScore.setTextColor(Color.parseColor("#8DDFE2"));
        } else if (meanUncertainty < 35) {
            txtScore.setText("Your grade: D \uD83D\uDE10"); //Neutral face emoji
            txtScore.setTextColor(Color.parseColor("#E09D80"));
        } else if (meanUncertainty < 45)  {
            txtScore.setText("Your grade: E \uD83D\uDC4E"); //Thumbs down
            txtScore.setTextColor(Color.parseColor("#EE8F67"));
        } else {
            txtScore.setText("Your grade: F \uD83D\uDCA9"); //Poop emoji
            txtScore.setTextColor(Color.parseColor("#FF763C"));
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