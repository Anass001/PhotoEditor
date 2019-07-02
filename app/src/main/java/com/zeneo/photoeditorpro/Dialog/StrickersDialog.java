package com.zeneo.photoeditorpro.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeneo.photoeditorpro.Adapter.ViewPagerAdapter;
import com.zeneo.photoeditorpro.Fragment.StickersFragment;
import com.zeneo.photoeditorpro.R;

public class StrickersDialog extends BottomSheetDialogFragment {

    private OnStickerAddListener listener;

    public void setListener(OnStickerAddListener listener) {
        this.listener = listener;
    }

    public interface OnStickerAddListener{
        void onStickerAdded(String path);
    }

    ViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_stickers,container,false);

        ViewPager pager = view.findViewById(R.id.stickers_pager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        addFragmentToPager("Emoji");
        addFragmentToPager("Summer");
        addFragmentToPager("Love");
        addFragmentToPager("Family");
        addFragmentToPager("Tab5");
        addFragmentToPager("Tab6");
        pager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);

        return view;
    }

    public void addFragmentToPager(String Title){

        Bundle bundle = new Bundle();
        bundle.putString("category",Title);
        StickersFragment fragment = new StickersFragment();
        fragment.setArguments(bundle);
        fragment.setListener(listener);
        adapter.addFrag(fragment,Title);
    }
}