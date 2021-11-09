package com.example.mycameraapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;

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
    private TextView realTimeParams, transparency;
    private SensorManager sManager;
    public static float azimuthValue, tiltValue, rollValue;
    private ImageView testPhoto;

    //Storing data to pass from one Activity to another
    Bundle bundle = new Bundle();

    private float[] acc, mags, values = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        transparency = findViewById(R.id.transparency);
        transparency.setText(progress + " %");

        testPhoto = (ImageView) findViewById(R.id.testPhoto);

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

        testPhoto = (ImageView) findViewById(R.id.testPhoto);

        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#FF763C"), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.parseColor("#FF763C"), PorterDuff.Mode.SRC_IN);

        //Open camera
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            transparency.setText(progress + " %");
            testPhoto.setImageResource(R.drawable.st_roch_test);
            testPhoto.setAlpha(progress * (int) 2.55);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

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
                float[] gravity = new float[9]; //the rotation matrix to be transformed
                float[] magnetic = new float[9]; //the inclination matrix
                SensorManager.getRotationMatrix(gravity, magnetic, acc, mags); //computes the rotation matrix (gravity)
                float[] outGravity = new float[9]; //the transformed rotation matrix.

                //Rotates the supplied rotation matrix (gravity) so it is expressed in a different coordinate system (out gravity)
                SensorManager.remapCoordinateSystem(gravity,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        outGravity);

                //Computes the device's orientation based on the rotation matrix
                SensorManager.getOrientation(outGravity, values);


                //compensating for magnetic declination;
                float latitude = 0;
                float longitude = 0;
                float altitude = 0;

                //get coordinates from JSON-file
                try {
                    latitude = sbToFloatCoord(jsonObj, "latitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    longitude = sbToFloatCoord(jsonObj, "longitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    altitude = sbToFloatCoord(jsonObj, "altitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //calculate declination
                GeomagneticField geoField = new GeomagneticField(latitude, longitude, altitude, System.currentTimeMillis());
                float declination = geoField.getDeclination();

                //add to azimuth
                float azimuth = Math.round(Math.toDegrees(values[0]) + declination);

                //normalise angles 0 - 360 degrees
                azimuth = azimuth % 360;
                azimuth = (azimuth + 360) % 360;

                float pitch = Math.round(Math.toDegrees(values[1]));

                pitch = pitch % 360;
                pitch = (pitch + 360) % 360;

                float roll = Math.round(Math.toDegrees(values[2]));

                roll = roll % 360;
                roll = (roll + 360) % 360;

                realTimeParams.setText("azimuth = " + azimuth + "\ntilt = " + pitch + "\nroll = " + roll);
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
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
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