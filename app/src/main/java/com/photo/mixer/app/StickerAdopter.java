package com.photo.mixer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StickerAdopter extends RecyclerView.Adapter<StickerAdopter.StickerViewHolder> {

    private final List<Integer> mstickerIds;
    private final Context mcontext;
    private final LayoutInflater mlayoutInflater;
    private StickersAdapterListener mStickersAdapterListeners;
    private int checkedPosition = -1;
    int postions;

    StickerAdopter(@NonNull List<Integer> stickerIds, @NonNull Context context, StickersAdapterListener mStickersAdapterListener) {
        this.mstickerIds = stickerIds;
        this.mcontext = context;
        this.mlayoutInflater = LayoutInflater.from(context);
        this.mStickersAdapterListeners = mStickersAdapterListener;
    }

    @Override
    public StickerAdopter.StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StickerAdopter.StickerViewHolder(mlayoutInflater.inflate(R.layout.sticker_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final StickerViewHolder holder, final int position) {
        holder.image.setImageDrawable(ContextCompat.getDrawable(mcontext, getItem(position)));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStickersAdapterListeners != null) {
                    mStickersAdapterListeners.onStickersSelected(getItem(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mstickerIds.size();
    }

    private int getItem(int position) {
        return mstickerIds.get(position);
    }

    class StickerViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        StickerViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.sticker_image);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    image.setVisibility(View.VISIBLE);
//                    if (checkedPosition != getAdapterPosition()) {
//                        notifyItemChanged(checkedPosition);
//                        checkedPosition = getAdapterPosition();
//                    }
//                }
//            });


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getAdapterPosition();
//                    if (pos >= 0) { // might be NO_POSITION
////                        StickerImageView iv_sticker = new StickerImageView(StickerAdopter.this);
////                            iv_sticker.removeAllViews();
////                        iv_sticker.setImageResource(getItem(pos));
////                        canvas.addView(iv_sticker);
////                            onStickerSelected(getItem(pos));
//                    }
//                }
//            });

        }

    }


    public interface StickersAdapterListener {
        void onStickersSelected(Integer Sticker);
    }
}