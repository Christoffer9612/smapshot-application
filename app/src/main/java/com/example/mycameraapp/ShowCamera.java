package com.example.mycameraapp;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceHolder holder;
    private int position = 0;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    /*Displays camera in realtime*/
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Camera.Parameters params = camera.getParameters();

        //Aspect ratio of camera (4:3 is set)
        List<Camera.Size> ratioSizes = params.getSupportedPreviewSizes();
        for (Camera.Size size : ratioSizes) {
            int width = size.width;
            int height = size.height;
            double aspectRatio = (double) width / (double) height;
            Log.d("ASPECT RATIO", "height: " + size.width + ", width: " + size.height + ", aspect ratio = " + aspectRatio);
            position++;
            if (aspectRatio == 1.3333333333333333) {
                params.setPreviewSize(width, height); //Setting preview camera to 4:3 ratio
                Log.d("RATIO SET TO", "height: " + width + ", width: " + height);
                break;
            }
        }

        //Picture sizes (resolution)
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size photoSize = null;

        for (Camera.Size size : sizes) {
            Log.d("PIC", "Available resolution: "+size.width+" "+size.height);
        }

        photoSize  = sizes.get(0); //Storing biggest resolution size values (width & height)
        Log.d("PICTURE RES", " height: " + photoSize.width + ", width: " + photoSize.height);

        //Change orientation of camera
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }
        //Setting photo size to biggest available
        params.setPictureSize(photoSize.width, photoSize.height);

        //Autofocus for camera when photographing
        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        camera.setParameters(params);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }
}

