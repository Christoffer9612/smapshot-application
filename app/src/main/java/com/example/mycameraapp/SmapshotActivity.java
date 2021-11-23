package com.example.mycameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SmapshotActivity extends AppCompatActivity {

    private ImageView smapshotModel;
    private Button goBack, visitWebsite;
    private Utils utils = new Utils(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smapshot);

        smapshotModel = findViewById(R.id.smapshotModel);
        goBack = findViewById(R.id.btnGoBack);
        visitWebsite = findViewById(R.id.btnVisitWebsite);

        Typeface montserrat_medium = Typeface.createFromAsset(getAssets(), "fonts/montserrat_medium.ttf");

        smapshotModel.setImageResource(R.drawable.smapshot_web);
        utils.setButton(goBack, montserrat_medium);
        utils.setButton(visitWebsite, montserrat_medium);

    }

    //Methods for opening new Activities
    public void openMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void visitWebsite(View view) {
        Uri uri = Uri.parse("https://smapshot.heig-vd.ch/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}