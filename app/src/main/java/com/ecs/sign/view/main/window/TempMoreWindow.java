package com.ecs.sign.view.main.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.view.edit.window.BottomPopupHelper;


public class TempMoreWindow implements View.OnClickListener {

    private PopupWindow morePopWindow;
    private View view;
    private Activity activity;
    private Button btnRename, btnDuplicate, btnEvent, btnDelete, btnCancel;

    private OnButtonClicked onButtonClicked;

    public TempMoreWindow(Activity context) {
        this.activity = context;
        init();
    }

    public void setOnButtonClicked(OnButtonClicked buttonClicked) {
        this.onButtonClicked = buttonClicked;
    }

    public interface OnButtonClicked {
        void onRename();

        void onDuplicate();

        void onEvent();

        void onDelete();
    }


    public void init() {
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_template_more_option, null, false);
        btnRename = view.findViewById(R.id.btn_temp_more_rename);
        btnDuplicate = view.findViewById(R.id.btn_temp_more_duplicate);
        btnEvent = view.findViewById(R.id.btn_temp_more_event);
        btnDelete = view.findViewById(R.id.btn_temp_more_delete);
        btnCancel = view.findViewById(R.id.btn_temp_more_cancel);

        btnRename.setOnClickListener(this);
        btnDuplicate.setOnClickListener(this);
        btnEvent.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        BottomPopupHelper helper = new BottomPopupHelper(view);
        morePopWindow =  helper.initPopWindow();



    }

    public void show() {
        morePopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_temp_more_rename:
                if (onButtonClicked != null) {
                    onButtonClicked.onRename();
                }
                morePopWindow.dismiss();
                break;
            case R.id.btn_temp_more_duplicate:
                if (onButtonClicked != null) {
                    onButtonClicked.onDuplicate();
                }
                morePopWindow.dismiss();
                break;
            case R.id.btn_temp_more_event:
                if (onButtonClicked != null) {
                    onButtonClicked.onEvent();
                }
                morePopWindow.dismiss();
                break;
            case R.id.btn_temp_more_delete:
                if (onButtonClicked != null) {
                    onButtonClicked.onDelete();
                }
                morePopWindow.dismiss();
                break;
            case R.id.btn_temp_more_cancel:
                morePopWindow.dismiss();
                break;
        }
    }
}
