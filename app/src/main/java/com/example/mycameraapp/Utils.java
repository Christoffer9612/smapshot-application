package com.example.mycameraapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

public class Utils {
    public Context context;

    public Utils(Context context) {
        this.context = context;
    }

    //Setting button color and font for design purposes
    public void setButton(Button button, Typeface font) {
        button.setTypeface(font);
        button.setTextColor(Color.parseColor("#444444"));
        button.setBackgroundColor(Color.parseColor("#E2E2E2"));
    }

    //Setting text color and font for design purposes
    public void setText(TextView txt, Typeface font) {
        txt.setTypeface(font);
        txt.setTextColor(Color.parseColor("#444444"));
    }

    //normalise angles 0 to 360 degrees
    public float normaliseAngles (float angle) {
        angle = angle % 360;
        angle = (angle + 360) % 360;

        return angle;
    }
    //normalise angles -180 to 180 degrees
    public float normaliseAngles180 (float angle) {
        angle = angle % 360;
        angle = (angle + 360) % 360;

        if (angle > 180)
            angle -= 360;

        return angle;
    }

    //calculate uncertainty for azimuth 0 to 360 degrees
    public float diff360(float angle_old, float angle_new) {
        //compute three accuracies, going difference, clockwise and counterclockwise
        //return lowest uncertainty


        float diff;
        float diffClockwise;
        float diffCounterclockwise;

        //Straight difference Diff
        diff = Math.abs(angle_old - angle_new);


        //clockwise up to 360 + old_angle
        float x = 360 - angle_new;
        diffClockwise = x + angle_old;



        //counterclockwise down to 0 degrees + (360-old_angle)
        float y = 360 - angle_old;
        diffCounterclockwise = angle_new + y;



        //return the highest accuracy
        if(diff < diffClockwise && diff < diffCounterclockwise) {
            return diff;
        } else if (diffClockwise < diff && diffClockwise < diffCounterclockwise) {
            return diffClockwise;
        } else {
            return diffCounterclockwise;

        }

    }

    //used for calculating uncertainty in tilt and roll -180 to 180 degrees
    public float diff180(float angle_old, float angle_new) {
        //compute two uncertainties, going difference, and for angles when one is positive, and the other is negative
        //return lowest uncertainty
        //corner cases for angles close to 180 or -180 are not covered, but those angles are very unlikely to encounter

        float diff;
        float diffAroundZero;

        //Straight difference Diff, when both angles are negative and when both angles are positive
        diff = Math.abs(angle_old - angle_new);


        //up or down to 0 + old_angle, when one angle is negative, and the other is positive
        diffAroundZero = Math.abs(angle_new) + Math.abs(angle_old);



        //when one angle is positive, and the other is negative
        if(angle_old < 0 && angle_new > 0 || angle_old > 0 && angle_new < 0) {
            return diffAroundZero;
        }

        if(diff < diffAroundZero) {
            return diff;
        } else {
            return diffAroundZero;
        }

    }

}
