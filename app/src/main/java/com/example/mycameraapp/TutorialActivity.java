package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialActivity extends AppCompatActivity {

    public static TextView stepOne, stepTwo, stepThree, stepFour;
    public Button btnGoBack;
    private ImageView contribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(),"fonts/montserrat_medium.ttf");

        stepOne = findViewById(R.id.stepOne);
        stepTwo = findViewById(R.id.stepTwo);
        stepThree = findViewById(R.id.stepThree);
        stepFour = findViewById(R.id.stepFour);
        btnGoBack = findViewById(R.id.btnGoBack);
        contribute = findViewById(R.id.contribute);

        setText(stepOne, montserrat_medium);
        setText(stepTwo, montserrat_medium);
        setText(stepThree, montserrat_medium);
        setText(stepFour, montserrat_medium);
        contribute.setImageResource(R.drawable.contribute);

        stepOne.setGravity(Gravity.CENTER);
        stepTwo.setGravity(Gravity.CENTER);
        stepThree.setGravity(Gravity.CENTER);
        stepFour.setGravity(Gravity.CENTER);

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