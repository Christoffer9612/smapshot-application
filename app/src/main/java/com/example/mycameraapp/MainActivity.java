package com.example.mycameraapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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

    public static TextView azimuth, tilt, roll, intro; //intro is displaying json-values from json-file
    public static JSONObject jsonObj = null;
    public Button btnTakePhoto, btnLoadJSON, btnTutorial, btnProfile, btnSmapshot;
    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Finding id:s from activity_main.xml file
        thumbnail = findViewById(R.id.thumbnail);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnLoadJSON = findViewById(R.id.loadJson);
        btnTutorial = findViewById(R.id.btnTutorial);
        btnProfile = findViewById(R.id.btnProfile);
        btnSmapshot = findViewById(R.id.btnSmapshot);
        azimuth = findViewById(R.id.azimuth);
        tilt = findViewById(R.id.tilt);
        roll = findViewById(R.id.roll);
        intro = findViewById(R.id.intro);

        //Setting stuff
        thumbnail.setImageResource(R.drawable.thumbnail);
        thumbnail.setAlpha(191); //0 is fully transparent, 255 is fully opaque (75 % opacity)
        azimuth.setTextColor(Color.parseColor("#444444"));
        azimuth.setTypeface(montserrat_medium);

        tilt.setTextColor(Color.parseColor("#444444"));
        tilt.setTypeface(montserrat_medium);

        roll.setTextColor(Color.parseColor("#444444"));
        roll.setTypeface(montserrat_medium);

        intro.setGravity(Gravity.CENTER);

        setButton(btnTakePhoto);
        setButton(btnLoadJSON);
        setButton(btnTutorial);
        setButton(btnProfile);
        setButton(btnSmapshot);

        //Fetching json-file from /assets/ folder
        String test = loadJSONFromAsset();
        try {
            jsonObj = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build() );
    }

    public String loadJSONFromAsset() { //Returns JSON string
        String json = null;
        try {
            InputStream is = getAssets().open("test_photo.json");
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

    //Converts StringBuilder that you receive from findValue method to a float
    public float sbToFloatAngles(JSONObject obj, String key) throws JSONException {
        StringBuilder sb = findValue(obj, key);
        String s = sb.toString();
        String[] parts = s.split(" ");
        String string_key = parts[3];
        double d = Double.parseDouble(string_key);
        float float_key = (float) d;

        return float_key;
    }

    public void openCustomCam(View view) { //Not used, replaced with selectPhoto
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoActivity.class);
        startActivity(intent);
    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    public void getJSONValues(View view) throws JSONException {
        azimuth.setText(findValue(jsonObj, "azimuth"));
        tilt.setText(findValue(jsonObj, "tilt"));
        roll.setText(findValue(jsonObj, "roll"));
    }

    public void setButton(Button button) {
        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");
        button.setTypeface(montserrat_medium);
        button.setTextColor(Color.parseColor("#444444"));
        button.setBackgroundColor(Color.parseColor("#E2E2E2"));
    }
}