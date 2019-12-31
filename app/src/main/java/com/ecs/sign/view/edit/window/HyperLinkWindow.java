package com.ecs.sign.view.edit.window;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.common.util.StringUtils;


public class HyperLinkWindow {

    private Activity activity;
    private PopupWindow linkWindow;
    private OnLinkSend linkSend;
    private View view;

    public HyperLinkWindow(Activity activity) {
        this.activity = activity;
        initPopWindow();
    }

    public interface OnLinkSend {
        void sendUrl(String url);
    }

    public void setOnCommentSend(OnLinkSend onLinkSend) {
        this.linkSend = onLinkSend;
    }


    public void show() {
        linkWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void initPopWindow() {
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_add_view_url, null, false);
        BottomPopupHelper bottomPopupWindow = new BottomPopupHelper(view);
        linkWindow =bottomPopupWindow.initPopWindow();


        Button btnCancel = view.findViewById(R.id.btn_set_time_cancel);
        Button btnSend = view.findViewById(R.id.btn_set_time_send);
        final EditText editTextContent = view.findViewById(R.id.editText_set_slider_time);
        editTextContent.setInputType(InputType.TYPE_NULL);//禁止弹出软键盘
        editTextContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager copy = (ClipboardManager)
                        activity.getSystemService(Context.CLIPBOARD_SERVICE);
                int index = editTextContent.getSelectionStart();//获取光标所在位置
                if (copy == null || TextUtils.isEmpty(copy.getText())) {

                } else {
                    ClipData clip = copy.getPrimaryClip();
                    //获取 text
                    String text = clip.getItemAt(0).getText().toString();
                    LogUtils.e("获取复制：" + text);
                    Editable edit = editTextContent.getEditableText();//获取EditText的文字
                    if (index < 0 || index >= edit.length()) {
                        edit.append(text);
                    } else {
                        edit.insert(index, text);//光标所在位置插入文字
                    }
                    editTextContent.setSelection(editTextContent.getText().length());
                }
                return false;
            }
        });

        //设置popupWindow里的按钮的事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkWindow.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linkContent = editTextContent.getText().toString();
                if (StringUtils.isUrl(linkContent)) {
                    editTextContent.setText("");
                    linkWindow.dismiss();
                    if (linkSend != null) {
                        linkSend.sendUrl(linkContent);
                    }
                } else {
                    ((BaseActivity) activity).showToast(activity.getResources().getString(R.string.enter_correct_url));
                }
            }
        });

    }

}
