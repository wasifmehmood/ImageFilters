package com.photo.mixer.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhotoEditor extends AppCompatActivity implements FiltersFragment.FiltersListFragmentListener, EditorFragment.EditImageFragmentListener, StickersFragment.FragmentStickersAdapterListener {

    public static final String IMAGE_NAME = "dog.jpg";
    public static final int SELECT_GALLERY_IMAGE = 101;
    private static final String TAG = PhotoEditor.class.getSimpleName();
    // load native image filters library
    static String photoPath;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    ImageView imagePreview;
    //    TabLayout tabLayout;
    ViewPager mviewPager;
    CoordinatorLayout coordinatorLayouts;
    Bitmap moriginalImage;
    // to backup image with filter applied
    Bitmap mfilteredImage;
    // the final image after applying
    // brightness, saturation, contrast
    Bitmap mfinalImage;
    FiltersFragment mfiltersListFragment;
    EditorFragment meditImageFragment;
    StickersFragment mstickersFragment;
    // modified image values
    int brightnessFinals = 0;
    float saturationFinals = 1.0f;
    float contrastFinalss = 1.0f;
    TabLayout mtabLayout;
    ImageView stickerPreview;
    FrameLayout mcanvas;
    String imageShare;
    Bitmap mBitmap;
    int n;
    ImageView mimageDreview;
    RelativeLayout SaveImgs;
    InterstitialAd mInterstitialAd;

    public PhotoEditor() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);
        InitViews();

//        MobileAds.initialize(this, getResources().getString(R.string.appid));

        loadImage();
        setupViewPager(mviewPager);
        mtabLayout.setupWithViewPager(mviewPager);
        SaveImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveTheImage();

            }
        });
    }

    public void InitViews() {
        SaveImgs = findViewById(R.id.SaveImgs);
        imagePreview = findViewById(R.id.image_preview);
        mcanvas = (FrameLayout) findViewById(R.id.Uripreview);
        mviewPager = findViewById(R.id.viewpager);
        mtabLayout = findViewById(R.id.tabs);
        mimageDreview = findViewById(R.id.image_preview);


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // adding filter list fragment
        mfiltersListFragment = new FiltersFragment();
        mfiltersListFragment.setListener(this);
        // adding edit image fragment
        meditImageFragment = new EditorFragment();
        meditImageFragment.setListener(this);
        mstickersFragment = new StickersFragment(this);
        mstickersFragment.setListener(this);
        adapter.addFragment(mfiltersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(meditImageFragment, getString(R.string.tab_edit));
        adapter.addFragment(mstickersFragment, getString(R.string.tab_sticker));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // reset image controls
        resetControls();
//        originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.alex);
        // applying the selected filter
        mfilteredImage = moriginalImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        mimageDreview.setImageBitmap(filter.processFilter(mfilteredImage));
        mfinalImage = mfilteredImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    public void onBrightnessChanged(final int brightness) {
        brightnessFinals = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        imagePreview.setImageBitmap(myFilter.processFilter(mfinalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(final float saturation) {
        saturationFinals = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        imagePreview.setImageBitmap(myFilter.processFilter(mfinalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onContrastChanged(final float contrast) {
        contrastFinalss = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        imagePreview.setImageBitmap(myFilter.processFilter(mfinalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        // once the editing is done i.e seekbar is drag is completed,
        // apply the values on to filtered image
        final Bitmap bitmap = mfilteredImage.copy(Bitmap.Config.ARGB_8888, true);
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinals));
        myFilter.addSubFilter(new ContrastSubFilter(contrastFinalss));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinals));
        mfinalImage = myFilter.processFilter(bitmap);
    }


    private void resetControls() {
        if (meditImageFragment != null) {
            meditImageFragment.resetControls();
        }
        brightnessFinals = 0;
        saturationFinals = 1.0f;
        contrastFinalss = 1.0f;
    }

    @Override
    public void onStickersSelected(Integer Sticker) {
//        Toast.makeText(this, "This is Sticker Id " + Sticker, Toast.LENGTH_SHORT).show();
        StickerImgView iv_sticker = new StickerImgView(this);
        mcanvas.addView(iv_sticker);
        iv_sticker.setImageResource(Sticker);
//        imagePreview.setImageResource(Sticker);
    }

    // load the default image from assets on app launch
    private void loadImage() {

        photoPath = PickImageEditor.selectedimagesUri.toString();
        try {
            moriginalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        originalImage = btmapUtils.getBitmapFromAssets(this, IMAGE_NAME, 300, 300);
//        originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.alex);
        mfilteredImage = moriginalImage.copy(Bitmap.Config.ARGB_8888, true);
        mfinalImage = moriginalImage.copy(Bitmap.Config.ARGB_8888, true);
        imagePreview.setImageBitmap(moriginalImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY_IMAGE) {
            Bitmap bitmap = btmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

            // clear bitmap memory
            moriginalImage.recycle();
            mfinalImage.recycle();
            mfinalImage.recycle();
            moriginalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            mfilteredImage = moriginalImage.copy(Bitmap.Config.ARGB_8888, true);
            mfinalImage = moriginalImage.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(moriginalImage);
            bitmap.recycle();

            // render selected image thumbnails
            mfiltersListFragment.mprepareThumbnail(moriginalImage);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void SaveTheImage() {
        try {
            mBitmap = getScreenShot(mcanvas);
            Random generator = new Random();
            n = 10000;
            n = generator.nextInt(n);
            imageShare = "Image-" + n + ".png";
            //                shareImageFile = "image" + System.currentTimeMillis() + ".png";
            store(mBitmap, imageShare);
            Toast.makeText(PhotoEditor.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}