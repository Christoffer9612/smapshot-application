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
    public float uncertaintyCalc(float angle_old, float angle_new) {
        //compute three accuracies, going difference, clockwise and counterclockwise
        //return lowest uncertainty


        float uncertainty_diff;
        float uncertainty_clockwise;
        float uncertainty_counterclockwise;

        //Straight difference Diff
        uncertainty_diff = Math.abs(angle_old - angle_new);
        uncertainty_diff = (float) (uncertainty_diff /360.0);

        //clockwise up to 360 + old_angle
        float x = 360 - angle_new;
        uncertainty_clockwise = x + angle_old;
        uncertainty_clockwise = (float) (uncertainty_clockwise/360.0);


        //counterclockwise down to 0 degrees + (360-old_angle)
        float y = 360 - angle_old;
        uncertainty_counterclockwise = angle_new + y;
        uncertainty_counterclockwise = (float) (uncertainty_counterclockwise/360.0);


        //return the highest accuracy
        if(uncertainty_diff < uncertainty_clockwise && uncertainty_diff < uncertainty_counterclockwise) {
            return uncertainty_diff;
        } else if (uncertainty_clockwise < uncertainty_diff && uncertainty_clockwise < uncertainty_counterclockwise) {
            return uncertainty_clockwise;
        } else {
            return uncertainty_counterclockwise;

        }

    }

    //used for calculating uncertainty in tilt and roll -180 to 180 degrees
    public float uncertaintyCalc180(float angle_old, float angle_new) {
        //compute two uncertainties, going difference, and for angles when one is positive, and the other is negative
        //return lowest uncertainty
        //corner cases for angles close to 180 or -180 are not covered, but those angles are very unlikely to encounter

        float uncertaintyDiff;
        float uncertaintyAroundZero;

        //Straight difference Diff, when both angles are negative and when both angles are positive
        uncertaintyDiff = Math.abs(angle_old - angle_new);
        uncertaintyDiff = (float) (uncertaintyDiff /180.0);

        //up or down to 0 + old_angle, when one angle is negative, and the other is positive
        uncertaintyAroundZero = Math.abs(angle_new) + Math.abs(angle_old);
        uncertaintyAroundZero = (float) (uncertaintyAroundZero/180.0);


        //when one angle is positive, and the other is negative
        if(angle_old < 0 && angle_new > 0 || angle_old > 0 && angle_new < 0) {
            return uncertaintyAroundZero;
        }

        if(uncertaintyDiff < uncertaintyAroundZero) {
            return uncertaintyDiff;
        } else {
            return uncertaintyAroundZero;
        }

    }

}
