package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostPicture extends MainActivity { //AppCompatActivity makes it run smoother, for now MainActivity. Change in future?
    private Button button;
    private TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_picture);

        txt2 = findViewById(R.id.txt);
        txt2.setText(sb);

        json1 = findViewById(R.id.json1);
        json1.setText(findValue(jsonObj, "azimuth"));
        json2 = findViewById(R.id.json2);
        json2.setText(findValue(jsonObj, "tilt"));
        json3 = findViewById(R.id.json3);
        json3.setText(findValue(jsonObj, "roll"));

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
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