package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class FindActivity extends AppCompatActivity {

    private Button btnMain, btnFind;
    private CheckBox checkboxCities, checkboxLandscapes, checkboxMonuments, checkboxRivers, checkboxViewpoints;
    private Utils utils = new Utils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        btnMain = findViewById(R.id.btnMain);
        btnFind = findViewById(R.id.btnFind);
        checkboxCities = findViewById(R.id.checkboxCities);
        checkboxLandscapes = findViewById(R.id.checkboxLandscapes);
        checkboxMonuments = findViewById(R.id.checkboxMonuments);
        checkboxRivers = findViewById(R.id.checkboxRivers);
        checkboxViewpoints = findViewById(R.id.checkboxViewpoints);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(), "fonts/montserrat_medium.ttf");

        utils.setButton(btnMain, montserrat_medium);
        utils.setButton(btnFind, montserrat_medium);
        utils.setText(checkboxCities, montserrat_medium);
        utils.setText(checkboxLandscapes, montserrat_medium);
        utils.setText(checkboxMonuments, montserrat_medium);
        utils.setText(checkboxRivers, montserrat_medium);
        utils.setText(checkboxViewpoints, montserrat_medium);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain(v);
            }
        });
    }

    public void openSelectPhoto(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void openMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



}