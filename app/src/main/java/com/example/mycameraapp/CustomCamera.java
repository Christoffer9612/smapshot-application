package com.example.mycameraapp;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomCamera extends MainActivity {
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    private Button btnGoBack;
    String currentPhotoPath;

    private TextView realTimeParams;
    private float[] acc = new float[3];
    private float[] mags = new float[3];
    private float[] values = new float[3];
    SensorManager sManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);
        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        realTimeParams = (TextView) findViewById(R.id.realTimeParams);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Open camera
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
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

                float azimuth = values[0] * 57.2957795f;
                float pitch = values[1] * 57.2957795f;
                float roll = values[2] * 57.2957795f;
                realTimeParams.setText("azimuth = " + azimuth + "\npitch = " + pitch + "\nroll = " + roll);
                mags = null;
                acc = null;
            }
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = null;
            try {
                pictureFile = createImageFile();
                Log.d("CREATED", String.valueOf(pictureFile));
                galleryAddPic();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (pictureFile == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void takePhoto(View view) {
        if (camera != null) {
            camera.takePicture(null, null, mPictureCallback);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}