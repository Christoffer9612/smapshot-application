package com.example.mycameraapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static TextView azimuth, tilt, roll;
    public static JSONObject jsonObj;
    public Button btnFindPhoto, btnLoadJSON, btnTutorial, btnProfile, btnSmapshot;
    private ImageView thumbnail;
    private Utils utils = new Utils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Finding id:s from activity_main.xml file
        thumbnail = findViewById(R.id.thumbnail);
        btnFindPhoto = findViewById(R.id.btnFindPhoto);
        btnLoadJSON = findViewById(R.id.loadJson);
        btnTutorial = findViewById(R.id.btnTutorial);
        btnProfile = findViewById(R.id.btnProfile);
        btnSmapshot = findViewById(R.id.btnSmapshot);
        azimuth = findViewById(R.id.azimuth);
        tilt = findViewById(R.id.tilt);
        roll = findViewById(R.id.roll);

        //"Configuring" buttons, photos, colors, font, etc. in main screen
        thumbnail.setImageResource(R.drawable.thumbnail);
        thumbnail.setAlpha(191); //0 is fully transparent, 255 is fully opaque (currently: 75 % opacity)

        utils.setText(azimuth, montserrat_medium);
        utils.setText(tilt, montserrat_medium);
        utils.setText(roll, montserrat_medium);

        utils.setButton(btnFindPhoto, montserrat_medium);
        utils.setButton(btnLoadJSON, montserrat_medium);
        utils.setButton(btnTutorial, montserrat_medium);
        utils.setButton(btnProfile, montserrat_medium);
        utils.setButton(btnSmapshot, montserrat_medium);

        //Fetching json-file from /assets/ folder
        JsonFinder jsonFinder = new JsonFinder(this);
        String testPhoto = jsonFinder.JSONFromAsset(this, "test_photo.json");
        try {
            jsonObj = new JSONObject(testPhoto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build() );
    }


    //Used when finding the orientation angles from json-file, normalise the angles (0-360 degrees)
    public StringBuilder findValue(JSONObject obj, String key)
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

    //Used when finding the coordinates from json-file
    public StringBuilder findCoordinates(JSONObject obj, String key)
            throws JSONException {
        StringBuilder value = new StringBuilder();
        JSONObject json_pose = obj.getJSONObject("pose");
        if (json_pose.has(key)){
            String angle_string = json_pose.optString(key);
            double d = Double.parseDouble(angle_string);
            String coordinates_string = String.valueOf(d);
            value.append(key + " from old_photo: " + coordinates_string);
        } else {
            value.append("No json key found!");
        }
        return value;
    }

    //Converts StringBuilder that you get from findCoordinates method to float
    public float sbToFloatCoord(JSONObject obj, String key) throws JSONException {
        StringBuilder sb = findCoordinates(obj, key);
        String s = sb.toString();
        String[] parts = s.split(" ");
        String string_key = parts[3];
        double d = Double.parseDouble(string_key);
        float float_key = (float) d;

        return float_key;
    }

    //Methods for opening new Activities
    public void openSelectPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Animation fade in, fade out
    }

    public void openSmapshot() {
        //To be implemented...
    }

    public void openChallenge(View view) {
        //To be implemented...
    }

    public void openProfile() {
        //To be implemented...
    }
}