package com.example.mycameraapp;

import android.hardware.Sensor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
{

    public static int index = 0;
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/";

    private TextView textViewToDisplayRotation;
    private float[] acc = new float[3];
    private float[] mags = new float[3];
    private float[] values = new float[3];
    SensorManager sManager; //SensorManager lets you access the device's sensors

    private float camera_azimuth;
    private float camera_pitch;
    private float camera_roll;

    private TextView txt; //Text to be updated with the values of az, ro, tilt
    public static StringBuffer sb = new StringBuffer("Before"); //static so only one instance is shared

    private SensorEventListener mySensorEventListener = new SensorEventListener() //Used for receiving notifications from the SensorManager when there is new sensor data.


    {
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
        }

        public void onSensorChanged(SensorEvent event)
        {
            switch (event.sensor.getType())
            {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    acc = event.values.clone();
                    break;
            }

            if (mags != null && acc != null)
            {
                float[] gravity = new float[9];
                float[] magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, acc, mags);  //Computes the inclination matrix I as well as the rotation matrix R - getRotationMatrix
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        outGravity); //Rotates the supplied rotation matrix so it is expressed in a different coordinate system - remapCoordinateSystem

                SensorManager.getOrientation(outGravity, values); //Compute the devise orientation based on the rotation matrix


                float azimuth = Math.round(values[0] * 57.2957795f);
                float pitch = Math.round(values[1] * 57.2957795f);
                float roll = Math.round(values[2] * 57.2957795f);
                textViewToDisplayRotation.setText("azimuth = " + azimuth + "\npitch = " + pitch + "\nroll = " + roll);
                camera_azimuth = azimuth; //storing values to attributes
                camera_pitch = pitch; //storing values to attributes
                camera_roll = roll; //storing values to attributes
                mags = null;
                acc = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textViewToDisplayRotation = findViewById(R.id.textViewToDisplayRotation);
        txt = findViewById(R.id.txt); //Finding the textView in activity_main.xml
        txt.setText(sb); //Setting textView to StringBuffer's values

        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build() );

    }

    public void CameraButton(View view) {
        sb.setLength(0);
        sb.append("azimuth= " + camera_azimuth + ", pitch= " + camera_pitch + ", roll= " + camera_roll);
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



    @Override
    protected void onResume()
    {
        super.onResume();
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}