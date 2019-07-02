package com.zeneo.photoeditorpro.Dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.zeneo.photoeditorpro.Adapter.FilterListAdapter;
import com.zeneo.photoeditorpro.R;
import com.zeneo.photoeditorpro.ThumbnailsManager;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import java.util.ArrayList;
import java.util.List;

public class FilterDialog extends BottomSheetDialogFragment {

    public static FilterDialog newInstance() {
        return new FilterDialog();
    }

    List<Boolean> isSelectedList;

    public interface OnFilterChangeListener{
        void onFilterSelected(Filter filter,int index);
    }

    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    private OnFilterChangeListener listener;
    private Bitmap bitmap;
    private RecyclerView recyclerView;

    public void setListener(OnFilterChangeListener listener) {
        this.listener = listener;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter,container,false);

        recyclerView = view.findViewById(R.id.filters_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        bindDataToAdapter();

        return view;
    }

    private void bindDataToAdapter() {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = bitmap;

                ThumbnailsManager.clearThumbs();
                List<Filter> filters = FilterPack.getFilterPack(getContext());

                ThumbnailItem originalThumb = new ThumbnailItem();
                originalThumb.image = thumbImage;
                originalThumb.filter = new Filter();
                originalThumb.filterName = "Original";
                ThumbnailsManager.addThumb(originalThumb);

                for (Filter filter : filters) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = thumbImage;
                    thumbnailItem.filter = filter;
                    thumbnailItem.filterName = filter.getName();
                    ThumbnailsManager.addThumb(thumbnailItem);
                }

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(getContext());

                isSelectedList = new ArrayList<>();
                for (int i = 0 ; i < thumbs.size() ; i++) {
                    if(i == index)
                        isSelectedList.add(true);
                    else
                        isSelectedList.add(false);

                }

                final FilterListAdapter adapter = new FilterListAdapter(thumbs,isSelectedList,listener,getContext());
                recyclerView.setAdapter(adapter);

                /*
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        adapter.setSelected(position);

                    }
                }));

                */
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener mListener;

        public interface OnItemClickListener {

            public void onItemClick(View view, int position);

        }


        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }

            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
    }



}
