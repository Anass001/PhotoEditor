package com.zeneo.photoeditorpro.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zeneo.photoeditorpro.EditActivity;
import com.zeneo.photoeditorpro.Model.AspectRatio;
import com.zeneo.photoeditorpro.R;

import java.util.List;

public class AspectRatioListAdapter extends RecyclerView.Adapter<AspectRatioListAdapter.ViewHolder> {

    private List<AspectRatio> list;
    private Context context;
    private int lastItemPosition = -1;
    ViewHolder mViewHolder;

    public interface OnItemSelectedListener {
        void onItemSelected(int hight , int width);
    }

    OnItemSelectedListener itemSelectedListener;

    public void setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public AspectRatioListAdapter(List<AspectRatio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_ratio,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.textView.setText(list.get(i).getName());

        if(i==0){
            viewHolder.view.setVisibility(View.VISIBLE);
            mViewHolder = viewHolder;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.view.setVisibility(View.VISIBLE);

                if(mViewHolder!=null){
                    if(mViewHolder != viewHolder){
                        mViewHolder.view.setVisibility(View.INVISIBLE);
                        itemSelectedListener.onItemSelected(list.get(i).getHight(),list.get(i).getWidth());
                    }
                }
                mViewHolder = viewHolder;

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            view = itemView.findViewById(R.id.indicator);
        }

    }
}
