package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanks.htextview.evaporate.EvaporateTextView;

public class HowToUseActivity extends AppCompatActivity {

    public EvaporateTextView stepOne, stepTwo, stepThree, stepFour;
    public Button btnGoBack;
    private ImageView contribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        stepOne = findViewById(R.id.stepOne);
        stepTwo = findViewById(R.id.stepTwo);
        stepThree = findViewById(R.id.stepThree);
        stepFour = findViewById(R.id.stepFour);
        btnGoBack = findViewById(R.id.btnGoBack);
        contribute = findViewById(R.id.contribute);

        stepOne.animateText("1. Find a photo you wish to retake");
        stepTwo.animateText("2. Navigate to its location");
        stepThree.animateText("3. Retake the photo");
        stepFour.animateText("4. Compare the past with the present!");

        contribute.setImageResource(R.drawable.contribute);

        stepOne.getBackground().setAlpha(230);
        stepTwo.getBackground().setAlpha(204);
        stepThree.getBackground().setAlpha(179);
        stepFour.getBackground().setAlpha(153);

        btnGoBack.setTypeface(montserrat_medium);
        btnGoBack.setTextColor(Color.parseColor("#444444"));
        btnGoBack.setBackgroundColor(Color.parseColor("#E2E2E2"));

    }

    //Setting text color and font for design purposes
    public void setText(TextView txt, Typeface font) {
        txt.setTypeface(font);
    }

    public void openMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}