package com.zeneo.photoeditorpro.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zeneo.photoeditorpro.Adapter.TextSettingsListAdapter;
import com.zeneo.photoeditorpro.R;

import java.util.ArrayList;
import java.util.List;


public class AddTextDialog extends BottomSheetDialogFragment {

    private Context context;
    private OnTextSettingsChangedListener listener;
    private TextSettingsListAdapter.OnItemSelected itemSelected;




    int colorIndex = 0;
    int fontIndex = 0;

    public interface OnTextSettingsChangedListener {

        void onColorChanged(String Color);
        void onFontChanged(String fontPath);
        void onSubmit(String text);

    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(OnTextSettingsChangedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_text,container,false);



        RecyclerView colorRecycler = view.findViewById(R.id.color_list);
        RecyclerView fontRecycler = view.findViewById(R.id.font_list);



        TextView done = view.findViewById(R.id.done);
        final EditText editText = view.findViewById(R.id.textInput);
        TextView cancel = view.findViewById(R.id.cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText()!=null&& !editText.getText().toString().equals("")){

                    listener.onSubmit(editText.getText().toString());

                }else {
                }

                dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        colorRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayout.HORIZONTAL,false));
        fontRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayout.HORIZONTAL,false));

        itemSelected = new TextSettingsListAdapter.OnItemSelected() {


            @Override
            public void onSelectedFont(String data , int i) {

                fontIndex = i;

                listener.onFontChanged(data);

            }

            @Override
            public void onSelectedColor(String data,int i) {

                colorIndex = i;
                listener.onColorChanged(data);

            }


        };




        List<Boolean> selectedList = new ArrayList<>();
        for (int i = 0 ; i < getAllColors().size() ; i++) {

            if (i == colorIndex){
                selectedList.add(true);
            } else selectedList.add(false);

        }

        colorRecycler.setAdapter(new TextSettingsListAdapter(getAllColors(),context,"color", itemSelected,selectedList));







        List<Boolean> selectedFontList = new ArrayList<>();
        for (int i = 0 ; i < getAllFonts().size() ; i++) {

            if (i == fontIndex){
                selectedFontList.add(true);
            } else selectedFontList.add(false);

        }

        fontRecycler.setAdapter(new TextSettingsListAdapter(getAllFonts() ,context,"font", itemSelected,selectedFontList));






//        TextView textView = view.findViewById(R.id.done);
//
//        TextPaint paint = textView.getPaint();
//        float width = paint.measureText("Done");
//
//        Shader textShader = new LinearGradient(width/2, 0, width, textView.getTextSize(),
//                new int[]{
//                        Color.parseColor("#9C49A7"),
//                        Color.parseColor("#764BA1"),
//                        Color.parseColor("#9C49A7")
//                }, null, Shader.TileMode.CLAMP);
//        textView.getPaint().setShader(textShader);

        return view;
    }



    private List<String> getAllColors(){
        List<String> list = new ArrayList<>();

        list.add("#000000");
        list.add("#FFFFFF");
        list.add("#EE3E34");
        list.add("#FDE92B");
        list.add("#1D4F9C");
        list.add("#F09243");
        list.add("#54B948");
        list.add("#BDA0CC");
        list.add("#EEC3D0");
        list.add("#772120");
        list.add("#221F1F");
        list.add("#9BABB6");
        list.add("#FFE1BA");

        return list;
    }

    private List<String> getAllFonts(){
        List<String> list = new ArrayList<>();

        list.add("fonts/B612Mono-Regular.ttf");
        list.add("fonts/AguafinaScript-Regular.ttf");
        list.add("fonts/Merienda-Regular.ttf");
        list.add("fonts/Merriweather-Regular.ttf");
        list.add("fonts/Montserrat-Regular.ttf");
        list.add("fonts/RobotoMono-Regular.ttf");
        list.add("fonts/RussoOne-Regular.ttf");
        list.add("fonts/SwankyandMooMoo.ttf");
        list.add("fonts/Teko-Regular.ttf");
        list.add("fonts/Vollkorn-Regular.ttf");
        list.add("fonts/ZillaSlab-Regular.ttf");
        return list;
    }




}
