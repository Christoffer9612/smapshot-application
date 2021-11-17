package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;

public class SelectPhotoActivity extends AppCompatActivity {
    private ShapeableImageView testPhoto, diaPhoto;
    private Button btnPhoto, btnBack;
    private boolean selectedTest, selectedDia;
    public Bundle bundleSelectedPhoto;
    public static JSONObject jsonObj = null;
    private GeoPoint testPoint, diaPoint;
    private IMapController mapController;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        //Create bundle, storing info about selected photo (test or dia)
        bundleSelectedPhoto = new Bundle();

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        testPhoto = findViewById(R.id.selTestPhoto);
        diaPhoto = findViewById(R.id.selDia303);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnBack = findViewById(R.id.btnBack);

        testPhoto.setImageResource(R.drawable.st_roch_test); // Might not need?
        diaPhoto.setImageResource(R.drawable.dia_303_12172); // Might not need since we set photos in .xml file instead!

        setButton(btnPhoto, montserrat_medium);
        setButton(btnBack, montserrat_medium);


        //OpenStreetMap
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //Move the map to a default view point. For this, access the map controller:
        mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(46.780828, 6.64775); //Location of Yverdon-les-Bains, read location with Listener instead?
        mapController.setCenter(startPoint);

        //Adding geopoints of old photos
        testPoint = new GeoPoint(46.780828, 6.64775); //Hardcoded, fetch from json-file instead
        Marker testMarker = new Marker(map);
        testMarker.setTitle("Test photo from St Roch building, Yverdon.");
        addMarker(map, testPoint, testMarker);
        diaPoint = new GeoPoint(46.78596271776273, 6.648714295980383); //Hardcoded, fetch from json-file instead
        Marker diaMarker = new Marker(map);
        diaMarker.setTitle("Photo from 1999, Smapshot archives.");
        addMarker(map, diaPoint, diaMarker);

    }

    public void addMarker(MapView map, GeoPoint point, Marker marker) {
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        // Read your drawable from somewhere
        //Drawable dr = getResources().getDrawable(R.drawable.favicon_196);
        //Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
        //Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 20, 20, true));
        // Set your new, scaled drawable "d"
        //marker.setIcon(d);

    }

    //OpenStreetMap method
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    public void openCustomCam(View view) {

        if (selectedDia == true) { //Fix so you only can select ONE photo at a time
            //Add your data to bundle
            bundleSelectedPhoto.putString("oldPhoto", "dia_303_12172");
            Intent intent = new Intent(this, CameraActivity.class);

            //Add the bundle to the intent
            intent.putExtras(bundleSelectedPhoto);
            startActivity(intent);

        } else if (selectedTest == true) {
            //Add your data to bundle
            bundleSelectedPhoto.putString("oldPhoto", "st_roch_test");
            Intent intent = new Intent(this, CameraActivity.class);

            //Add the bundle to the intent
            intent.putExtras(bundleSelectedPhoto);
            startActivity(intent);
        }
        Log.d("SELECT", "Select one old photo!"); // Display message in UI: "Please select one photo to retake"
    }

    public void openMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //Refactor: merge with method below?
    public void selectTest(View view) throws JSONException {
        int colorInt = getResources().getColor(R.color.smapshot_blue);
        ColorStateList csl = ColorStateList.valueOf(colorInt);

        if (!selectedTest) {
            if (!selectedDia) {
                testPhoto.setStrokeColor(csl);
                testPhoto.setStrokeWidth(30);
                selectedTest = true;
            } else {
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(0);
                testPhoto.setStrokeColor(csl);
                testPhoto.setStrokeWidth(30);
                selectedTest = true;
                selectedDia = false;
            }
        } else {
            testPhoto.setStrokeWidth(0);
            selectedTest = false;
        }

        //Fetching json-file from /assets/ folder
        String testPhoto = fetchJSON("test_photo.json");
        try {
            jsonObj = new JSONObject(testPhoto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_test", az);
        bundleSelectedPhoto.putFloat("tilt_test", tilt);
        bundleSelectedPhoto.putFloat("roll_test", roll);
    }

    //Refactor: merge with method above?
    public void selectDia(View view) throws JSONException {
        int colorInt = getResources().getColor(R.color.smapshot_blue);
        ColorStateList csl = ColorStateList.valueOf(colorInt);

        if (!selectedDia) {
            if (!selectedTest) {
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(30);
                selectedDia = true;
            } else {
                testPhoto.setStrokeColor(csl);
                testPhoto.setStrokeWidth(0);
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(30);
                selectedDia = true;
                selectedTest = false;
            }
        } else {
            diaPhoto.setStrokeWidth(0);
            selectedDia = false;
        }

        //Fetching json-file from /assets/ folder
        String diaPhoto = fetchJSON("dia_303_12172.json");
        try {
            jsonObj = new JSONObject(diaPhoto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_dia", az);
        bundleSelectedPhoto.putFloat("tilt_dia", tilt);
        bundleSelectedPhoto.putFloat("roll_dia", roll);
    }


    public String fetchJSON(String filename) {
        String json = null;
        try {
            InputStream is = getAssets().open(filename);
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

    //Converts StringBuilder that you receive from findValue method to a float
    public float sbToFloatAng(JSONObject obj, String key) throws JSONException {
        StringBuilder sb = findJSONParams(obj, key);
        String s = sb.toString();
        String[] parts = s.split(" ");
        String string_key = parts[3];
        double d = Double.parseDouble(string_key);
        float float_key = (float) d;

        return float_key;
    }

    //Used when finding the orientation angles from json-file, normalise the angles (0-360 degrees)
    public StringBuilder findJSONParams(JSONObject obj, String key)
            throws JSONException {
        StringBuilder value = new StringBuilder();
        JSONObject json_pose = obj.getJSONObject("pose");
        if (json_pose.has(key)){
            String angle_string = json_pose.optString(key);
            double d = Double.parseDouble(angle_string);
            d = d % 360;
            d = (d + 360) % 360;
            angle_string = String.valueOf(d);
            value.append(key + " from old_photo: " + angle_string);

        } else {
            value.append("No json key found!");
        }
        return value;
    }

    private void setButton(Button button, Typeface font) {
        button.setTypeface(font);
        button.setTextColor(Color.parseColor("#444444"));
        button.setBackgroundColor(Color.parseColor("#E2E2E2"));
    }

}