package com.zeneo.photoeditorpro.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zeneo.photoeditorpro.Dialog.FilterDialog;
import com.zeneo.photoeditorpro.R;
import com.zomato.photofilters.utils.ThumbnailItem;
import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder> {

    private static final String TAG = "THUMBNAILS_ADAPTER";
    private static int lastPosition = -1;
    private List<ThumbnailItem> dataSet;
    private List<Boolean> isSelectedList;
    private FilterDialog.OnFilterChangeListener listener;
    ViewHolder mViewHolder;
    Context context;

    public FilterListAdapter(List<ThumbnailItem> dataSet, List<Boolean> isSelectedList, FilterDialog.OnFilterChangeListener listener, Context context) {
        this.dataSet = dataSet;
        this.isSelectedList = isSelectedList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_filter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final ThumbnailItem thumbnailItem = dataSet.get(i);
        viewHolder.imageView.setImageBitmap(thumbnailItem.image);
        viewHolder.textView.setText(thumbnailItem.filterName);

        if(isSelectedList.get(i)){
            viewHolder.frame.setSelected(true);
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else{
            viewHolder.frame.setSelected(false);
            viewHolder.textView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterSelected(thumbnailItem.filter, i);
//                Toast.makeText(context,i+"",Toast.LENGTH_SHORT).show();
                setSelected(i);

            }
        });
    }

    public void setSelected(int pos) {
        try {
            isSelectedList.set(isSelectedList.indexOf(true),false);
            isSelectedList.set(pos,true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        FrameLayout frame;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.filter_thumb);
            textView = itemView.findViewById(R.id.filter_name);
            frame = itemView.findViewById(R.id.frame);

        }
    }

}
