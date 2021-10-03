package com.example.mycameraapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class CameraActivity extends MainActivity {
    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;
    private Button btnGoBack, btnCapture;
    private String currentPhotoPath;
    private TextView realTimeParams;
    private SensorManager sManager;
    public static float azimuthValue, tiltValue, rollValue;

    //Storing data to pass from one Activity to another
    Bundle bundle = new Bundle();

    private float[] acc, mags, values = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        realTimeParams = (TextView) findViewById(R.id.realTimeParams);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        realTimeParams.setTextColor(Color.parseColor("#FFFFFF"));
        realTimeParams.setTypeface(montserrat_medium);

        btnGoBack.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnGoBack.setTextColor(Color.parseColor("#444444"));
        btnGoBack.setTypeface(montserrat_medium);

        btnCapture.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnCapture.setTextColor(Color.parseColor("#444444"));
        btnCapture.setTypeface(montserrat_medium);

        //Open camera
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    acc = event.values.clone();
                    break;
            }

            if (mags != null && acc != null) {
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
                azimuthValue = azimuth; //Store values when taking photo
                tiltValue = pitch; //Store values when taking photo
                rollValue = roll; //Store values when taking photo
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
            //Add your data to bundle
            bundle.putFloat("azimuth", azimuthValue);
            bundle.putFloat("tilt", tiltValue);
            bundle.putFloat("roll", rollValue);
            openPostPicture(); //Jump to next Activity, displaying values from the captured photo
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

    public void openPostPicture() {
        Intent intent = new Intent(this, PostPicActivity.class);

        //Add the bundle to the intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

}