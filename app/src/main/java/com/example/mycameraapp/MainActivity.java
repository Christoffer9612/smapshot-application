package com.example.mycameraapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.StrictMode;
import android.view.View;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static TextView azimuth, tilt, roll, intro;
    public static JSONObject jsonObj;
    public Button btnFindPhoto, btnLoadJSON, btnTutorial, btnProfile, btnSmapshot;
    private ImageView thumbnail;
    private FusedLocationProviderClient client;

    private Utils utils = new Utils(this);
    private RequestAPI requestAPI = new RequestAPI(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(), "fonts/montserrat_medium.ttf");

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

        requestPermission();

        //"Configuring" buttons, photos, colors, font, etc.
        thumbnail.setImageResource(R.drawable.thumbnail_nofade);
        thumbnail.setAlpha(200); //value: [0-255]. Where 0 is fully transparent and 255 is fully opaque

        utils.setText(azimuth, montserrat_medium);
        utils.setText(tilt, montserrat_medium);
        utils.setText(roll, montserrat_medium);
        utils.setText(intro, montserrat_medium);

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build() );

        JSONObject diaJSON = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = requestAPI.requestJsonFile();
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }



    /* Requesting access to: camera, storage and external memory */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE}, 1);
    }

    /* Used when finding the coordinates from json-file */
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

    /* Converts StringBuilder that you get from findCoordinates() to float */
    public float sbToFloatCoord(JSONObject obj, String key) throws JSONException {
        StringBuilder sb = findCoordinates(obj, key);
        String s = sb.toString();
        String[] parts = s.split(" ");
        String string_key = parts[3];
        double d = Double.parseDouble(string_key);
        float float_key = (float) d;

        return float_key;
    }



    /* Methods for opening new Activities */
    public void openFindPhoto(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openHowToUse(View view) {
        Intent intent = new Intent(this, HowToUseActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Animation fade in, fade out
    }

    public void openSmapshot(View view) {
        Intent intent = new Intent(MainActivity.this, SmapshotActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Animation fade in, fade out
    }

    /* Currently, there are no methods for the two buttons: "Scoreboard" and "Your profile" */
    
}