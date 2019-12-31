package com.ecs.sign.view.main.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.view.edit.window.BottomPopupHelper;


public class RenameWindow {

    private Activity activity;
    private PopupWindow nameWindow;
    private OnNameChanged onNameChanged;
    private View view;
    private EditText editTextContent;
    public RenameWindow(Activity activity) {
        this.activity = activity;
        initPopWindow();
    }

    public interface OnNameChanged {
        void onNameChanged(String name);
    }

    public void setOnNameChanged(OnNameChanged onNameChanged){
        this.onNameChanged = onNameChanged;
    }


    public void  show(){
        nameWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }

    private void initPopWindow() {
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_info_option_rename, null, false);

        Button btnCancel =  view.findViewById(R.id.btn_rename_cancel);
        Button btnOK = view.findViewById(R.id.btn_rename_ok);
        editTextContent = view.findViewById(R.id.editText_template_rename);


        BottomPopupHelper helper = new BottomPopupHelper(view);
        nameWindow =  helper.initPopWindow();

        //设置popupWindow里的按钮的事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameWindow.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rename = editTextContent.getText().toString();

                if (rename==null || rename.length()<2){
                    ((BaseActivity)activity).showToast(activity.getResources().getString(R.string.rename_tips));
                }else {
                    editTextContent.setText("");
                    nameWindow.dismiss();
                    if (onNameChanged !=null){
                        onNameChanged.onNameChanged(rename);
                    }
                }

            }
        });

    }




}
