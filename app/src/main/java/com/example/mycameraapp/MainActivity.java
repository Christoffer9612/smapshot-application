package com.example.mycameraapp;

import android.hardware.Sensor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private TextView textViewToDisplayRotation;
    private float[] acc = new float[3];
    private float[] mags = new float[3];
    private float[] values = new float[3];
    SensorManager sManager;
    private SensorEventListener mySensorEventListener = new SensorEventListener()
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
                SensorManager.getRotationMatrix(gravity, magnetic, acc, mags);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        outGravity);
                SensorManager.getOrientation(outGravity, values);

                float azimuth = Math.round(values[0] * 57.2957795f);
                float pitch = Math.round(values[1] * 57.2957795f);
                float roll = Math.round(values[2] * 57.2957795f);
                textViewToDisplayRotation.setText("azimuth = " + azimuth + "\npitch = " + pitch + "\nroll = " + roll);
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