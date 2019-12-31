package com.ecs.sign.view.edit.window.attr;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.ecs.sign.R;
import com.ljx.view.FontResizeView;

public class TextSizeWindow {

    private TextView Hello;
    private Button cancel,ok;
    private View contentView;
    private Context mContext;
    private PopupWindow popupWindow;
    private OnTextSizeChangedListener textSizeChangedListener;
    private float textSize = 12;

    public TextSizeWindow(Context context) {
        mContext = context;
    }

    public interface OnTextSizeChangedListener{
        void onSizeChanged(float size);

    }

    public void setOnTextSizeChangedListener(OnTextSizeChangedListener onTextSizeChangedListener){
        this.textSizeChangedListener = onTextSizeChangedListener;
    }

    public void show(){
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_text_view_change_text_size,null);
        WindowManager manager = ((Activity) mContext).getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height * 2/5);
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.black)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        Hello = contentView.findViewById(R.id.text);
        cancel = contentView.findViewById(R.id.cancel);
        ok = contentView.findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSizeChangedListener.onSizeChanged(textSize);
                popupWindow.dismiss();
            }
        });

        final FontResizeView fontResizeView = contentView.findViewById(R.id.font_resize_view);
        fontResizeView.setOnFontChangeListener(new FontResizeView.OnFontChangeListener() {
            @Override
            public void onFontChange(float fontSize) {
                //字体size改变回调  单位:sp
                textSize =fontSize;
                Hello.setTextSize(fontSize);
            }
        });
        popupWindow.setAnimationStyle(R.style.popup_window_anim_style);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

    }
}
