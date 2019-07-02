package com.zeneo.photoeditorpro.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_effect,container,false);
        float Contrast = ((EditActivity)getContext()).getFinalContrast();
        int BrightNess = ((EditActivity)getContext()).getFinalBrightness();
        float Saturation = ((EditActivity)getContext()).getFinalSaturation();

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
