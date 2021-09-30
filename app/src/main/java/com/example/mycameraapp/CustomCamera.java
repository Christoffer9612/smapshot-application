package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomCamera extends MainActivity {
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    private Button btnGoBack;
    private TextView realTimeParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);

        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        realTimeParams = (TextView) findViewById(R.id.realTimeParams);


        //open the camera

        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}