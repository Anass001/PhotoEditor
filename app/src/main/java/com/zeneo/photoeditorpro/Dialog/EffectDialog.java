package com.zeneo.photoeditorpro.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import com.zeneo.photoeditorpro.EditActivity;
import com.zeneo.photoeditorpro.R;


public class EffectDialog extends BottomSheetDialogFragment {

    private OnFilterChangeListener listener;

    public void setListener(OnFilterChangeListener listener) {
        this.listener = listener;
    }

    public interface OnFilterChangeListener{

        void onBrightnessChanged(int value);
        void onConstratChanged(float value);
        void onStaturationChanged(float value);
        void onFiltreComplet();

    }

    float Contrast;
    int BrightNess;
    float Saturation;

    public void initAttrs(float contrast, int brightNess, float saturation){

        Contrast = contrast;
        BrightNess = brightNess;
        Saturation = saturation;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_effect,container,false);
        Contrast = ((EditActivity)getContext()).getFinalContrast();
        BrightNess = ((EditActivity)getContext()).getFinalBrightness();
        Saturation = ((EditActivity)getContext()).getFinalSaturation();

        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        SeekBar brightnessProgress = view.findViewById(R.id.brightness_bar);
        brightnessProgress.setProgress(BrightNess);
        brightnessProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrightnessChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                listener.onFiltreComplet();
            }
        });

        SeekBar contrastProgress = view.findViewById(R.id.contrast_bar);
        contrastProgress.setProgress((int) (Contrast));
        contrastProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onConstratChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                listener.onFiltreComplet();
            }
        });

        SeekBar saturationProgress = view.findViewById(R.id.saturation_bar);
        saturationProgress.setProgress((int) (Saturation));
        saturationProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onStaturationChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                listener.onFiltreComplet();
            }
        });


        return view;
    }
}
