package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ShapeableImageView imageOnePhoto, imageTwoPhoto;
    private Button btnPhoto, btnBack;
    private boolean selectedImageOne, selectedImageTwo;
    public Bundle bundleSelectedPhoto;
    public static JSONObject jsonObj = null;
    private GeoPoint imageOnePoint, imageTwoPoint;
    private Marker imageOneMarker, imageTwoMarker;
    private IMapController mapController;
    private MapView map;
    private JsonFinder jsonFinder = new JsonFinder(this);
    private Utils utils = new Utils(this);
    private com.google.android.material.imageview.ShapeableImageView selImageOne, selImageTwo;
    private FusedLocationProviderClient client;
    private TextView txtImageTwoDistance, txtImageOneDistance;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Create bundle, storing info about selected photo (imageOne or imageTwo)
        bundleSelectedPhoto = new Bundle();

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        imageOnePhoto = findViewById(R.id.selTestPhoto);
        imageTwoPhoto = findViewById(R.id.selDia303);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnBack = findViewById(R.id.btnBack);
        selImageOne = findViewById(R.id.selTestPhoto);
        selImageTwo = findViewById(R.id.selDia303);
        txtImageTwoDistance = findViewById(R.id.txtDiaDistance);
        txtImageOneDistance = findViewById(R.id.txtTestDistance);

        imageOnePhoto.setImageResource(R.drawable.st_roch_test); // Might not need?
        imageTwoPhoto.setImageResource(R.drawable.dia_303_12172); // Might not need since we set photos in .xml file instead!

        utils.setButton(btnPhoto, montserrat_medium);
        btnPhoto.getBackground().setAlpha(100); //Proceed button transparent
        btnPhoto.setTextColor(Color.rgb(204,204,204)); //Proceed button text transparent
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
        mapController.setZoom(16);

        //Using JsonFinder to get key-values in json-files under /assets/
        Double latitudeImageOne = null;
        Double longitudeImageOne = null;
        String jsonImageOne = jsonFinder.JSONFromAsset(this, "test_photo.json");
        try {
            JSONObject jsonObjImageOne = new JSONObject(jsonImageOne);
            jsonObjImageOne = jsonObjImageOne.getJSONObject("pose"); //Json object within a json object...
            latitudeImageOne = jsonFinder.getValue(jsonObjImageOne, "latitude");
            longitudeImageOne = jsonFinder.getValue(jsonObjImageOne, "longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Double latitudeImageTwo = null;
        Double longitudeImageTwo = null;
        String jsonImageTwo = jsonFinder.JSONFromAsset(this, "dia_303_12172.json");
        try {
            JSONObject jsonObjImageTwo = new JSONObject(jsonImageTwo);
            jsonObjImageTwo = jsonObjImageTwo.getJSONObject("pose"); //Json object within a json object...
            latitudeImageTwo = jsonFinder.getValue(jsonObjImageTwo, "latitude");
            longitudeImageTwo = jsonFinder.getValue(jsonObjImageTwo, "longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adding geopoints of old photos
        imageOnePoint = new GeoPoint(latitudeImageOne, longitudeImageOne);
        imageOneMarker = new Marker(map);
        imageOneMarker.setTitle("Test photo from St Roch building, Yverdon.");
        addMarker(map, imageOnePoint, imageOneMarker);
        //Listener to know when user clicks on Marker on map
        imageOneMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView map) {
                marker.showInfoWindow();
                map.getController().animateTo(marker.getPosition());
                Log.d("CLICKED", "Test clicked");
                try {
                    selectImageOne(selImageOne);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        imageTwoPoint = new GeoPoint(latitudeImageTwo, longitudeImageTwo);
        imageTwoMarker = new Marker(map);
        imageTwoMarker.setTitle("Photo from 1999, Smapshot archives.");
        addMarker(map, imageTwoPoint, imageTwoMarker);
        //Listener to know when user clicks on Marker on map
        imageTwoMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView map) {
                marker.showInfoWindow();
                map.getController().animateTo(marker.getPosition());
                Log.d("CLICKED", "Dia clicked");
                try {
                    selectImageTwo(selImageTwo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        //Getting current location
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Double finalLatitudeImageOne = latitudeImageOne;
        Double finalLongitudeImageOne = longitudeImageOne;
        Double finalLatitudeImageTwo = latitudeImageTwo;
        Double finalLongitudeImageTwo = longitudeImageTwo;
        client.getLastLocation().addOnSuccessListener(SelectActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double currentLat = location.getLatitude();
                    double currentLon = location.getLongitude();
                    Log.d("COORD", "Lat: " + location.getLatitude() + ", Long: " + location.getLongitude());
                    GeoPoint currentPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    Marker currentMarker = new Marker(map);
                    addMarker(map, currentPoint, currentMarker);
                    mapController.setCenter(currentPoint);
                    // Read your drawable from somewhere
                    Drawable dr = getResources().getDrawable(R.drawable.blue_location);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    // Scale it to 50 x 50
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
                    // Set your new, scaled drawable "d"
                    currentMarker.setIcon(d);
                    currentMarker.setTitle("Your current location.");

                    int distanceOne = distance(currentLat, finalLatitudeImageOne, currentLon, finalLongitudeImageOne);
                    int distanceTwo = distance(currentLat, finalLatitudeImageTwo, currentLon, finalLongitudeImageTwo);

                    int timeOne = timeToDestination(distanceOne);
                    int timeTwo = timeToDestination(distanceTwo);

                    txtImageOneDistance.setText(distanceOne + "m" + "\nWalk: " + timeOne + " min");
                    txtImageTwoDistance.setText(distanceTwo + "m" + "\nWalk: " + timeTwo + " min");
                }
            }
        });
    }

    /*Calculating distance from current location to photos location and returning in meters, absolute distance (straight line on map)*/
    public int distance(double lat1, double lat2, double lon1, double lon2) {
        double dlon = Math.toRadians(lon2) - Math.toRadians(lon1);
        double dlat = Math.toRadians(lat2) - Math.toRadians(lat1);
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;
        double distMeters = c * r * 1000;
        int result = (int) distMeters;
        return result;
    }

    /*Calculating distance to photo, based on 1 km = 5 min walk*/
    public int timeToDestination(double distanceMeters) {
        double minutes = distanceMeters / 1000 * 5;
        minutes = Math.ceil(minutes); //Rounding up value
        return (int) minutes;
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

        if (selectedImageTwo == true) { //Fix so you only can select ONE photo at a time
            //Add your data to bundle
            bundleSelectedPhoto.putString("oldPhoto", "dia_303_12172");
            Intent intent = new Intent(this, CameraActivity.class);

            //Add the bundle to the intent
            intent.putExtras(bundleSelectedPhoto);
            startActivity(intent);

        } else if (selectedImageOne == true) {
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
    public void selectImageOne(View view) throws JSONException {
        int colorInt = getResources().getColor(R.color.smapshot_blue);
        ColorStateList csl = ColorStateList.valueOf(colorInt);

        if (!selectedImageOne) {
            if (!selectedImageTwo) {
                imageOnePhoto.setStrokeColor(csl);
                imageOnePhoto.setStrokeWidth(30);
                selectedImageOne = true;
                btnPhoto.setTextColor(Color.parseColor("#444444"));
                btnPhoto.getBackground().setAlpha(255);
                imageOneMarker.showInfoWindow();
            } else {
                imageTwoPhoto.setStrokeColor(csl);
                imageTwoPhoto.setStrokeWidth(0);
                imageOnePhoto.setStrokeColor(csl);
                imageOnePhoto.setStrokeWidth(30);
                selectedImageOne = true;
                imageOneMarker.showInfoWindow();
                selectedImageTwo = false;
            }
        } else {
            imageOnePhoto.setStrokeWidth(0);
            selectedImageOne = false;
            btnPhoto.setTextColor(Color.rgb(204,204,204));
            btnPhoto.getBackground().setAlpha(100);
            imageOneMarker.closeInfoWindow();
        }

        //Fetching json-file from /assets/ folder
        String imageOnePhoto = jsonFinder.JSONFromAsset(this,"test_photo.json");
        try {
            jsonObj = new JSONObject(imageOnePhoto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_test", az);
        bundleSelectedPhoto.putFloat("tilt_test", tilt);
        bundleSelectedPhoto.putFloat("roll_test", roll);

        mapController.animateTo(imageOnePoint);
    }

    //Refactor: merge with method above?
    public void selectImageTwo(View view) throws JSONException {
        int colorInt = getResources().getColor(R.color.smapshot_blue);
        ColorStateList csl = ColorStateList.valueOf(colorInt);

        imageTwoMarker.showInfoWindow();

        if (!selectedImageTwo) {
            if (!selectedImageOne) {
                imageTwoPhoto.setStrokeColor(csl);
                imageTwoPhoto.setStrokeWidth(30);
                selectedImageTwo = true;
                btnPhoto.setTextColor(Color.parseColor("#444444"));
                btnPhoto.getBackground().setAlpha(255);
                imageTwoMarker.showInfoWindow();
            } else {
                imageOnePhoto.setStrokeColor(csl);
                imageOnePhoto.setStrokeWidth(0);
                imageTwoPhoto.setStrokeColor(csl);
                imageTwoPhoto.setStrokeWidth(30);
                selectedImageTwo = true;
                imageTwoMarker.showInfoWindow();
                selectedImageOne = false;
            }
        } else {
            imageTwoPhoto.setStrokeWidth(0);
            selectedImageTwo = false;
            btnPhoto.setTextColor(Color.rgb(204,204,204));
            btnPhoto.getBackground().setAlpha(100);
            imageTwoMarker.closeInfoWindow();
        }

        //Fetching json-file from /assets/ folder
        String imageTwoPhoto = jsonFinder.JSONFromAsset(this,"dia_303_12172.json");
        try {
            jsonObj = new JSONObject(imageTwoPhoto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float az = sbToFloatAng(jsonObj, "azimuth");
        Float tilt = sbToFloatAng(jsonObj, "tilt");
        Float roll = sbToFloatAng(jsonObj, "roll");

        bundleSelectedPhoto.putFloat("azimuth_dia", az);
        bundleSelectedPhoto.putFloat("tilt_dia", tilt);
        bundleSelectedPhoto.putFloat("roll_dia", roll);

        mapController.animateTo(imageTwoPoint);
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