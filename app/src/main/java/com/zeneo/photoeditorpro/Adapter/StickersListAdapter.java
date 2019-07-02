package com.zeneo.photoeditorpro.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zeneo.photoeditorpro.R;
import com.zeneo.photoeditorpro.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class StickersListAdapter extends RecyclerView.Adapter<StickersListAdapter.ViewHolder> {

    private List<String> stickersPaths;
    private Context context;
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void itemSelected(String path);
    }

    public StickersListAdapter(List<String> stickersPaths, Context context, OnItemSelectedListener listener) {
        this.stickersPaths = stickersPaths;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sticker,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Utils utils = new Utils();
        int width = (utils.getScreenWidth(context)/5)-2*(utils.converDPToPX(context,5))-utils.converDPToPX(context,40)/5;

        ViewGroup.LayoutParams params = viewHolder.imageView.getLayoutParams();
        params.height = width;
        params.width = width;
        viewHolder.imageView.setLayoutParams(params);

        try {
            InputStream stream = context.getAssets().open(stickersPaths.get(i));
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(stream),150,150,false);
            Glide.with(context).load(bitmap).into(viewHolder.imageView);
            bitmap = null;
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemSelected(stickersPaths.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return stickersPaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_sticker);
        }
    }
}
