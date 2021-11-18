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

}
