package com.zeneo.photoeditorpro.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeneo.photoeditorpro.Adapter.StickersListAdapter;
import com.zeneo.photoeditorpro.Dialog.StrickersDialog;
import com.zeneo.photoeditorpro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StickersFragment extends Fragment {

    List<String> stickersList;

    public StickersFragment() {
        // Required empty public constructor
    }

    private StrickersDialog.OnStickerAddListener listener;

    public void setListener(StrickersDialog.OnStickerAddListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.stickers_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new StickersListAdapter(getStickersList(), getContext(), new StickersListAdapter.OnItemSelectedListener() {
            @Override
            public void itemSelected(String path) {
                listener.onStickerAdded(path);
            }
        }));

        return view;
    }

    public List<String> getStickersList(){
        stickersList = new ArrayList<>();

        String category = getArguments().getString("category");

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

            }
            else if (category.equals("Face")){
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
////                stickersList.clear();
//
//            } else if (category.equals("Tab6")){
////                stickersList.clear();
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