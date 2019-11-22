package com.photo.mixer.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.MobileAds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.os.Build.VERSION_CODES.M;

public class BlenderActivity extends AppCompatActivity implements View.OnClickListener {

    public static String imageFilePathCameras;
    LinearLayout camera, gallery;
    int REQUEST_CAPTURE_IMAGE = 1001;
    public static int flags = 0;
    ArrayList<Image> imagesUri;
    String results;
    static Uri uriPickers;
    static ArrayList userSelectedImageUriList;
    static File paths;
    static int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blender);
        InitViews();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BlenderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (view == gallery) {
            PickPictures();
        }

        if (view == camera) {
            openCameraIntent();
        }
    }

    public void InitViews() {
        camera = findViewById(R.id._camera);
        gallery = findViewById(R.id._gallery);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }

    public void PickPictures() {
        Intent intent = new Intent(BlenderActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 4); // set limit for image selection
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    public void openCameraIntent() {
        Intent pictureIntents = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        if (pictureIntents.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFiles = null;
            try {
                photoFiles = createsImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFiles != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getPackageName(), photoFiles);
                pictureIntents.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntents, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createsImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDirs =
                null;
        if (Build.VERSION.SDK_INT >= M) {
            storageDirs = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDirs      /* directory */
        );

        imageFilePathCameras = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            flags = 1;
            Intent intent = new Intent(getApplicationContext(), BlendPictures.class);
            startActivity(intent);
        }

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            flags = 2;

            imagesUri = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < imagesUri.size(); i++) {

                uriPickers = Uri.fromFile(new File(imagesUri.get(i).path));
                results = getURIPaths(uriPickers);

                if (userSelectedImageUriList == null) {
                    userSelectedImageUriList = new ArrayList<>();
                    userSelectedImageUriList.clear();
                    userSelectedImageUriList.add(results);
                } else {
                    userSelectedImageUriList.add(results);
                }
                super.onActivityResult(requestCode, resultCode, data);
            }
            total = userSelectedImageUriList.size();
            File path = new File(String.valueOf((userSelectedImageUriList)));
            paths = path;
            userSelectedImageUriList.clear();

            try {
                if (paths != null) {
                    Intent intent = new Intent(BlenderActivity.this, BlendPictures.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public String getURIPaths(Uri uri) {
        String realPaths = "";
// SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            String[] proj = {MediaStore.Images.Media.DATA};
            @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int column_index = 0;
            String result = "";
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                realPaths = cursor.getString(column_index);
            }
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                realPaths = cursor.getString(column_index);
            }
        } else if (Build.VERSION.SDK_INT == M) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                realPaths = uri.getPath();
            } else {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                cursor.moveToFirst();
                realPaths = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        // SDK > 19 (Android 4.4)
        else {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                realPaths = uri.getPath();
            } else {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                cursor.moveToFirst();
                realPaths = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return realPaths;
    }
}