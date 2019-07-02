package com.zeneo.photoeditorpro.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.zeneo.photoeditorpro.Adapter.TextSettingsListAdapter;
import com.zeneo.photoeditorpro.R;

import java.util.ArrayList;
import java.util.List;

public class BrushDialog extends BottomSheetDialogFragment {


    public interface OnBrushChangedListener{

        void onSizeChanged(int size);
        void onOpacityChanged(int opacity);
        void onColorChanged(String color,int index);

    }

    private int colorIndex;

    private OnBrushChangedListener listener;

    public void setListener(OnBrushChangedListener listener) {
        this.listener = listener;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    private int color;
    private int width;
    private int opacity;

    public void setColor(int color) {
        this.color = color;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_brush,container,false);

        final RecyclerView colorRecycler = view.findViewById(R.id.color_list);
        SeekBar sizeBar = view.findViewById(R.id.brush_size_progress);
        SeekBar opacityBar = view.findViewById(R.id.opacity_progress);



        final TextSettingsListAdapter.OnItemSelected itemSelected = new TextSettingsListAdapter.OnItemSelected() {

            @Override
            public void onSelectedFont(String data,int i) {


            }

            @Override
            public void onSelectedColor(String data,int i) {
                colorIndex = i;

                listener.onColorChanged(data,i);

            }
        };

        sizeBar.setProgress(width);
        opacityBar.setProgress(opacity);

        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                listener.onSizeChanged(progress);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        opacityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onOpacityChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        List<Boolean> selectedList = new ArrayList<>();
        for (int i = 0 ; i < getAllColors().size() ; i++) {

            if (i == getAllColors().indexOf(String.format("#%06X", 0xFFFFFF & color))){
                selectedList.add(true);
            } else selectedList.add(false);

        }


        colorRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayout.HORIZONTAL,false));
        colorRecycler.setAdapter(new TextSettingsListAdapter(getAllColors(),getContext(),"color",itemSelected,selectedList));


        return view;
    }

    private List<String> getAllColors(){
        List<String> list = new ArrayList<>();

        list.add("#000000");
        list.add("#FFFFFF");
        list.add("#EE3E34");
        list.add("#FDE92B");
        list.add("#1D4F9C");
        list.add("#F09243");
        list.add("#54B948");
        list.add("#BDA0CC");
        list.add("#EEC3D0");
        list.add("#772120");
        list.add("#221F1F");
        list.add("#9BABB6");
        list.add("#FFE1BA");

        return list;
    }


}
