package com.ecs.sign.view.edit.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.util.StringUtils;


/**
 *
 *                 TextAlignWindow textAlignWindow = new TextAlignWindow(activity);
 *                 textAlignWindow.setAlign(2);
 *                 textAlignWindow.setOnAlignChangedListener(new TextAlignWindow.OnTextChangedListener() {
 *                     @Override
 *                     public void onTypefaceChanged(int index) {
 *                         showToast("onTypefaceChanged :"+index);
 *                     }
 *                 });
 *                 textAlignWindow.showDeviceDialog();
 *
 *
 *
 */
public class TextInputWindow {

    private Activity activity;
    private PopupWindow inputWindow;
    private View view;
    private EditText editText;
    private OnTextChangedListener onTextChangedListener;

    public void setText(String text) {
        editText.setText(text);
    }

    public TextInputWindow(Activity activity) {
        this.activity = activity;
        initPopupWindow();
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }

    public void setOnTextChangedListener(OnTextChangedListener listener){
        onTextChangedListener = listener;
    }

    private void initPopupWindow() {

        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_text_view_add_text, null, false);

        BottomPopupHelper bottomPopupWindow = new BottomPopupHelper(view);
        inputWindow = bottomPopupWindow.initPopWindow();

        editText = view.findViewById(R.id.editText_add_textview_text_content);

        view.findViewById(R.id.btn_add_text_view_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWindow.dismiss();
            }
        });

        view.findViewById(R.id.btn_add_text_view_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textStr =editText.getText().toString();
                if (StringUtils.isEmpty(textStr)){
                    ((BaseActivity)activity).showToast("Please Input text");
                }else {
                    if (onTextChangedListener!=null){
                        onTextChangedListener.onTextChanged(textStr);
                    }
                    editText.setText("");
                    inputWindow.dismiss();
                }
            }
        });

        inputWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editText.setText("");
            }
        });

    }

    public void show() {
        inputWindow.setAnimationStyle(R.style.popup_window_anim_style);
        inputWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
