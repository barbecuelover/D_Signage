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


public class CommentWindow {

    private Activity activity;
    private PopupWindow commentPopWindow;
    private OnCommentSend commentSend;
    private View view;

    public CommentWindow(Activity activity) {
        this.activity = activity;
        initPopWindow();
    }

    public interface OnCommentSend{
        void SendMsg(String msg);
    }

    public void setOnCommentSend(OnCommentSend onCommentSend){
        this.commentSend = onCommentSend;
    }


    public void  show(){
        commentPopWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }

    private void initPopWindow() {
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_info_option_comment, null, false);

        Button btnCancel =  view.findViewById(R.id.btn_comment_cancel);
        Button btnSend = view.findViewById(R.id.btn_comment_send);
        final EditText editTextContent = view.findViewById(R.id.editText_comment_content);

        BottomPopupHelper helper = new BottomPopupHelper(view);
        commentPopWindow =  helper.initPopWindow();

        //设置popupWindow里的按钮的事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopWindow.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = editTextContent.getText().toString();

                if (commentContent==null || commentContent.length()<10){
                    ((BaseActivity)activity).showToast(activity.getResources().getString(R.string.comment_tips));
                }else {
                    editTextContent.setText("");
                    commentPopWindow.dismiss();
                    if (commentSend!=null){
                        commentSend.SendMsg(commentContent);
                    }
                }

            }
        });

    }




}
