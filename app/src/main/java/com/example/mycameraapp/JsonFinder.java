package com.example.mycameraapp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonFinder {
    private Context context;

    /*A class that returns values from json-files under /assets/ folder.*/
    public JsonFinder(Context context){
        this.context = context;
    }

    /*Returns JSON file on String format.*/
    public String JSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
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

    /*Method for returning double value from String key.*/
    public Double getValue(JSONObject jsonObj, String key) throws JSONException {
        Double val = jsonObj.getDouble(key);
        return val;
    }

    /*Method for returning double value from String key.*/
    public String getStringValue(JSONObject jsonObj, String key) throws JSONException {
        String val = jsonObj.getString(key);
        return val;
    }

}
