package com.photo.mixer.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class mySplashScreen extends AppCompatActivity {

    Handler mhandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysplash_screen);

        mhandlers = new Handler();
        mhandlers.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mySplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}