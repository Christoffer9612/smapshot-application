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


        // Used for aspect ratio in camera
        List<Camera.Size> sizes1 = params.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes1, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        params.setPreviewSize(optimalSize.width, optimalSize.height);



        List<Camera.Size> sizes = params.getSupportedPictureSizes(); //List with different resolution sizes available on Android phone
        Camera.Size photoSize = null;

        for (Camera.Size size : sizes) {
            Log.i("RES", "Available resolution: "+size.width+" "+size.height);
            //photoSize = size;
        }

        photoSize  = sizes.get(0); //Storing biggest resolution size values (width & height)
        Log.d("RESOLUTION SET TO", " h: " + photoSize.width + ", w: " + photoSize.height);

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

    // Used for aspect ratio in camera
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;

        if (sizes==null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        Log.d("OPTIMAL CAM SIZE: ", "height: " + optimalSize.width + ", width: " + optimalSize.height);
        return optimalSize;
    }
}

