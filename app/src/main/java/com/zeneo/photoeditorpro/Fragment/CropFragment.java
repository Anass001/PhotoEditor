package com.zeneo.photoeditorpro.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import com.zeneo.photoeditorpro.Adapter.AspectRatioListAdapter;
import com.zeneo.photoeditorpro.Model.AspectRatio;
import com.zeneo.photoeditorpro.R;
import com.zeneo.photoeditorpro.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropFragment extends Fragment {


    public CropFragment() {
        // Required empty public constructor
    }

    private UCropView uCropView;
    private TextView scale_preview;
    private TextView degree_review;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    private Bitmap bitmap;
    private OnCropSaveListener listener;

    String path;
    Uri uri;
    String inputPath;

    private boolean isInCrop = false;

    public void setListener(OnCropSaveListener listener) {
        this.listener = listener;
    }

    public void setInCrop(boolean inCrop) {
        isInCrop = inCrop;
    }

    public interface OnCropSaveListener{
        void onSave(Uri uri);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private TransformImageView.TransformImageListener transformImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onLoadComplete() {

        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {

        }

        @Override
        public void onRotate(float currentAngle) {
            String angle = String.format(Locale.getDefault(), "%.1f°", currentAngle);
            degree_review.setText(angle);
        }

        @Override
        public void onScale(float currentScale) {
            String scale = String.format(Locale.getDefault(), "%d%%", (int) (currentScale * 100));
            scale_preview.setText(scale);
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop, container, false);

        if (isInCrop){
            setHasOptionsMenu(true);
            path = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/temp";


            uCropView = view.findViewById(R.id.ucrop);
            uri = getImageUri(getContext(),bitmap);
            try {
                uCropView.getCropImageView().setImageUri(uri,Uri.fromFile(new File(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            uCropView.getCropImageView().setTransformImageListener(transformImageListener);
            new File(uri.getPath()).delete();
            new File(path).delete();

            setUpTabLayout(view);
            linkViews(view);
            setUpProgressView(uCropView.getCropImageView(),view);
            rotateIcons(view);
            setUpAspectsRatio(view);
        }


        return view;
    }

    public void rotateIcons(View view){

        ImageView reset = view.findViewById(R.id.reset_rotation);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uCropView.getCropImageView().postRotate(-uCropView.getCropImageView().getCurrentAngle());
                uCropView.getCropImageView().setImageToWrapCropBounds();
            }
        });

        ImageView rotate90 = view.findViewById(R.id.rotate_90);
        rotate90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uCropView.getCropImageView().postRotate(90);
                uCropView.getCropImageView().setImageToWrapCropBounds();
            }
        });
    }

    public void setUpProgressView(final GestureCropImageView mGestureCropImageView,View view){

        HorizontalProgressWheelView scaleProgressView = view.findViewById(R.id.seek_scale);
        scaleProgressView.setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScrollStart() {
                mGestureCropImageView.cancelAllAnimations();
            }

            @Override
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0) {
                    mGestureCropImageView.zoomInImage(mGestureCropImageView.getCurrentScale()
                            + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                } else {
                    mGestureCropImageView.zoomOutImage(mGestureCropImageView.getCurrentScale()
                            + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                }
            }

            @Override
            public void onScrollEnd() {
                mGestureCropImageView.setImageToWrapCropBounds();
            }
        });

        HorizontalProgressWheelView angleProgressView = view.findViewById(R.id.seek_angle);
        angleProgressView.setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScrollStart() {
                mGestureCropImageView.cancelAllAnimations();
            }

            @Override
            public void onScroll(float delta, float totalDistance) {
                mGestureCropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
            }

            @Override
            public void onScrollEnd() {
                mGestureCropImageView.setImageToWrapCropBounds();
            }
        });

    }

    public void linkViews(View view){

        scale_preview = view.findViewById(R.id.scale_preview);
        degree_review = view.findViewById(R.id.degree_preview);

    }

    void setUpTabLayout (final View view){

        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    view.findViewById(R.id.scale_lt).setVisibility(View.VISIBLE);
                }else if (tab.getPosition() == 1){
                    view.findViewById(R.id.rotate_lt).setVisibility(View.VISIBLE);
                }else if (tab.getPosition() == 2){
                    view.findViewById(R.id.ratio_lt).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    view.findViewById(R.id.scale_lt).setVisibility(View.INVISIBLE);
                }else if (tab.getPosition() == 1){
                    view.findViewById(R.id.rotate_lt).setVisibility(View.INVISIBLE);
                }else if (tab.getPosition() == 2){
                    view.findViewById(R.id.ratio_lt).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void setUpAspectsRatio(View view){

        RecyclerView recyclerView = view.findViewById(R.id.ratio_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        List<AspectRatio> list = new ArrayList<>();
        list.add(new AspectRatio("Original",bitmap.getWidth(),bitmap.getHeight()));
        list.add(new AspectRatio("2 : 3",2,3));
        list.add(new AspectRatio("3 : 4",3,4));
        list.add(new AspectRatio("3 : 5",3,5));
        list.add(new AspectRatio("4 : 5",4,5));
        list.add(new AspectRatio("4 : 6",4,6));
        list.add(new AspectRatio("5 : 6",5,6));
        list.add(new AspectRatio("5 : 7",5,7));
        list.add(new AspectRatio("11 : 14",11,14));
        list.add(new AspectRatio("9 : 16",9,16));
        list.add(new AspectRatio("10 : 16",10,16));

        AspectRatioListAdapter adapter = new AspectRatioListAdapter(list,getContext());
        adapter.setItemSelectedListener(new AspectRatioListAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int hight, int width) {
                setAspectRatio(width,hight);
            }
        });

        adapter.setupSelectedList();

        recyclerView.setAdapter(adapter);

    }

    public void setAspectRatio(int width, int hight){
        uCropView.getCropImageView().setTargetAspectRatio((float) width/hight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                if (isInCrop){
                    AlertDialog dialog;
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(),R.style.SaveDialog);
                    dialogBuilder.setTitle("Save crop changes");
                    dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if(isInCrop){
                                uCropView.getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 100, new BitmapCropCallback() {
                                    @Override
                                    public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {

                                        listener.onSave(resultUri);
                                        dialog.cancel();
                                        Toast.makeText(getContext(),"Save",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCropFailure(@NonNull Throwable t) {
                                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    });
                    dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialogBuilder.setMessage("Save changes?");

                    dialog = dialogBuilder.create();
                    dialog.show();

                }

                break;
        }

        return false;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        inputPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "tempor", null);
        return Uri.parse(inputPath);
    }
}