package com.zeneo.photoeditorpro;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.zeneo.photoeditorpro.Dialog.AddTextDialog;
import com.zeneo.photoeditorpro.Dialog.BrushDialog;
import com.zeneo.photoeditorpro.Dialog.EffectDialog;
import com.zeneo.photoeditorpro.Dialog.EraserDialog;
import com.zeneo.photoeditorpro.Dialog.FilterDialog;
import com.zeneo.photoeditorpro.Dialog.StrickersDialog;
import com.zeneo.photoeditorpro.Fragment.CropFragment;
import com.zeneo.photoeditorpro.Fragment.EditToolsFragment;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;

public class EditActivity extends AppCompatActivity {

    FrameLayout fragmentContainer;
    FrameLayout cropContainer;
    EditToolsFragment fragment;
    CropFragment cropFragment;
    FilterDialog dialog;
    EffectDialog effectDialog;

    String fontPath;
    String textColor;
    Bitmap bitmap;

    int brushOpacity = 100;
    int selectedFilterIndex = 0;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private int finalBrightness = 100;
    private float finalSaturation = 100;
    private float finalContrast = 0;


    public int getFinalBrightness() {
        return finalBrightness;
    }

    public float getFinalSaturation() {
        return finalSaturation;
    }

    public float getFinalContrast() {
        return finalContrast;
    }

    Bitmap finalBitmap;

    Uri cropUri;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    boolean isInTools = false;
    boolean isInCrop = false;

    Filter cFilter = new Filter();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.EDIT_AD_UNIT_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        photoEditorView = findViewById(R.id.photoEditor);
        photoEditor = new PhotoEditor.Builder(getApplicationContext(), photoEditorView).build();

        String path = getIntent().getStringExtra("path");
        bitmap = setupBitmapSize(BitmapFactory.decodeFile(path));


        fragmentContainer = findViewById(R.id.fragmentsContainer);
        cropContainer = findViewById(R.id.crop_container);

        photoEditorView.getSource().setImageBitmap(bitmap);

        setupFilter();
        setupEffect();

        findViewById(R.id.filter_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                dialog.show(getSupportFragmentManager(), "Filter");

            }
        });

        findViewById(R.id.effect_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                effectDialog.initAttrs(finalContrast,finalBrightness,finalSaturation);
                effectDialog.show(getSupportFragmentManager(), "Effect");
            }
        });

        findViewById(R.id.edit_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                isInTools = true;
                fragment = new EditToolsFragment();
                fragment.setListenner(listenner);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentsContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                fragmentContainer.setVisibility(View.VISIBLE);
                findViewById(R.id.tools).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.crop_lt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                cropFragment = new CropFragment();

                photoEditor.saveAsBitmap(new OnSaveBitmap() {
                    @Override
                    public void onBitmapReady(Bitmap saveBitmap) {
                        isInCrop = true;
                        cropFragment.setBitmap(saveBitmap);
                        cropFragment.setListener(new CropFragment.OnCropSaveListener() {
                            @Override
                            public void onSave(Uri uri) {
                                photoEditorView.getSource().setImageURI(uri);
                                bitmap = BitmapFactory.decodeFile(uri.getPath());
                                new File(uri.getPath()).delete();
                                closeCropFragment();
                            }
                        });
                        cropFragment.setInCrop(isInCrop);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.crop_container, cropFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        findViewById(R.id.bottombar).setVisibility(View.GONE);
                        findViewById(R.id.sd).setVisibility(View.GONE);
                        photoEditorView.setVisibility(View.GONE);
                        cropContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });


            }
        });

    }

    public void setupEffect(){

        effectDialog = new EffectDialog();
        effectDialog.initAttrs(finalContrast,finalBrightness,finalSaturation);
        effectDialog.setListener(new EffectDialog.OnFilterChangeListener() {
            @Override
            public void onBrightnessChanged(int value) {
                finalBrightness = value;
            }

            @Override
            public void onConstratChanged(float value) {
                finalContrast = value;
            }

            @Override
            public void onStaturationChanged(float value) {
                finalSaturation = value;
            }

            @Override
            public void onFiltreComplet() {

                finalBitmap = bitmap;

                ContrastSubFilter contrastSubFilter = new ContrastSubFilter(finalContrast / 100 + 1);
                contrastSubFilter.setTag("contrast");
                cFilter.removeSubFilterWithTag("contrast");
                cFilter.addSubFilter(contrastSubFilter);
                SaturationSubfilter saturationSubfilter = new SaturationSubfilter(finalSaturation / 100);
                saturationSubfilter.setTag("saturation");
                cFilter.removeSubFilterWithTag("saturation");
                cFilter.addSubFilter(saturationSubfilter);
                BrightnessSubFilter brightnessSubFilter = new BrightnessSubFilter(finalBrightness - 100);
                brightnessSubFilter.setTag("brightness");
                cFilter.removeSubFilterWithTag("brightness");
                cFilter.addSubFilter(brightnessSubFilter);
                photoEditorView.getSource().setImageBitmap(cFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));

            }
        });


    }

    public void setupFilter(){

        dialog = new FilterDialog();
        dialog.setBitmap(bitmap);
        dialog.setIndex(selectedFilterIndex);
        dialog.setupSelectedFilter(ThumbnailsManager.processThumbs(getApplicationContext()));
        dialog.setListener(new FilterDialog.OnFilterChangeListener() {
            @Override
            public void onFilterSelected(Filter filter,int index) {

                finalBitmap = bitmap;
                selectedFilterIndex = index;
                dialog.setIndex(selectedFilterIndex);
                cFilter = filter;
//                dialog.dismiss();
                finalContrast = 0;
                finalSaturation = 100;
                finalBrightness = 100;
                photoEditorView.getSource().setImageBitmap(cFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_edit_menu, menu);
        return true;
    }

    public void addText() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        final AddTextDialog textDialog = new AddTextDialog();
        final AddTextDialog.OnTextSettingsChangedListener listener;

        listener = new AddTextDialog.OnTextSettingsChangedListener() {
            @Override
            public void onColorChanged(String color) {

                textColor = color;

            }

            @Override
            public void onFontChanged(String font) {

                fontPath = font;

            }

            @Override
            public void onSubmit(String text) {

                if ((fontPath != null) && (textColor != null)) {

                    photoEditor.addText(Typeface.createFromAsset(getAssets(), fontPath), text, Color.parseColor(textColor));

                } else if ((fontPath == null) && (textColor != null)) {

                    photoEditor.addText(text, Color.parseColor(textColor));

                } else if ((fontPath != null) && (textColor == null)) {

                    photoEditor.addText(Typeface.createFromAsset(getAssets(), fontPath), text, Color.parseColor("#000000"));

                } else if ((fontPath == null) && textColor == null) {

                    photoEditor.addText(text, Color.parseColor("#000000"));

                }


            }

        };


        LinearLayout addText_lt = findViewById(R.id.addText_lt);
        addText_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.setContext(EditActivity.this);
                textDialog.setListener(listener);
                textDialog.show(getSupportFragmentManager(), "Add Text");
            }
        });


    }


    EditToolsFragment.OnToolSelectedListenner listenner = new EditToolsFragment.OnToolSelectedListenner() {
        @Override
        public void onAddTextSelected() {
            addText();
        }

        @Override
        public void onBrushSelected() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            switchToBrush();
        }

        @Override
        public void onEraserSlected() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }

            switchToEraser();
        }

        @Override
        public void onStickerSelected() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            addStickers();
        }
    };

    public void addStickers() {

        final StrickersDialog dialog = new StrickersDialog();
        dialog.setListener(new StrickersDialog.OnStickerAddListener() {
            @Override
            public void onStickerAdded(String path) {
                photoEditor.addImage(getBitmapFromAsset(getApplicationContext(), path));
                dialog.dismiss();
            }
        });

        LinearLayout stickers_lt = findViewById(R.id.stickers_lt);
        stickers_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show(getSupportFragmentManager(), "Stickers");

            }
        });
    }

    public void switchToBrush() {

        final BrushDialog brushDialog = new BrushDialog();

        photoEditor.setBrushDrawingMode(true);
        brushDialog.setColor(photoEditor.getBrushColor());
        brushDialog.setWidth((int) photoEditor.getBrushSize());
        brushDialog.setOpacity(brushOpacity);


        brushDialog.setListener(new BrushDialog.OnBrushChangedListener() {
            @Override
            public void onSizeChanged(int size) {
                photoEditor.setBrushSize(size);
            }

            @Override
            public void onOpacityChanged(int opacity) {
                photoEditor.setOpacity(opacity);
                brushOpacity = opacity;
            }

            @Override
            public void onColorChanged(String color, int index) {
                photoEditor.setBrushColor(Color.parseColor(color));
            }
        });

        brushDialog.show(getSupportFragmentManager(), "Brush");

    }


    public void switchToEraser() {

        final EraserDialog eraserDialog = new EraserDialog();
        eraserDialog.setSize((int) photoEditor.getEraserSize());
        eraserDialog.setListener(new EraserDialog.OnEraserChangeListener() {
            @Override
            public void onSizeChanged(int Size) {
                photoEditor.setBrushEraserSize(Size);
                photoEditor.brushEraser();
            }
        });

        photoEditor.brushEraser();
        eraserDialog.show(getSupportFragmentManager(), "Eraser");

    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }


        return bitmap;
    }

    public void onBackPressed() {
        if (isInTools) {

            AlertDialog dialog;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditActivity.this, R.style.SaveDialog);
            dialogBuilder.setTitle("Save Changes");
            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    photoEditor.saveAsBitmap(new SaveSettings.Builder().setCompressFormat(Bitmap.CompressFormat.JPEG).setCompressQuality(100).setClearViewsEnabled(true).build(),
                            new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    selectedFilterIndex = 0;
                                    finalBrightness = 100;
                                    finalContrast = 0;
                                    finalSaturation = 100;
                                    photoEditorView.getSource().setImageBitmap(saveBitmap);
                                    isInTools = false;
                                    photoEditor.clearAllViews();
                                    photoEditor.setBrushDrawingMode(false);
                                    findViewById(R.id.tools).setVisibility(View.VISIBLE);
                                    fragmentContainer.setVisibility(View.INVISIBLE);
                                    bitmap = saveBitmap;
                                    setupFilter();
                                    setupEffect();

                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                }
            });
            dialogBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isInTools = false;
                    findViewById(R.id.tools).setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.INVISIBLE);
                    photoEditor.clearAllViews();
                    photoEditor.setBrushDrawingMode(false);
                }
            });

            dialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });

            dialogBuilder.setMessage("Do you want Save changes ?");

            dialog = dialogBuilder.create();
            dialog.show();



        } else if (isInCrop) {
            closeCropFragment();
        } else {
            AlertDialog dialog;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditActivity.this, R.style.SaveDialog);
            dialogBuilder.setTitle("Discard Changes");
            dialogBuilder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.setMessage("Do you want Discard changes ?");

            dialog = dialogBuilder.create();
            dialog.show();
        }
    }

    public void closeCropFragment() {
        isInCrop = false;
        photoEditorView.setVisibility(View.VISIBLE);
        findViewById(R.id.bottombar).setVisibility(View.VISIBLE);
        findViewById(R.id.sd).setVisibility(View.VISIBLE);
        cropContainer.setVisibility(View.INVISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        cropFragment.setInCrop(isInCrop);
        transaction.replace(R.id.crop_container, new Fragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditActivity.this, R.style.SaveDialog);
        dialogBuilder.setTitle("Discard changes");
        dialogBuilder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (isInCrop)
                    closeCropFragment();
                else
                    finish();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setMessage("Do you want to discard the changes?");

        dialog = dialogBuilder.create();
        dialog.show();

        return super.onSupportNavigateUp();
    }

    public Bitmap setupBitmapSize(Bitmap bitmap){

        int width = bitmap.getWidth();
        int higth = bitmap.getHeight();

        if (width>=2500||higth>=2500){

            if (width>=higth){

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,2500,2500*higth/width,false);

                return scaledBitmap;

            }
            else {

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,2500*width/higth,2500,false);

                return scaledBitmap;

            }


        }else return bitmap;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finalBitmap = null;
        photoEditor = null;
        photoEditorView.getSource().setImageURI(null);
        photoEditorView = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isInCrop&&item.getItemId()==R.id.done) {
            AlertDialog dialog;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditActivity.this, R.style.SaveDialog);
            dialogBuilder.setTitle("Save Changes");
            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    photoEditor.saveAsBitmap(new OnSaveBitmap() {
                        @Override
                        public void onBitmapReady(Bitmap saveBitmap) {
                            String title = "Img_"+Calendar.getInstance().getTimeInMillis();
                            insertImage(getContentResolver(),saveBitmap,title,"");
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.setMessage("Do you want to save the changes?");

            dialog = dialogBuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

}