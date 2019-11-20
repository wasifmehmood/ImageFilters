package com.photo.mixer.app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.photo.mixer.app.BlenderActivity.flags;
import static com.photo.mixer.app.BlenderActivity.paths;
import static com.photo.mixer.app.BlenderActivity.total;

public class BlendPictures extends AppCompatActivity {

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    List<String> mimagesEncodedList;
    int i;
    FrameLayout layout;
    ImageView m_imgView;
    ImageView[] mimageViewsArray = new ImageView[4];
    StickerImgView iv_sticker;
    StickerImgView stickerImgV;
    StickerImgView[] stickerImgViews = new StickerImgView[total];
    ArrayList<StickerImgView> stickerImgViewArrayList = new ArrayList<>();
    int mlastTouch = -1;
    float[] mlastEvent = null;
    float d = 0f;
    float newRot = 0f;
    int count = 0;
    float oldDist = 1f;
    int imageCounter = 3;
    SeekBar mbarOpacity;
    Bitmap mBitmap;
    FrameLayout v;
    int n;
    String imageShare;
    ImageView saveImages;
    String muriString;
    String str;
    String tag = "";
    String[] parts;
    ArrayList<String> arrayListofStr;
    Bitmap moriginalImage;
    boolean setAlpha = false;
    boolean setAlphaImgV = false;
    int totalImg;
    int imgCounter = 0;
    StickerView view;
    StickerView views;
    StickerImgView stickerImgView;
    int setAlphCheck = 0;
    boolean checkgalleryImage = false;
    boolean checkCameraImage = false;
    View.OnTouchListener mTouchListener;


    SeekBar.OnSeekBarChangeListener mbarOpacityOnSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
//                    int mbarOpacity = progress * 0x01000000;
                    final int alpha = mbarOpacity.getProgress();
//                    textopacitysetting.setText(String.valueOf(alpha));

//                    for (counter = 0; counter < setAlphCheck; counter++) {

//                    if (setAlphCheck < totalImg) {

////                        setAlphCheck++;
////                    }

//                    setAlpha = true;

                    switch (mlastTouch) {
                        case 1:
                            iv_sticker.setimageAplha(alpha);
                            setAlpha = true;
                            break;

                        case 2:
                            stickerImgV.setimageAplha(alpha);
                            setAlphaImgV = true;
                            break;

//                        case 3:
//                            iv_sticker.setimageAplha(alpha);
//                            break;
//                        case 3:
//                            stickerImgViews[1].setimageAplha(alpha);
//                            break;
//                        case 4:
//                            stickerImgViews[2].setimageAplha(alpha);
//                            break;
//                        case 5:
//                            stickerImgViews[3].setimageAplha(alpha);
//                            break;
                        default:
                            if (stickerImgView != null)
                                stickerImgView.setimageAplha(alpha);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };
    private Button btn;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float xCoOrdinate, yCoOrdinate;
    private int REQUEST_GALLERY_IMAGE = 1001;
    private int counter = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blend_pictures);
        SetBitmap();
        stickerImgV = new StickerImgView(this);

        if (BlenderActivity.flags == 1) {
            String camshotUri = BlenderActivity.imageFilePathCameras;
            File file = new File(camshotUri);
            Uri urisCamshot = Uri.fromFile(file);

            try {
                moriginalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), urisCamshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();

            layout.addView(iv_sticker);
//            Toast.makeText(this, ""+iv_sticker.getImageBitmap(), Toast.LENGTH_SHORT).show();
            iv_sticker.setImageBitmap(moriginalImage);
//            Toast.makeText(this, ""+iv_sticker.getImageBitmap(), Toast.LENGTH_SHORT).show();
            checkCameraImage = true;
            iv_sticker.setTag("camera_image");

//            iv_sticker.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if (count == 0) {
//                        count = 1;
//                        stickerImgV.setControlsVisibility(false);                   /* Uncheck ittttttt*/
//                    } else {
//                        count = 0;
//                        stickerImgV.setControlsVisibility(true);
//                    }
//
//                    viewTransformation(view, motionEvent);
//                    if (mlastTouch != 2)
//                        mlastTouch = 3;
//                    else
//                        mlastTouch = -1;
//                    return true;
//                }
//
////            iv_sticker.setTag("hello");
////            Glide.with(this).load(BlenderActivity.imageFilePathCameras).into(m_imgView);
//            });

        }
        if (BlenderActivity.flags == 2) {
            Toast.makeText(this, "0000", Toast.LENGTH_SHORT).show();
            SplitImages();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        saveImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraImage) {
                    iv_sticker.setControlsVisibility(false);
                } else {
                    if (count == 0) {
                        count = 1;
                        stickerImgV.setControlsVisibility(false);
                        /* Set Visibility*/
                    } else {
                        count = 0;
                        stickerImgV.setControlsVisibility(true);
                    }
                }
                SaveTheImage();
            }
        });
        setTouchListeners();
    }

    public void setTouchListeners() {
        mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                String tag = (String) iv_sticker.getTag();
                String tags = (String) stickerImgV.getTag();
                Log.e("touch", "tag : " + tag);
                if (tag.equals("camera_image")) {
                    if (checkCameraImage) {
                        iv_sticker.setControlsVisibility(false);
                    }
                    viewTransformation(view, event);
                    views = (StickerView) view;
                    views.setControlsVisibility(true);
                } else if (tags.equals("gallery_image")) {
                    if (checkgalleryImage) {
                        stickerImgV.setControlsVisibility(false);
                    }
                    viewTransformation(view, event);
                    views = (StickerView) view;
                    views.setControlsVisibility(true);
                }
//                     your stuff
                return true;
            }
        };
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("image", -1);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    private void viewTransformation(View view, MotionEvent event) {
        view.bringToFront();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();
                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                mlastEvent = null;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                mlastEvent = new float[4];
                mlastEvent[0] = event.getX(0);
                mlastEvent[1] = event.getX(1);
                mlastEvent[2] = event.getY(0);
                mlastEvent[3] = event.getY(1);
                d = rotation(event);
                break;

            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }

            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                mlastEvent = null;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                mlastEvent = null;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (mlastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
        //        str = str.replaceAll("[\\[\\]]", "");
    }

    int j = -1;

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
//            Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                mimagesEncodedList = new ArrayList<String>();

                stickerImgV = new StickerImgView(this);

                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    RelativeLayout relativeLayout = new RelativeLayout(this);
//                    Toast.makeText(this, ""+j, Toast.LENGTH_SHORT).show();
                    layout.addView(stickerImgV);
                    stickerImgV.setImageBitmap(bitmap);

                    /**
                     * test
                     */

//                    for (i = totalImg; i < totalImg+1; i++) {
//
////                Toast.makeText(BlendPictures.this, "" + totalImg, Toast.LENGTH_SHORT).show();
//
//                        stickerImgViews[totalImg] = new StickerImgView(this);
//
//                        stickerImgViews[totalImg].isFocusable();
//                        stickerImgViews[totalImg].setTag("TouchListeners" + totalImg);
//                        stickerImgViews[totalImg].setLayoutParams(new android.view.ViewGroup.LayoutParams(800, 800));
//                        stickerImgViews[totalImg].setMaxHeight(400);
//                        stickerImgViews[totalImg].setMaxWidth(400);
//                        stickerImgViews[totalImg].setImageBitmap(bitmap);
//                        layout.addView(stickerImgViews[totalImg]);
//
//                        Toast.makeText(this, "count: " + stickerImgViews.length, Toast.LENGTH_SHORT).show();
//
////                        stickerImgViewArrayList.add(new StickerImgView(this));
////                        stickerImgViewArrayList.get(totalImg)
//
////            if (i >= total) {
////                stickerImgViews[i].setVisibility(View.INVISIBLE);
////            }
//
//                        final int j = i;
//
//                        stickerImgViews[totalImg].setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
////                ImageView view = (ImageView) v;
////                view.bringToFront();
//
//                                removeAllIconsOnImage();
//
//                                Toast.makeText(BlendPictures.this, "" + v.isFocused(), Toast.LENGTH_SHORT).show();
//                                setAlphCheck = totalImg;
//
////                    stickerImgViews[i].setControlsVisibility(true);
//
//                                viewTransformation(v, event);
//                                view = (StickerView) v;
//                                view.setControlsVisibility(true);
//                                tag = (String) v.getTag();
//
//                                stickerImgView = (StickerImgView) findViewById(R.id.blender_layout).findViewWithTag(tag);
//                                Log.e("count", "" + totalImg);
////                        Toast.makeText(BlendPictures.this, "count: "+i, Toast.LENGTH_SHORT).show();
//
//                                if (count == 0) {
//                                    count = 1;
////                            if (totalImg == 1) {
//                                    stickerImgViews[totalImg].setControlsVisibility(false);
////                            }
//                                } else {
//                                    count = 0;
//                                    stickerImgViews[totalImg].setControlsVisibility(true);
//                                }
//                                mlastTouch = 2;
//                                return true;
//                            }
//                        });
//                    }
//
//
//
//                    /**
//                     * test
//                     */
//                totalImg++;

//                    Toast.makeText(this, "IN"+layout.getChildCount(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "out", Toast.LENGTH_SHORT).show();

//                    for (int i=0;i< layout.getChildCount();i++) {
//                        StickerView stickerView = (StickerView) layout.getChildAt(i);
//                    }

                    ///Gallery Image Hello

//                    stickerImgV.setOnTouchListener(mTouchListener);

                    stickerImgV.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            Toast.makeText(BlendPictures.this, "stickerImgV", Toast.LENGTH_SHORT).show();
                            if (count == 0) {
                                count = 1;
                                stickerImgV.setControlsVisibility(false);
                                /* Set Visibility*/
                            } else {
                                count = 0;
                                stickerImgV.setControlsVisibility(true);
                            }

                            viewTransformation(view, motionEvent);
                            if (mlastTouch != 2)
                                mlastTouch = 2;
                            else
                                mlastTouch = -1;
                            return true;
                        }
                    });

//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                    mArrayUri.add(mImageUri);
//
//                    try {
//                        Bitmap fgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);

//                        switch (imageCounter) {
//                            case 0:
//                                addImage(imageCounter, fgBitmap);
//                                imageCounter--;
//                                break;
//                            case 1:
//                                addImage(imageCounter, fgBitmap);
//                                imageCounter--;
//                                break;
//                            case 2:
//                                addImage(imageCounter, fgBitmap);
//                                imageCounter--;
//                                break;
//                            case 3:
//                                addImage(imageCounter, fgBitmap);
//                                imageCounter--;
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    public void addImage(int i, Bitmap fgBitmap) {
//        mimageViewsArray[i].setImageBitmap(fgBitmap);
        stickerImgViews[i].setImageBitmap(fgBitmap);
        // Adds the view to the layout
    }

    public void SplitImages() {
        String stringuri = paths.toString();
        str = stringuri.replaceAll("[\\[\\]]", "");
        arrayListofStr = new ArrayList<>();
        arrayListofStr.add(str);
        parts = str.split(",");

        for (String p : parts) {
            File file = new File(p);
            Uri urisss = Uri.fromFile(file);
            muriString = String.valueOf(urisss);

            if (muriString.contains("/%20")) {
                muriString = ("file://" + file);
            }

            try {
                Bitmap fgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(muriString));
//                for (int images = totalImg - 1; images >= 0; images--) {
                for (int images = 0; images < totalImg; images++) {
                    if (imgCounter == images) {
                        addImage(images, fgBitmap);
                        imgCounter++;
                        break;
                    }
                }

//                switch (imageCounter) {
//                    case 0:
////                        int totaleach;
//                        addImage(imageCounter, fgBitmap);
//                        imageCounter--;
//                        break;
//
//                    case 1:
//                        addImage(imageCounter, fgBitmap);
//                        imageCounter--;
//                        break;
//
//                    case 2:
//                        addImage(imageCounter, fgBitmap);
//                        imageCounter--;
//                        break;
//
//                    case 3:
//                        addImage(imageCounter, fgBitmap);
//                        imageCounter--;
//                        break;
//                }

            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
//            muriString=null;
//            stringuri=null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void InitViews() {

        v = findViewById(R.id.FrameBlender);
        btn = findViewById(R.id.ChooseMultipleImg);
        mbarOpacity = findViewById(R.id.opacity);
        layout = findViewById(R.id.FrameBlender);
        saveImages = findViewById(R.id.saveImages);
        iv_sticker = new StickerImgView(this);

//        layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                ImageView view = (ImageView) v;
//                FrameLayout view = (FrameLayout) v;
//                view.bringToFront();
//                viewTransformation(view, event);
//                mlastTouch = 2;
//                return true;
//            }
//        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void SetBitmap() {
        InitViews();
        final int alpha = mbarOpacity.getProgress();

        if (setAlpha == true) {
            iv_sticker.setAlpha(alpha);
        }

        if (setAlphaImgV == true) {
            stickerImgV.setAlpha(alpha);
        }

        mbarOpacity.setOnSeekBarChangeListener(mbarOpacityOnSeekBarChangeListener);

//        for (i = 0; i < 4; i++) {

        totalImg = total;
        if (flags == 2) {
            for (i = 0; i < totalImg; i++) {

//                Toast.makeText(BlendPictures.this, "" + totalImg, Toast.LENGTH_SHORT).show();

                stickerImgViews[i] = new StickerImgView(this);
                stickerImgViews[i].isFocusable();
                stickerImgViews[i].setTag("TouchListeners" + i);
                stickerImgViews[i].setLayoutParams(new android.view.ViewGroup.LayoutParams(800, 800));
                stickerImgViews[i].setMaxHeight(400);
                stickerImgViews[i].setMaxWidth(400);
                layout.addView(stickerImgViews[i]);

                Toast.makeText(this, "count: " + stickerImgViews.length, Toast.LENGTH_SHORT).show();

//            if (i >= total) {
//                stickerImgViews[i].setVisibility(View.INVISIBLE);
//            }

                final int j = i;

                stickerImgViews[j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
//                ImageView view = (ImageView) v;
//                view.bringToFront();

                        removeAllIconsOnImage();

                        Toast.makeText(BlendPictures.this, "" + v.isFocused(), Toast.LENGTH_SHORT).show();
                        setAlphCheck = j;

//                    stickerImgViews[i].setControlsVisibility(true);

                        viewTransformation(v, event);
                        view = (StickerView) v;
                        view.setControlsVisibility(true);
                        tag = (String) v.getTag();

                        stickerImgView = (StickerImgView) findViewById(R.id.blender_layout).findViewWithTag(tag);
                        Log.e("count", "" + j);
//                        Toast.makeText(BlendPictures.this, "count: "+i, Toast.LENGTH_SHORT).show();

                        if (count == 0) {
                            count = 1;
//                            if (totalImg == 1) {
                            stickerImgViews[j].setControlsVisibility(false);
//                            }
                        } else {
                            count = 0;
                            stickerImgViews[j].setControlsVisibility(true);
                        }
                        mlastTouch = 2;
                        return true;
                    }
                });
            }
        }

//            stickerImgViews[i].setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
////                ImageView view = (ImageView) v;
////                view.bringToFront();
//                    viewTransformation(v, event);
//                    if (count == 0) {
//                        count = 1;
//                        stickerImgViews[1].setControlsVisibility(false);

//                    } else {
//                        count = 0;
//                        stickerImgViews[1].setControlsVisibility(true);
//                    }
//                    mlastTouch = 3;
//                    return true;
//                }
//            });

//            stickerImgViews[2].setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
////                ImageView view = (ImageView) v;
////                view.bringToFront();
//                    viewTransformation(v, event);
//                    if (count == 0) {
//                        count = 1;
//                        stickerImgViews[2].setControlsVisibility(false);
//
//                    } else {
//                        count = 0;
//                        stickerImgViews[2].setControlsVisibility(true);
//                    }
//                    mlastTouch = 4;
//                    return true;
//                }
//            });
//            stickerImgViews[3].setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
////                ImageView view = (ImageView) v;
////                view.bringToFront();
//                    viewTransformation(v, event);
//                    if (count == 0) {
//                        count = 1;
//                        stickerImgViews[3].setControlsVisibility(false);
//                    } else {
//                        count = 0;
//                        stickerImgViews[3].setControlsVisibility(true);
//                    }
//                    mlastTouch = 5;
//                    return true;
//                }
//            });


//        iv_sticker.setOnTouchListener(mTouchListener);

        //////////////////////Hello

        iv_sticker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                iv_sticker.setControlsVisibility();
                Toast.makeText(BlendPictures.this, "touch", Toast.LENGTH_SHORT).show();
                if (count == 0) {
                    count = 1;
                    iv_sticker.setControlsVisibility(false);
                } else {
                    count = 0;
                    iv_sticker.setControlsVisibility(true);
                }
                viewTransformation(view, motionEvent);
//                if (mlastTouch != 2)
//                    mlastTouch = 3;
//                else
//                    mlastTouch = -1;

                if (mlastTouch != 1)
                    mlastTouch = 1;
                else
                    mlastTouch = -1;

                return true;
            }
        });


//        m_imgView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ImageView view = (ImageView) v;
//                view.bringToFront();
//                viewTransformation(view, event);
//                mlastTouch = 1;
//                return true;
//            }
//        });

    }

    private void removeAllIconsOnImage() {
        for (int j = 0; j < stickerImgViews.length; j++) {
            stickerImgViews[j].setControlsVisibility(false);
        }
        if (stickerImgV != null) {
            stickerImgV.setControlsVisibility(false);
        }
    }


//    public View.OnTouchListener mTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//
//            if (view.getTag().equals("TouchListeners")) {
//                viewTransformation(view, event);
//                if (count == 0) {
//                    count = 1;
//                    stickerImgViews[i - 1].setControlsVisibility(false);
//                } else {
//                    count = 0;
//                    stickerImgViews[i - 1].setControlsVisibility(true);
//                }
//                return true;
//            }
//            return true;
//        }
//    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BlendPictures.this, BlenderActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void SaveTheImage() {
        removeAllIconsOnImage();
        try {
            mBitmap = getScreenShot(v);
            Random generator = new Random();
            n = 10000;
            n = generator.nextInt(n);
            imageShare = "Image-" + n + ".png";
            //                shareImageFile = "image" + System.currentTimeMillis() + ".png";
            store(mBitmap, imageShare);
            Toast.makeText(BlendPictures.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileUriExposedException f) {
            f.printStackTrace();
        }
    }

    private Bitmap getScreenShot(View vi) {
        vi.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(vi.getDrawingCache());
        vi.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void store(Bitmap mbtmap, String fileName) {
        File dir = new File(ProjectUtils.savedPaths);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
//        photoFramesModel.setSavePath(dir + "/" + fileName);
//        File dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mbtmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
//            Snackbar.make(main, "Image Saved!!", Snackbar.LENGTH_SHORT).show();
//            floatingMenuButton.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}