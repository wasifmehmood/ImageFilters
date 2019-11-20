package com.photo.mixer.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class StickerImgView extends StickerView {

    private String mowner_ids;
    private ImageView miv_mains;

    public StickerImgView(Context context) {
        super(context);
    }

    public void setMaxHeight(int setMaxHeight) {
        miv_mains.setMaxHeight(setMaxHeight);
    }

    public void VisibilityControls() {
//        super.setControlsVisibility();
    }

    public void setimageAplha(int alpha) {
        miv_mains.setAlpha(alpha);
    }

    public void setMaxWidth(int setMaxWidth) {
        miv_mains.setMaxWidth(setMaxWidth);
    }

    public StickerImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerImgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOwnerId(String owner_id) {
        this.mowner_ids = owner_id;
    }

    public String getOwnerId() {
        return this.mowner_ids;
    }

    @Override
    public View getMainView() {
        if (this.miv_mains == null) {
            this.miv_mains = new ImageView(getContext());
            this.miv_mains.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return miv_mains;
    }

    public void setImageBitmap(Bitmap bmp) {
        this.miv_mains.setImageBitmap(bmp);
    }

    public void setImageResource(int res_id) {
        this.miv_mains.setImageResource(res_id);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.miv_mains.getDrawable()).getBitmap();
    }
}
