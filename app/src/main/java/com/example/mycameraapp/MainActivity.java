package com.example.mycameraapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
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

    private TextView txt; //Text to be updated with the values of az, ro, tilt
    public static TextView azimuth, tilt, roll; //Displaying json-values from json-file
    public static JSONObject jsonObj = null;
    public Button btnTakePhoto, btnLoadJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnTakePhoto.setTextColor(Color.parseColor("#444444"));
        btnTakePhoto.setTypeface(montserrat_medium);

        btnLoadJSON = findViewById(R.id.loadJson);
        btnLoadJSON.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnLoadJSON.setTextColor(Color.parseColor("#444444"));
        btnLoadJSON.setTypeface(montserrat_medium);

        azimuth = findViewById(R.id.azimuth);
        azimuth.setTextColor(Color.parseColor("#444444"));
        azimuth.setTypeface(montserrat_medium);

        tilt = findViewById(R.id.tilt);
        tilt.setTextColor(Color.parseColor("#444444"));
        tilt.setTypeface(montserrat_medium);

        roll = findViewById(R.id.roll);
        roll.setTextColor(Color.parseColor("#444444"));
        roll.setTypeface(montserrat_medium);

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
            InputStream is = getAssets().open("master_thesis_smapshot_1_bw.json");
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

    public StringBuilder findValue(JSONObject obj, String key)
            throws JSONException {
        StringBuilder value = new StringBuilder();
        JSONObject json_pose = obj.getJSONObject("pose");
        if (json_pose.has(key)){
            value.append(key + " from json: " + json_pose.optString(key));
        } else {
            value.append("No json key found!");
        }
        return value;
    }

    public void openCustomCam(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void getJSONValues(View view) throws JSONException {
        azimuth.setText(findValue(jsonObj, "azimuth"));
        tilt.setText(findValue(jsonObj, "tilt"));
        roll.setText(findValue(jsonObj, "roll"));
    }

}