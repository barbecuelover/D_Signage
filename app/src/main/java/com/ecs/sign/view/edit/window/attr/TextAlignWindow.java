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


public class TextAlignWindow {

    private Activity activity;
    private PopupWindow alignWindow;
    private View view;
    private RadioGroup radioGroup;
    private RadioButton radioButtonLeft;
    private RadioButton radioButtonRight;
    private RadioButton radioButtonCenter;
    private int currentIndex =1;
    private OnAlignChangedListener onAlignChangedListener;

    public void setAlign(int index) {
        int selectId = index < 3 ? index : 0;
        currentIndex = selectId;
        switch (index) {
            case 0:
                radioButtonLeft.setChecked(true);
                break;
            case 1:
                radioButtonCenter.setChecked(true);
                break;
            case 2:
                radioButtonRight.setChecked(true);
                break;
        }
    }


    public TextAlignWindow(Activity activity) {
        this.activity = activity;
        initPopupWindow();
    }

    public interface OnAlignChangedListener{
        void alignChanged(int index);
    }

    public void setOnAlignChangedListener(OnAlignChangedListener listener){
        onAlignChangedListener = listener;
    }

    private void initPopupWindow() {

        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_text_view_change_align, null, false);

        BottomPopupHelper helper = new BottomPopupHelper(view);
        alignWindow = helper.initPopWindow();

        radioButtonRight = view.findViewById(R.id.radio_btn_align_right);
        radioButtonLeft = view.findViewById(R.id.radio_btn_align_left);
        radioButtonCenter = view.findViewById(R.id.radio_btn_align_center);

        view.findViewById(R.id.btn_add_text_view_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignWindow.dismiss();
            }
        });

        radioGroup = view.findViewById(R.id.radio_group_add_text_view_detail);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_align_left:
                        currentIndex = 0;
                        break;
                    case R.id.radio_btn_align_center:
                        currentIndex = 1;
                        break;
                    case R.id.radio_btn_align_right:
                        currentIndex = 2;
                        break;
                }

            }
        });

        view.findViewById(R.id.btn_add_text_view_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAlignChangedListener!=null){
                    onAlignChangedListener.alignChanged(currentIndex);
                }
                alignWindow.dismiss();
            }
        });


        alignWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                radioGroup.clearCheck();
            }
        });

        //设置popupWindow里的按钮的事件

    }

    public void show() {
        alignWindow.setAnimationStyle(R.style.popup_window_anim_style);
        alignWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
