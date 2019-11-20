package com.photo.mixer.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PickImageEditor extends AppCompatActivity implements View.OnClickListener {

    static Uri selectedimagesUri;
    LinearLayout SelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image_editor);
        InitViews();
    }


    public void InitViews() {
        SelectImage = findViewById(R.id.gallerybtn);
        SelectImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == SelectImage) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(PickImageEditor.this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedimagesUri = result.getUri();
                Intent intent = new Intent(PickImageEditor.this, PhotoEditor.class);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}