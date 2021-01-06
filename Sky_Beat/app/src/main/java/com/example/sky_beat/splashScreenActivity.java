package com.example.sky_beat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sky_beat.Activity.MainActivity;

public class splashScreenActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        linearLayout = findViewById(R.id.Splash_Activity);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        // Transparent actionbar
        getWindow().setFlags(

                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

        );
        getSupportActionBar().hide();

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(splashScreenActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}