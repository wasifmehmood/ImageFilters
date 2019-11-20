package com.photo.mixer.app;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemSpaceDecorations extends RecyclerView.ItemDecoration {
    private int spaces;

    public ItemSpaceDecorations(int space) {
        this.spaces = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.left = spaces;
            outRect.right = 0;
        } else {
            outRect.right = spaces;
            outRect.left = 0;
        }
    }
}