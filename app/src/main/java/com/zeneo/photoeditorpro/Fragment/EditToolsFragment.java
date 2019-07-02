package com.zeneo.photoeditorpro.Fragment;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zeneo.photoeditorpro.Dialog.AddTextDialog;
import com.zeneo.photoeditorpro.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditToolsFragment extends Fragment {


    public EditToolsFragment() {
        // Required empty public constructor
    }

    public interface OnToolSelectedListenner {

        void onAddTextSelected();
        void onBrushSelected();
        void onEraserSlected();
        void onStickerSelected();

    }

    private OnToolSelectedListenner listenner;

    public void setListenner(OnToolSelectedListenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_tools, container, false);

        view.findViewById(R.id.addText_lt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listenner.onAddTextSelected();
                return false;
            }
        });

        view.findViewById(R.id.brush_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onBrushSelected();
            }

        });

        view.findViewById(R.id.eraser_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onEraserSlected();
            }
        });

        view.findViewById(R.id.stickers_lt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listenner.onStickerSelected();
                return false;
            }
        });
        return view;
    }






}
