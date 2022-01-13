package com.example.mycameraapp;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAPI {
    private Context context;
    private String urlPhoto;

    private String urlJson;

    /*Description*/
    public RequestAPI(Context context){
        this.context = context;

        /* Change this String to whatever Smapshot photo you want to display in the app, e.g: https://smapshot.heig-vd.ch/api/v1/data/collections/31/images/500/185746.jpg */
        this.urlPhoto = "https://smapshot.heig-vd.ch/api/v1/data/collections/31/images/500/185747.jpg"; // URL, API endpoint pointing towards a Smapshot photo

        /* WiP */
        this.urlJson = "https://smapshot.heig-vd.ch/api/v1/images/185747/attributes/";
    }

    /* Used in SelectActivity, CameraActivity and ResultActivity. Displays the photo */
    public StringRequest requestPhoto(ImageView imageView) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPhoto, //Dia photo
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Picasso.get().load(urlPhoto).into(imageView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("REQ_ERROR", "URL link is broken or you don't have internet connection...");
            }
        });
        return stringRequest;
    }


    /* Work in progress */
    public JsonObjectRequest requestJsonFile() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlJson, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response = new JSONObject(response.toString());
                            JSONObject pose = new JSONObject(response.getString("pose")); // JSONObject within JSONObject...
                            Double value = pose.getDouble("latitude");
                            Log.d("JSON_FILE","" + value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JSON_FILE", "error");
                    }
                });
        return jsonObjectRequest;
    }

}
