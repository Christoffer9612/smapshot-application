package com.example.mycameraapp;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{

    public static int index = 0;
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/";



    private TextView txt; //Text to be updated with the values of az, ro, tilt
    public static TextView json1, json2, json3; //Displaying json-values from json-file
    public static StringBuffer sb = new StringBuffer("Before"); //static so only one instance is shared
    public static JSONObject jsonObj = null;
    public Button button, button2;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");

        txt = findViewById(R.id.txt); //Finding the textView in activity_main.xml
        txt.setText(sb); //Setting textView to StringBuffer's values
        txt.setTypeface(type);

        button = findViewById(R.id.button);
        button.setTypeface(type);
        button2 = findViewById(R.id.button2);
        button2.setTypeface(type);



        json1 = findViewById(R.id.json1);
        json1.setTypeface(type);
        json2 = findViewById(R.id.json2);
        json2.setTypeface(type);
        json3 = findViewById(R.id.json3);
        json3.setTypeface(type);

        String test = loadJSONFromAsset();

        try {
            jsonObj = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            json1.setText(findValue(jsonObj, "azimuth"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json2.setText(findValue(jsonObj, "tilt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json3.setText(findValue(jsonObj, "roll"));
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
            InputStream is = getAssets().open("test.json");
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

    public void cameraButton(View view) {
        //sb.setLength(0);
        //sb.append("azimuth= " + camera_azimuth + ", tilt= " + camera_tilt + ", roll= " + camera_roll);
        //Updates StringBuffer to angles when pressing "TAKE PHOTO" button
        txt.setText(sb);

        index++;
        String file = directory + index + ".jpg"; //creates file with directory name + increasing index
        File newFile = new File(file);
        try {
            newFile.createNewFile();
            openPostPicture(); //Jump to next Activity, where we want to display values from the captured photo
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri outputFileUri = Uri.fromFile(newFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivity(cameraIntent);
    }

    public void openPostPicture() {
        Intent intent = new Intent(this, PostPicture.class);
        startActivity(intent);
    }

    public void openCustomCam(View view) {
        Intent intent = new Intent(this, CustomCamera.class);
        startActivity(intent);
    }

}