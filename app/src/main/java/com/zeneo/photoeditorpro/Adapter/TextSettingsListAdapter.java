package com.zeneo.photoeditorpro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zeneo.photoeditorpro.R;
import java.util.List;

public class TextSettingsListAdapter extends RecyclerView.Adapter<TextSettingsListAdapter.ViewHolder> {

    private List<String> list;
    private Context context;
    private String type;
    private OnItemSelected itemSelected;
    private List<Boolean> selectedList;

    public interface OnItemSelected {
        void onSelectedFont(String data,int index);
        void onSelectedColor(String data,int index);
    }

    public TextSettingsListAdapter(List<String> list, Context context, String type, OnItemSelected itemSelected) {
        this.list = list;
        this.context = context;
        this.type = type;
        this.itemSelected = itemSelected;
    }

    public TextSettingsListAdapter(List<String> list, Context context, String type, OnItemSelected itemSelected, List<Boolean> selectedList) {
        this.list = list;
        this.context = context;
        this.type = type;
        this.itemSelected = itemSelected;
        this.selectedList = selectedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ViewHolder viewHolder;
        if (type.equals("color")){
            viewHolder =  new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_color,viewGroup,false));
        }
        else {
            viewHolder =  new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_font,viewGroup,false));

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        if(type == "font"){



            Typeface typeface = Typeface.createFromAsset(context.getAssets(),list.get(i));
            viewHolder.textView.setTypeface(typeface);

            if(selectedList.get(i)){
                viewHolder.textView.setSelected(true);
            } else  viewHolder.textView.setSelected(false);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemSelected.onSelectedFont(list.get(i),i);
                    setSelected(i);
                }
            });


        }else if(type == "color"){


            String color = list.get(i);

            if(selectedList.get(i)){
                viewHolder.frameLayout.setSelected(true);
            } else  viewHolder.frameLayout.setSelected(false);





            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor(color));
            drawable.setShape(GradientDrawable.OVAL);
            viewHolder.imageView.setBackground(drawable);



            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemSelected.onSelectedColor(list.get(i),i);
                    setSelected(i);
                }
            });





        }



    }

    public void setSelected(int pos) {
        try {
            selectedList.set(selectedList.indexOf(true),false);
            selectedList.set(pos,true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.color_preview);
            frameLayout = itemView.findViewById(R.id.color_preview_container);
            textView = itemView.findViewById(R.id.item_font);

        }

    }
}
