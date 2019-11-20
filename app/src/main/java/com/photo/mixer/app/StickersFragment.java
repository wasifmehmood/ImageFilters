package com.photo.mixer.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.mixer.app.R;

import java.util.ArrayList;
import java.util.List;


public class StickersFragment extends Fragment implements StickerAdopter.StickersAdapterListener {

    private FragmentStickersAdapterListener fragmentStickersAdapterListener;
    private Context context;

    public void setListener(StickersFragment.FragmentStickersAdapterListener listener) {
        this.fragmentStickersAdapterListener = listener;
    }

    public StickersFragment(Context context) {
        this.context = context;
    }
    //    public StickersFragment(Context context) {
//        // Required empty public constructor
//        if (context instanceof PhotoEditor){
//            fragmentStickersAdapterListener = PhotoEditor.this;
//        }
//    }

    private final int[] stickerIds = {
            R.drawable.eye, R.drawable.face, R.drawable.glas1, R.drawable.glas2,R.drawable.flower, R.drawable.fram, R.drawable.frame, R.drawable.frm,
            R.drawable.glass,  R.drawable.heart, R.drawable.hrt, R.drawable.roses,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stickers_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        List<Integer> stickers = new ArrayList<>(stickerIds.length);
        for (Integer id : stickerIds) {
            stickers.add(id);
        }

        recyclerView.setAdapter(new StickerAdopter(stickers, context, this));
        return view;
    }

    @Override
    public void onStickersSelected(Integer Sticker) {
        if (fragmentStickersAdapterListener != null) {
            fragmentStickersAdapterListener.onStickersSelected(Sticker);
        }
    }

    public interface FragmentStickersAdapterListener {
        void onStickersSelected(Integer Sticker);
    }
}