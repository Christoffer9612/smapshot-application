package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    public static int index = 0;
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build() );

    }

    public void CameraButton(View view) {

        index++;
        String file = directory + index + ".jpg";
        File newFile = new File(file);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri outputFileUri = Uri.fromFile(newFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivity(cameraIntent);


    }

}