package com.zeneo.photoeditorpro;

import android.content.Context;
import android.graphics.Bitmap;

import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailsManager {

    private static List<ThumbnailItem> filterThumbs = new ArrayList<ThumbnailItem>(10);
    private static List<ThumbnailItem> processedThumbs = new ArrayList<ThumbnailItem>(10);

    private ThumbnailsManager() {
    }


    public static void addThumb(ThumbnailItem thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<ThumbnailItem> processThumbs(Context context) {
        for (ThumbnailItem thumb : filterThumbs) {
            // scaling down the image
            int width = thumb.image.getWidth();
            int hight = thumb.image.getHeight();
            thumb.image = Bitmap.createScaledBitmap(thumb.image, 200, 200*hight/width, false);
            thumb.image = thumb.filter.processFilter(thumb.image);
            // cropping circle

            // TODO - think about circular thumbnails
            // thumb.image = GeneralUtils.generateCircularBitmap(thumb.image);
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList<>();
        processedThumbs = new ArrayList<>();
    }

}
