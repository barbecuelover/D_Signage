package com.ecs.sign.view.main.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.view.edit.window.BottomPopupHelper;


public class ProjectInfoWindow implements View.OnClickListener {

    private Activity activity;
    private PopupWindow popWindow;
    private OnButtonClicked buttonClicked;
    private  View view;

    public ProjectInfoWindow(Activity activity) {
        this.activity = activity;
        initPopWindow();
    }


    public void  setButtonClicked(OnButtonClicked onButtonClicked){
        buttonClicked = onButtonClicked;
    }

    public interface OnButtonClicked {
        void onComment();
        void onSupport();
        void onLicence();
    }

    public void show(){

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

    }

    private void initPopWindow() {
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_info_option, null, false);

        Button btnComment = view.findViewById(R.id.btn_info_leave_comment);
        Button btnSupport = view.findViewById(R.id.btn_info_support);
        Button btnLicence = view.findViewById(R.id.btn_info_licence);
        Button btnCancel = view.findViewById(R.id.btn_info_cancel);
        //设置popupWindow里的按钮的事件
        btnComment.setOnClickListener(this);
        btnSupport.setOnClickListener(this);
        btnLicence.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        BottomPopupHelper helper = new BottomPopupHelper(view);
        popWindow =  helper.initPopWindow();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_info_leave_comment:
                if (buttonClicked != null) {
                    buttonClicked.onComment();
                }
                popWindow.dismiss();
                break;
            case R.id.btn_info_licence:
                if (buttonClicked != null) {
                    buttonClicked.onLicence();
                }
                popWindow.dismiss();
                break;
            case R.id.btn_info_support:
                if (buttonClicked != null) {
                    buttonClicked.onSupport();
                }
                popWindow.dismiss();
                break;
            case R.id.btn_info_cancel:
                popWindow.dismiss();
                break;

        }
    }
}
