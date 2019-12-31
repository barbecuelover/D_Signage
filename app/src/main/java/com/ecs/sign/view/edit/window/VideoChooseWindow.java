package com.ecs.sign.view.edit.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ecs.sign.R;


public class VideoChooseWindow {

    private Activity activity;
    private PopupWindow linkWindow;
    private OnLinkSendListener linkSend;
    private View view;

    TextView mTvPopTitle;
    RelativeLayout mRlTitleLayout;
    Button mBtnLocalVideo;
    Button mBtnNetUrl;
    LinearLayout mLlSelectLayout;

    public VideoChooseWindow(Activity activity) {
        this.activity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_video_select, null, false);
        BottomPopupHelper bottomPopWindow = new BottomPopupHelper(view);
        linkWindow = bottomPopWindow.initPopWindow();
        initView(view);
    }

    private void initView(View view) {
        this.mTvPopTitle = (TextView) view.findViewById(R.id.tv_pop_title);
        this.mRlTitleLayout = (RelativeLayout) view.findViewById(R.id.rl_title_layout);
        this.mBtnLocalVideo = (Button) view.findViewById(R.id.btn_select_video);
        this.mBtnNetUrl = (Button) view.findViewById(R.id.btn_video_url);
        this.mLlSelectLayout = (LinearLayout) view.findViewById(R.id.ll_select_layout);

        mBtnLocalVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkWindow != null) {
                    linkWindow.dismiss();
                }
                linkSend.onLocal();
            }
        });

        mBtnNetUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkWindow != null) {
                    linkWindow.dismiss();
                }
                HyperLinkWindow hyperLinkWindow = new HyperLinkWindow(activity);
                hyperLinkWindow.show();
                hyperLinkWindow.setOnCommentSend(new HyperLinkWindow.OnLinkSend() {
                    @Override
                    public void sendUrl(String url) {
                        linkSend.onNetUrl(url);
                    }
                });
            }
        });
    }

    public interface OnLinkSendListener {
        void onNetUrl(String url);
        void onLocal();
    }

    public void setOnLinkSendListener(OnLinkSendListener onLinkSend) {
        this.linkSend = onLinkSend;
    }

    public void show() {
        linkWindow.setAnimationStyle(R.style.popup_window_anim_style);
        linkWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
