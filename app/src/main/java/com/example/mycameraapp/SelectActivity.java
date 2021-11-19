package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
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

public class SelectActivity extends AppCompatActivity {
    private ShapeableImageView testPhoto, diaPhoto;
    private Button btnPhoto, btnBack;
    private boolean selectedTest, selectedDia;
    public Bundle bundleSelectedPhoto;
    public static JSONObject jsonObj = null;
    private GeoPoint testPoint, diaPoint;
    private Marker testMarker, diaMarker;
    private IMapController mapController;
    private MapView map;
    private JsonFinder jsonFinder = new JsonFinder(this);
    private Utils utils = new Utils(this);
    private com.google.android.material.imageview.ShapeableImageView selTestPhoto, selDia303;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Create bundle, storing info about selected photo (test or dia)
        bundleSelectedPhoto = new Bundle();

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        testPhoto = findViewById(R.id.selTestPhoto);
        diaPhoto = findViewById(R.id.selDia303);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnBack = findViewById(R.id.btnBack);
        selTestPhoto = findViewById(R.id.selTestPhoto);
        selDia303 = findViewById(R.id.selDia303);

        testPhoto.setImageResource(R.drawable.st_roch_test); // Might not need?
        diaPhoto.setImageResource(R.drawable.dia_303_12172); // Might not need since we set photos in .xml file instead!

        utils.setButton(btnPhoto, montserrat_medium);
        utils.setButton(btnBack, montserrat_medium);


        //OpenStreetMap
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //Move the map to a default view point. For this, access the map controller:
        mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(46.780828, 6.64775); //Location of Yverdon-les-Bains, read location with Listener instead?
        mapController.setCenter(startPoint);

        //Using JsonFinder to get key-values in json-files under /assets/
        Double latitudeTest = null;
        Double longitudeTest = null;
        String jsonTest = jsonFinder.JSONFromAsset(this, "test_photo.json");
        try {
            JSONObject jsonObjTest = new JSONObject(jsonTest);
            jsonObjTest = jsonObjTest.getJSONObject("pose"); //Json object within a json object...
            latitudeTest = jsonFinder.getValue(jsonObjTest, "latitude");
            longitudeTest = jsonFinder.getValue(jsonObjTest, "longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Double latitudeDia = null;
        Double longitudeDia = null;
        String jsonDia = jsonFinder.JSONFromAsset(this, "dia_303_12172.json");
        try {
            JSONObject jsonObjDia = new JSONObject(jsonDia);
            jsonObjDia = jsonObjDia.getJSONObject("pose"); //Json object within a json object...
            latitudeDia = jsonFinder.getValue(jsonObjDia, "latitude");
            longitudeDia = jsonFinder.getValue(jsonObjDia, "longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adding geopoints of old photos
        testPoint = new GeoPoint(latitudeTest, longitudeTest);
        testMarker = new Marker(map);
        testMarker.setTitle("Test photo from St Roch building, Yverdon.");
        addMarker(map, testPoint, testMarker);
        //Listener to know when user clicks on Marker on map
        testMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView map) {
                marker.showInfoWindow();
                map.getController().animateTo(marker.getPosition());
                Log.d("CLICKED", "Test clicked");
                try {
                    selectTest(selTestPhoto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        diaPoint = new GeoPoint(latitudeDia, longitudeDia);
        diaMarker = new Marker(map);
        diaMarker.setTitle("Photo from 1999, Smapshot archives.");
        addMarker(map, diaPoint, diaMarker);
        //Listener to know when user clicks on Marker on map
        diaMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView map) {
                marker.showInfoWindow();
                map.getController().animateTo(marker.getPosition());
                Log.d("CLICKED", "Dia clicked");
                try {
                    selectDia(selDia303);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

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
                testMarker.showInfoWindow();
            } else {
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(0);
                testPhoto.setStrokeColor(csl);
                testPhoto.setStrokeWidth(30);
                selectedTest = true;
                testMarker.showInfoWindow();
                selectedDia = false;
            }
        } else {
            testPhoto.setStrokeWidth(0);
            selectedTest = false;
            testMarker.closeInfoWindow();
        }

        //Fetching json-file from /assets/ folder
        String testPhoto = jsonFinder.JSONFromAsset(this,"test_photo.json");
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

        mapController.animateTo(testPoint);
    }

    //Refactor: merge with method above?
    public void selectDia(View view) throws JSONException {
        int colorInt = getResources().getColor(R.color.smapshot_blue);
        ColorStateList csl = ColorStateList.valueOf(colorInt);

        diaMarker.showInfoWindow();

        if (!selectedDia) {
            if (!selectedTest) {
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(30);
                selectedDia = true;
                diaMarker.showInfoWindow();
            } else {
                testPhoto.setStrokeColor(csl);
                testPhoto.setStrokeWidth(0);
                diaPhoto.setStrokeColor(csl);
                diaPhoto.setStrokeWidth(30);
                selectedDia = true;
                diaMarker.showInfoWindow();
                selectedTest = false;
            }
        } else {
            diaPhoto.setStrokeWidth(0);
            selectedDia = false;
            diaMarker.closeInfoWindow();
        }

        //Fetching json-file from /assets/ folder
        String diaPhoto = jsonFinder.JSONFromAsset(this,"dia_303_12172.json");
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

        mapController.animateTo(diaPoint);
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
    public StringBuilder findJSONParams(JSONObject obj, String key) throws JSONException {
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

}