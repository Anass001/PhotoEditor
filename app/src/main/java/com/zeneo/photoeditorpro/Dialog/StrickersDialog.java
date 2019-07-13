package com.zeneo.photoeditorpro.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zeneo.photoeditorpro.Adapter.StickersListAdapter;
import com.zeneo.photoeditorpro.Adapter.ViewPagerAdapter;
import com.zeneo.photoeditorpro.R;

import java.util.ArrayList;
import java.util.List;

public class StrickersDialog extends BottomSheetDialogFragment {

    private OnStickerAddListener listener;
    List<String> stickersList;


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

        final RecyclerView recyclerView = view.findViewById(R.id.stickers_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(true);

        TabLayout tabLayout = view.findViewById(R.id.tablayout);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recyclerView.setAdapter(new StickersListAdapter(getStickersList(tab.getText().toString()), getContext(), new StickersListAdapter.OnItemSelectedListener() {
                    @Override
                    public void itemSelected(String path) {
                        listener.onStickerAdded(path);
                    }
                }));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                recyclerView.setAdapter(new StickersListAdapter(getStickersList(tab.getText().toString()), getContext(), new StickersListAdapter.OnItemSelectedListener() {
                    @Override
                    public void itemSelected(String path) {
                        listener.onStickerAdded(path);
                    }
                }));
            }
        });


        tabLayout.getTabAt(0).select();

        return view;
    }

    public List<String> getStickersList(String category){
        stickersList = new ArrayList<>();

        if (category != null) {

            if(category.equals("Emoji")){
                stickersList.clear();
                listAssetsFiles("emojies");
                listAssetsFiles("emojies2");

            } else if (category.equals("Summer")){
                stickersList.clear();
                listAssetsFiles("summer");

            } else if (category.equals("Text")){
                stickersList.clear();
                listAssetsFiles("text");

            } else if (category.equals("Love")){
                stickersList.clear();
                listAssetsFiles("love");

            } else if (category.equals("Face")){
                stickersList.clear();
                listAssetsFiles("face");

            } else if (category.equals("Kawaii")){
                stickersList.clear();
                listAssetsFiles("kawaii");

            } else if (category.equals("Vaporwave")){
                stickersList.clear();
                listAssetsFiles("vaporwave");

            }
//            else if (category.equals("Tab5")){
//                stickersList.clear();
//
//            } else if (category.equals("Tab6")){
//                stickersList.clear();
//
//            }
        }
        return stickersList;
    }

    private boolean listAssetsFiles(String path){

        String[] list;

        try{
            list = getContext().getAssets().list(path);
            assert list != null;
            if (list.length>0){
                for (String file : list){
                    if (!listAssetsFiles(path+"/"+file)){
                        return false;
                    }
                    else {
                        Log.e("fuck", path+"/"+file);
                        stickersList.add(path+"/"+file);
                    }
                }
            }
        }
        catch (Exception e){
            return false;
        }

        return true;
    }
}