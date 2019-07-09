package com.zeneo.photoeditorpro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.zeneo.photoeditorpro.Adapter.ImagesListAdapter;
import com.zeneo.photoeditorpro.Model.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    AnimatedRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        recyclerView = findViewById(R.id.imageslist);
        ImagesListAdapter adapter = new ImagesListAdapter(GalleryActivity.this,getImagesPath());
        recyclerView.setAdapter(adapter);

        setupTextFonts();

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public List<Image> getImagesPath() {
        Uri uri;
        List<Image> listOfAllImages = new ArrayList<>();
        Cursor cursor;
        int column_index_data, column_index_name, column_index_size, column_index_modification;
        String PathOfImage = null;
        String name = null;
        int modifie;
        int size;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE
        };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_modification = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
        column_index_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            name = cursor.getString(column_index_name);
            modifie = cursor.getInt(column_index_modification);
            size = cursor.getInt(column_index_size);

            listOfAllImages.add(new Image(PathOfImage, name, size + "", modifie+""));
        }

        Collections.sort(listOfAllImages, new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                return o1.getModifation().compareTo(o2.getModifation());
            }
        });

        Collections.reverse(listOfAllImages);

        return listOfAllImages;
    }

    public void setupTextFonts(){
        TextView textView = findViewById(R.id.toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        textView.setTypeface(typeface);
    }
}