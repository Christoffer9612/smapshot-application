package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SelectPhotoActivity extends AppCompatActivity {
    private ShapeableImageView selTestPhoto, selDia303;
    private Button btnPhoto, btnBack;
    private boolean selectedTest, selectedDia;
    public Bundle bundleSelectedPhoto;
    public static JSONObject jsonObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        //Create bundle, storing info about selected photo (test or dia)
        bundleSelectedPhoto = new Bundle();

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Finding id:s from activity_select_photo.xml file
        selTestPhoto = findViewById(R.id.selTestPhoto);
        selDia303 = findViewById(R.id.selDia303);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnBack = findViewById(R.id.btnBack);

        //Setting stuff
        selTestPhoto.setImageResource(R.drawable.st_roch_test); // Might not need?
        selDia303.setImageResource(R.drawable.dia_303_12172); // Might not need since we set photos in .xml file instead!

        btnPhoto.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnPhoto.setTextColor(Color.parseColor("#444444"));
        btnPhoto.setTypeface(montserrat_medium);

        btnBack.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnBack.setTextColor(Color.parseColor("#444444"));
        btnBack.setTypeface(montserrat_medium);

    }

    public void openCustomCam(View view) {

        if (selectedDia == true) { //Fix so you only can select ONE photo at a time
            //Add your data to bundle
            bundleSelectedPhoto.putString("oldPhoto", "dia_303_12172");
            Intent intent = new Intent(this, CameraActivity.class);

            //Add the bundle to the intent
            intent.putExtras(bundleSelectedPhoto);
            startActivity(intent);

        } else if (selectedTest == true) {
            //Add your data to bundle
            bundleSelectedPhoto.putString("oldPhoto", "st_roch_test");
            Intent intent = new Intent(this, CameraActivity.class);

            //Add the bundle to the intent
            intent.putExtras(bundleSelectedPhoto);
            startActivity(intent);
        }
        Log.d("SELECT", "Select one old photo!"); // Display message in UI: "Please select one photo to retake"
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Refactor: merge with method below
    public void selectTest(View view) throws JSONException {

        if (!selectedTest) {
            int colorInt = getResources().getColor(R.color.smapshot_blue);
            ColorStateList csl = ColorStateList.valueOf(colorInt);

            selTestPhoto.setStrokeColor(csl);
            selTestPhoto.setStrokeWidth(25);

            selectedTest = true;
        } else {
            selTestPhoto.setStrokeWidth(0);
            selectedTest = false;
        }

        //Fetching json-file from /assets/ folder
        String test = fetchJSON("test_photo.json");
        try {
            jsonObj = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_test", az);
        bundleSelectedPhoto.putFloat("tilt_test", tilt);
        bundleSelectedPhoto.putFloat("roll_test", roll);
    }

    //Refactor: merge with method above
    public void selectDia(View view) throws JSONException {

        if (!selectedDia) {
            int colorInt = getResources().getColor(R.color.smapshot_blue);
            ColorStateList csl = ColorStateList.valueOf(colorInt);

            selDia303.setStrokeColor(csl);
            selDia303.setStrokeWidth(25);

            selectedDia = true;
        } else {
            selDia303.setStrokeWidth(0);
            selectedDia = false;
        }

        //Fetching json-file from /assets/ folder
        String test = fetchJSON("dia_303_12172.json");
        try {
            jsonObj = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_dia", az);
        bundleSelectedPhoto.putFloat("tilt_dia", tilt);
        bundleSelectedPhoto.putFloat("roll_dia", roll);
    }


    public String fetchJSON(String filename) {
        String json = null;
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //Converts StringBuilder that you receive from findValue method to a float
    public float sbToFloatAng(JSONObject obj, String key) throws JSONException {
        StringBuilder sb = findJSONParams(obj, key);
        String s = sb.toString();
        String[] parts = s.split(" ");
        String string_key = parts[3];
        double d = Double.parseDouble(string_key);
        float float_key = (float) d;

        return float_key;
    }

    //Used when finding the orientation angles from json-file, normalise the angles (0-360 degrees)
    public StringBuilder findJSONParams(JSONObject obj, String key)
            throws JSONException {
        StringBuilder value = new StringBuilder();
        JSONObject json_pose = obj.getJSONObject("pose");
        if (json_pose.has(key)){
            String angle_string = json_pose.optString(key);
            double d = Double.parseDouble(angle_string);
            int angle_int = (int) d;
            angle_int = angle_int % 360;
            angle_int = (angle_int + 360) % 360;
            angle_string = String.valueOf(angle_int);
            value.append(key + " from old_photo: " + angle_string);

        } else {
            value.append("No json key found!");
        }
        return value;
    }

}