package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.imageview.ShapeableImageView;

public class SelectPhotoActivity extends AppCompatActivity {
    private ShapeableImageView selTestPhoto, selDia303;
    private Button btnPhoto, btnBack;
    private boolean selectedTest, selectedDia;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        //Create the bundle
        bundle = new Bundle();

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        //Finding id:s from activity_select_photo.xml file
        selTestPhoto = findViewById(R.id.selTestPhoto);
        selDia303 = findViewById(R.id.selDia303);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnBack = findViewById(R.id.btnBack);

        //Setting stuff
        selTestPhoto.setImageResource(R.drawable.st_roch_test); // Might not need?
        selDia303.setImageResource(R.drawable.dia_303_12172); // Might not need since we set photos in .xml file instead!

        btnPhoto.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnPhoto.setTextColor(Color.parseColor("#444444"));
        btnPhoto.setTypeface(montserrat_medium);

        btnBack.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnBack.setTextColor(Color.parseColor("#444444"));
        btnBack.setTypeface(montserrat_medium);

    }

    public void openCustomCam(View view) {

        if (selectedDia == true) { //Fix so you only can select ONE photo at a time
            //Add your data to bundle
            bundle.putString("oldPhoto", "dia_303_12172");
        } else if (selectedTest == true) {
            //Add your data to bundle
            bundle.putString("oldPhoto", "st_roch_test");
        }

        Intent intent = new Intent(this, CameraActivity.class);

        //Add the bundle to the intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Refactor: merge with method below
    public void selectedTest(View view) {

        if (!selectedTest) {
            int colorInt = getResources().getColor(R.color.smapshot_blue);
            ColorStateList csl = ColorStateList.valueOf(colorInt);

            selTestPhoto.setStrokeColor(csl);
            selTestPhoto.setStrokeWidth(25);

            selectedTest = true;
        } else {
            selTestPhoto.setStrokeWidth(0);
            selectedTest = false;
        }
    }

    //Refactor: merge with method above
    public void selectedDia(View view) {

        if (!selectedDia) {
            int colorInt = getResources().getColor(R.color.smapshot_blue);
            ColorStateList csl = ColorStateList.valueOf(colorInt);

            selDia303.setStrokeColor(csl);
            selDia303.setStrokeWidth(25);

            selectedDia = true;
        } else {
            selDia303.setStrokeWidth(0);
            selectedDia = false;
        }
    }

}