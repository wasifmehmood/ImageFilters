package com.photo.mixer.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.photo.mixer.app.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class SinglePicture extends AppCompatActivity {

    public static String mpath;
    ImageView mimageView;
    AdView adView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_picture);
//        MobileAds.initialize(this,getResources().getString(R.string.appid));
//        mtoolbar = findViewById(R.id.toolbar);

        add();
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mtoolbar.setTitleTextColor(Color.WHITE);
        // id2 = getIntent().getStringExtra("idkey") ;//get id
        //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
        File imgFile = new File(mpath);
        String filename = imgFile.getName();
        if (imgFile.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(mpath);
            mimageView.setImageBitmap(bmp);
//            toolbar.setTitle(filename);

//            setSupportActionBar(toolbar);
        }
    }

    public void add() {
        adView = findViewById(R.id.adViews);
        AdRequest adrequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adrequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView.setVisibility(View.GONE);
            }
        });
    }
}