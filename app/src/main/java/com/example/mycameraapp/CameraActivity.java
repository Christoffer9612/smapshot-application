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
    private TextView realTimeParams_az, realTimeParams_ti, realTimeParams_ro, transparency;
    private SensorManager sManager;
    public static float azimuthValue, tiltValue, rollValue;
    private ImageView overlayPhoto;
    private Utils utils = new Utils(this);
    private ImageView right, left;



    //Storing data to pass from one Activity to another
    Bundle bundle = new Bundle();
    Bundle bundleSelectedPhoto = new Bundle(); //Bundle from: SelectPhotoActivity

    private float[] acc, mags, values = new float[3];

    //values for JSON-file params
    Float azimuthOld = null;
    Float tiltOld = null;
    Float rollOld = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //Setting a change listener to the SeekBar (slider)
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        //int progress = seekBar.getProgress();
        int progress = 100;

        transparency = findViewById(R.id.transparency);
        transparency.setText(progress + " %");

        overlayPhoto = findViewById(R.id.overlayPhoto);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        right = (ImageView) findViewById(R.id.btnright);
        left = (ImageView) findViewById(R.id.btnleft);

        btnGoBack = findViewById(R.id.btnGoBack);
        btnCapture = findViewById(R.id.btnCapture);
        frameLayout = findViewById(R.id.frameLayout);
        realTimeParams_az = findViewById(R.id.realTimeParams_az);
        realTimeParams_ti = findViewById(R.id.realTimeParams_ti);
        realTimeParams_ro = findViewById(R.id.realTimeParams_ro);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        realTimeParams_az.setTextColor(Color.parseColor("#FFFFFF"));
        realTimeParams_az.setTypeface(montserrat_medium);

        realTimeParams_ti.setTextColor(Color.parseColor("#FFFFFF"));
        realTimeParams_ti.setTypeface(montserrat_medium);

        realTimeParams_ro.setTextColor(Color.parseColor("#FFFFFF"));
        realTimeParams_ro.setTypeface(montserrat_medium);

        utils.setButton(btnGoBack, montserrat_medium);
        utils.setButton(btnCapture, montserrat_medium);

        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#FF763C"), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.parseColor("#FF763C"), PorterDuff.Mode.SRC_IN);

        //Open camera
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

        //Get the bundle, refactor: create as method?
        bundleSelectedPhoto = getIntent().getExtras();

        //Extract name of old photo selected (test vs. dia) to display on top of camera as transparent
        if (bundleSelectedPhoto.getString("oldPhoto").equals("st_roch_test")) {
            overlayPhoto.setImageResource(R.drawable.st_roch_test);
        } else if (bundleSelectedPhoto.getString("oldPhoto").equals("dia_303_12172")) {
            overlayPhoto.setImageResource(R.drawable.dia_303_12172);
        }
        overlayPhoto.setAlpha(progress * (int) 2.55);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //Updates continuously as the user slides the bar
            transparency.setText(progress + " %");
            overlayPhoto.setAlpha(progress * (int) 2.55);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //Called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //Called after the user finishes moving the SeekBar
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
                float declination = getDeclination();

                //normalise angles 0 - 360 degrees
                //add declination to azimuth
                float azimuth = normaliseAngles(Math.round(Math.toDegrees(values[0]) + declination));

                float pitch = normaliseAngles(Math.round(Math.toDegrees(values[1])));
                pitch = 360 - pitch;

                float roll = normaliseAngles(Math.round(Math.toDegrees(values[2])));

                // Loading in old az, ti, roll from old selected photo (dia vs. test, based on photo name stored in bundle)
                if (bundleSelectedPhoto.getString("oldPhoto").equals("st_roch_test")) {
                    azimuthOld = bundleSelectedPhoto.getFloat("azimuth_test");
                    tiltOld = bundleSelectedPhoto.getFloat("tilt_test");
                    rollOld = bundleSelectedPhoto.getFloat("roll_test");
                } else if (bundleSelectedPhoto.getString("oldPhoto").equals("dia_303_12172")) {
                    azimuthOld = bundleSelectedPhoto.getFloat("azimuth_dia");
                    tiltOld = bundleSelectedPhoto.getFloat("tilt_dia");
                    rollOld = bundleSelectedPhoto.getFloat("roll_dia");
                }

                //setting the colors of real time parameters, green if within range of 5.0
                setRealTimeParamsColor(azimuthOld, azimuth, "azimuth");
                setRealTimeParamsColor(tiltOld, pitch, "tilt");
                setRealTimeParamsColor(rollOld, roll, "roll");

                //determine which picture to illustrate direction instruction to user.
                float [] azRotation = wayOfRotation(azimuthOld, azimuth);
                setImageInstruction(azRotation, azimuthOld, azimuth);

                realTimeParams_az.setText("azimuth = " + (int) azimuth);
                realTimeParams_ti.setText("tilt = " + (int) pitch);
                realTimeParams_ro.setText("roll = " + (int) roll);
                azimuthValue = azimuth; //Store values when taking photo
                tiltValue = pitch; //Store values when taking photo
                rollValue = roll; //Store values when taking photo
                mags = null;
                acc = null;
            }
        }
    };

    public float getDeclination() {

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
        return declination;
    }

    public float [] wayOfRotation(float angleOld, float angleRealTime) {

        float [] rotations = new float[3];

        float diff = Math.abs(angleOld - angleRealTime);
        float clockwise = (360 - angleRealTime) + angleOld;
        float counterclockwise = angleRealTime + (360 - angleOld);

        rotations[0] = diff;
        Log.d("test", String.valueOf(diff));
        rotations[1] = clockwise;
        rotations[2] = counterclockwise;

        return rotations;

    }
    public void setImageInstruction(float [] rotations, float angleOld, float realTimeAngle) {


        if(Math.round(rotations[0]) < 50.0) {
            right.clearAnimation();
            left.clearAnimation();
            right.setVisibility(View.INVISIBLE);
            left.setVisibility(View.INVISIBLE);
        }

        if (rotations[0] < rotations[1] && rotations[0] < rotations[2] && realTimeAngle < angleOld) {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.GONE);
            return;
        } else if (rotations[0] < rotations[1] && rotations[0] < rotations[2] && realTimeAngle > angleOld) {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.GONE);
        } else if (rotations[1] < rotations[0] && rotations[1] < rotations[2]) {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.GONE);
        } else {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.GONE);
        }
    }

    public void setRealTimeParamsColor(float angleOld, float angleRealTime, String orientationAngle) {

        if (orientationAngle.equals("azimuth")) {

            if (Math.abs(angleRealTime - angleOld) < 5.0) {
                realTimeParams_az.setTextColor(Color.GREEN);
                right.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
            } else {
                realTimeParams_az.setTextColor(Color.WHITE);
            }
        }

        if (orientationAngle.equals("tilt")) {

        if (Math.abs(angleRealTime - angleOld) < 5.0) {
            realTimeParams_ti.setTextColor(Color.GREEN);
        } else {
            realTimeParams_ti.setTextColor(Color.WHITE);
            }
        }

        if(orientationAngle.equals("roll")) {

        if (Math.abs(angleRealTime - angleOld) < 5.0) {
            realTimeParams_ro.setTextColor(Color.GREEN);
        } else {
            realTimeParams_ro.setTextColor(Color.WHITE);
            }
        }

    }

    public float normaliseAngles (float angle) {
        angle = angle % 360;
        angle = (angle + 360) % 360;
        return angle;
    }

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

    public void goToSelect(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
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
        String timeStamp = new SimpleDateFormat("yy_MM_dd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Log.d("FILEPATH", "" + currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void openPostPicture() {
        Intent intent = new Intent(this, ResultActivity.class);

        //Passing bundles to next intent (result screen to display json values)
        intent.putExtras(bundle);
        intent.putExtras(bundleSelectedPhoto);

        startActivity(intent);
    }


}