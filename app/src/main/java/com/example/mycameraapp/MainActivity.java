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

    public static TextView azimuth, tilt, roll, intro;
    public static JSONObject jsonObj;
    public Button btnFindPhoto, btnLoadJSON, btnTutorial, btnProfile, btnSmapshot;
    private ImageView thumbnail;

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
        intro = findViewById(R.id.intro);

        //"Configuring" buttons, photos, etc. in main screen (using setter methods)
        thumbnail.setImageResource(R.drawable.thumbnail);
        thumbnail.setAlpha(191); //0 is fully transparent, 255 is fully opaque (currently: 75 % opacity)

        intro.setGravity(Gravity.CENTER); //Centering intro text

        setText(azimuth, montserrat_medium);
        setText(tilt, montserrat_medium);
        setText(roll, montserrat_medium);

        setButton(btnFindPhoto, montserrat_medium);
        setButton(btnLoadJSON, montserrat_medium);
        setButton(btnTutorial, montserrat_medium);
        setButton(btnProfile, montserrat_medium);
        setButton(btnSmapshot, montserrat_medium);

        //Fetching json-file from /assets/ folder
        String testPhoto = loadJSONFromAsset();
        try {
            jsonObj = new JSONObject(testPhoto);
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
            InputStream is = getAssets().open("test_photo.json"); // Remove in the future, no need to display test_photo.json on home screen
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

    public void getJSONValues(View view) throws JSONException {
        azimuth.setText(findValue(jsonObj, "azimuth"));
        tilt.setText(findValue(jsonObj, "tilt"));
        roll.setText(findValue(jsonObj, "roll"));
    }


    //Setting button color and font for design purposes
    public void setButton(Button button, Typeface font) {
        button.setTypeface(font);
        button.setTextColor(Color.parseColor("#444444"));
        button.setBackgroundColor(Color.parseColor("#E2E2E2"));
    }

    //Setting text color and font for design purposes
    public void setText(TextView txt, Typeface font) {
        txt.setTypeface(font);
        txt.setTextColor(Color.parseColor("#444444"));
    }

    //Methods for opening new Activities
    public void openSelectPhoto(View view) {
        Intent intent = new Intent(this, SelectPhotoActivity.class);
        startActivity(intent);
    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    public void openSmapshot() {
        //To be implemented...
    }

    public void openChallenge() {
        //To be implemented...
    }

    public void openProfile() {
        //To be implemented...
    }
}