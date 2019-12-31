package com.ecs.sign.view.edit.window.attr;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ecs.sign.R;
import com.ecs.sign.view.edit.window.BottomPopupHelper;


/**
 *
 */
public class TextTypefaceWindow {

    private Activity activity;
    private PopupWindow typefaceWindow;
    private View view;
    private RadioGroup radioGroup;
    private RadioButton radioButtonNormal;
    private RadioButton radioButtonBold;
    private RadioButton radioButtonItalic;
    private int currentIndex =0;
    private OnTypefaceChangedListener onTypefaceChangedListener;

    public void setTypeface(int index) {

        int selectId = index < 3 ? index : 0;
        currentIndex = selectId;
        switch (index) {
            case 0:
                radioButtonNormal.setChecked(true);
                break;
            case 1:
                radioButtonBold.setChecked(true);
                break;
            case 2:
                radioButtonItalic.setChecked(true);
                break;
        }
    }


    public TextTypefaceWindow(Activity activity) {
        this.activity = activity;
        initPopupWindow();
    }

    public interface OnTypefaceChangedListener {
        void onTypefaceChanged(int index);
    }

    public void setnTypefaceChangedListener(OnTypefaceChangedListener listener){
        onTypefaceChangedListener = listener;
    }

    private void initPopupWindow() {

        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_text_view_typeface, null, false);

        BottomPopupHelper helper = new BottomPopupHelper(view);
        typefaceWindow = helper.initPopWindow();

        radioButtonBold = view.findViewById(R.id.radio_btn_typeface_bold);
        radioButtonNormal = view.findViewById(R.id.radio_btn_typeface_normal);
        radioButtonItalic = view.findViewById(R.id.radio_btn_typeface_italic);

        view.findViewById(R.id.btn_add_text_view_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typefaceWindow.dismiss();
            }
        });

        radioGroup = view.findViewById(R.id.radio_group_add_text_view_detail);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_typeface_normal:
                        currentIndex = 0;
                        break;
                    case R.id.radio_btn_typeface_bold:
                        currentIndex = 1;
                        break;
                    case R.id.radio_btn_typeface_italic:
                        currentIndex = 2;
                        break;
                }
            }
        });

        view.findViewById(R.id.btn_add_text_view_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTypefaceChangedListener !=null){
                    onTypefaceChangedListener.onTypefaceChanged(currentIndex);
                }
                typefaceWindow.dismiss();
            }
        });

        typefaceWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                radioGroup.clearCheck();

            }
        });

    }

    public void show() {
        typefaceWindow.setAnimationStyle(R.style.popup_window_anim_style);
        typefaceWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
