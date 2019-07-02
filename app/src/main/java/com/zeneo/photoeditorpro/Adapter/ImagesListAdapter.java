package com.zeneo.photoeditorpro.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeneo.photoeditorpro.EditActivity;
import com.zeneo.photoeditorpro.GalleryActivity;
import com.zeneo.photoeditorpro.Model.Image;
import com.zeneo.photoeditorpro.R;
import com.zeneo.photoeditorpro.Utils;

import java.util.List;


public class ImagesListAdapter extends RecyclerView.Adapter<ImagesListAdapter.ViewHolder> {

    private Context context;
    private List<Image> list;

    public ImagesListAdapter(Context context, List<Image> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImagesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_images_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesListAdapter.ViewHolder viewHolder, final int i) {

        Glide.with(context).load(list.get(i).getPath()).into(viewHolder.imageView);
        ViewGroup.LayoutParams params = viewHolder.imageView.getLayoutParams();
        params.width = new Utils().getScreenWidth(context)/3;
        params.height = new Utils().getScreenWidth(context)/3;
        viewHolder.imageView.setLayoutParams(params);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("path",list.get(i).getPath());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ((GalleryActivity)context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_item);

        }
    }
}
