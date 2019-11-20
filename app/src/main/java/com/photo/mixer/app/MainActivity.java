package com.photo.mixer.app;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout blender, imageeditor, rateus, mypictures;
    AdView adView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViews();
        requestForPermissions();
        adview();

//        MobileAds.initialize(this, getResources().getString(R.string.appid));

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstial));
        try {
            if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void adview() {
        adView = findViewById(R.id.main_adViews);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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

    @Override
    public void onClick(View view) {

        if (view == blender) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent intent = new Intent(MainActivity.this, BlenderActivity.class);
                startActivity(intent);
            }
            mInterstitialAd.setAdListener(new AdListener() {
                                              @Override
                                              public void onAdClosed() {
                                                  requestNewInterstitial();
                                                  Intent intent = new Intent(MainActivity.this, BlenderActivity.class);
                                                  startActivity(intent);
                                              }
                                          }
            );
        }
        if (view == imageeditor) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent intent = new Intent(MainActivity.this, PickImageEditor.class);
                startActivity(intent);
            }
            mInterstitialAd.setAdListener(new AdListener() {
                                              @Override
                                              public void onAdClosed() {
                                                  requestNewInterstitial();
                                                  Intent intent = new Intent(MainActivity.this, PickImageEditor.class);
                                                  startActivity(intent);
                                              }
                                          }
            );
        }

        if (view == rateus) {
            rate_us();
        }
        if (view == mypictures) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent intent = new Intent(MainActivity.this, MyPictures.class);
                startActivity(intent);
            }
            mInterstitialAd.setAdListener(new AdListener() {
                                              @Override
                                              public void onAdClosed() {
                                                  requestNewInterstitial();
                                                  Intent intent = new Intent(MainActivity.this, MyPictures.class);
                                                  startActivity(intent);
                                              }
                                          }
            );
        }
    }

    public void rate_us() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
    }


    public void InitViews() {
        blender = findViewById(R.id._blender);
        imageeditor = findViewById(R.id._editor);
        rateus = findViewById(R.id._rateUs);
        mypictures = findViewById(R.id._shareApp);
        blender.setOnClickListener(this);
        imageeditor.setOnClickListener(this);
        rateus.setOnClickListener(this);
        mypictures.setOnClickListener(this);
    }

    private void requestForPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
                            token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}