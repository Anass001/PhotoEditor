package com.zeneo.photoeditorpro.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.zeneo.photoeditorpro.R;

import static android.widget.ListPopupWindow.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class EraserDialog extends BottomSheetDialogFragment {


    public interface OnEraserChangeListener{
        void onSizeChanged(int Size);
    }

    private OnEraserChangeListener listener;
    private int size;
    public void setListener(OnEraserChangeListener listener) {
        this.listener = listener;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_eraser,container,false);

        SeekBar eraserProgress = view.findViewById(R.id.eraser_size_progress);
        eraserProgress.setProgress(size);
        eraserProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        return view;
    }
}
