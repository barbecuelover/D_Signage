package com.ecs.sign.view.edit.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ecs.sign.R;


public class ImageChooseWindow {

    private Activity activity;
    private PopupWindow linkWindow;
    private OnLinkTypeSelectedListener linkTypeSelectedListener;
    private View view;

    private Button mBtnSelectAlbum;
    private Button mBtnSelectCamera;
    private Button mBtnImgUrl;

    public ImageChooseWindow(Activity activity) {
        this.activity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_image_select, null, false);
        BottomPopupHelper bottomPopupWindow = new BottomPopupHelper(view);
        linkWindow = bottomPopupWindow.initPopWindow();
        initView(view);
    }

    private void initView(View view) {

        this.mBtnSelectAlbum = view.findViewById(R.id.btn_select_album);
        this.mBtnSelectCamera =  view.findViewById(R.id.btn_select_camera);
        this.mBtnImgUrl =  view.findViewById(R.id.btn_img_url);

        mBtnSelectAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkWindow != null) {
                    linkWindow.dismiss();
                }
                linkTypeSelectedListener.onGallery();
            }
        });

        mBtnSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkWindow != null) {
                    linkWindow.dismiss();
                }
                linkTypeSelectedListener.onCamera();
            }
        });

        mBtnImgUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkWindow != null) {
                    linkWindow.dismiss();
                }
                HyperLinkWindow hyperLinkWindow = new HyperLinkWindow(activity);
                hyperLinkWindow.show();
                hyperLinkWindow.setOnCommentSend(new HyperLinkWindow.OnLinkSend() {
                    public void sendUrl(String url) {
                        linkTypeSelectedListener.onNetUrl(url);
                    }
                });
            }
        });
    }

    public interface OnLinkTypeSelectedListener {
        void onNetUrl(String url);
        void onGallery();
        void onCamera();
    }

    public void setOnLinkTypeSelectedListener(OnLinkTypeSelectedListener listener) {
        this.linkTypeSelectedListener = listener;
    }

    public void show() {
        linkWindow.setAnimationStyle(R.style.popup_window_anim_style);
        linkWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
