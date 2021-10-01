package com.example.mycameraapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

public class PostPicActivity extends MainActivity { //AppCompatActivity makes it run smoother, for now MainActivity. Change in future?
    private Button btnGoBack;
    private TextView txtFromMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpic);

        txtFromMainActivity = findViewById(R.id.txt);
        txtFromMainActivity.setTextColor(Color.parseColor("#444444"));
        txtFromMainActivity.setText(sb);

        azimuth = findViewById(R.id.json1);
        azimuth.setTextColor(Color.parseColor("#444444"));
        try {
            azimuth.setText(findValue(jsonObj, "azimuth"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tilt = findViewById(R.id.json2);
        tilt.setTextColor(Color.parseColor("#444444"));
        try {
            tilt.setText(findValue(jsonObj, "tilt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        roll = findViewById(R.id.json3);
        roll.setTextColor(Color.parseColor("#444444"));
        try {
            roll.setText(findValue(jsonObj, "roll"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnGoBack = (Button) findViewById(R.id.button);
        btnGoBack.setBackgroundColor(Color.parseColor("#E2E2E2"));
        btnGoBack.setTextColor(Color.parseColor("#444444"));

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(v);
            }
        });
    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}