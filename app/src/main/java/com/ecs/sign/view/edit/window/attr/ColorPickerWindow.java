package com.ecs.sign.view.edit.window.attr;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.view.edit.window.BottomPopupHelper;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class ColorPickerWindow {

    private Activity activity;
    private PopupWindow colorPickerWindow;
    private View view;
    private ColorPicker picker;
    private OpacityBar opacityBar;
    private ValueBar valueBar;
    private OnColorChangedListener onColorChangedListener;
    public ColorPickerWindow(Activity activity) {
        this.activity = activity;
        initPopupWindow();
    }

    public void setColor(int color){
        if (color>0){
            picker.setColor(color);
        }
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener){
        this.onColorChangedListener = onColorChangedListener;
    }

    public interface OnColorChangedListener{
        void onColorChanged(int color);
    }

    private void initPopupWindow() {

        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_text_view_color_picker, null, false);

        BottomPopupHelper bottomPopupHelper = new BottomPopupHelper(view);
        colorPickerWindow = bottomPopupHelper.initPopWindow();

        picker = view.findViewById(R.id.picker);

        opacityBar = view.findViewById(R.id.opacitybar);

        valueBar = view.findViewById(R.id.valuebar);

        picker.addOpacityBar(opacityBar);
        picker.addValueBar(valueBar);
        picker.setShowOldCenterColor(false);

        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {

            }
        });

        //设置popupWindow里的按钮的事件
        view.findViewById(R.id.btn_add_text_view_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerWindow.dismiss();
            }
        });

        view.findViewById(R.id.btn_add_text_view_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onColorChangedListener != null) {
                    int color = picker.getColor();
                    onColorChangedListener.onColorChanged(color);
                }
                colorPickerWindow.dismiss();
            }
        });
    }

    public void show() {
        colorPickerWindow.setAnimationStyle(R.style.popup_window_anim_style);
        colorPickerWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
