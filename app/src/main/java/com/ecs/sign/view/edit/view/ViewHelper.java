package com.ecs.sign.view.edit.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.ecs.sign.R;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.view.edit.EditActivity;

import static com.ecs.sign.base.common.util.UrlParseUtils.getPath;

/**
 * View 解析工具类
 * 使用先#init()宽高。以计算View缩放比例。
 */
public class ViewHelper {

    Activity context;


    public ViewHelper(Activity activity) {
        this.context = activity;
    }

    /**
     * 将 Json 解析出的具体ViewBean 转化成对应的View 并添加到 容器中。(单个 )
     * 目前只支持 TextView  ImageView VideoView
     *
     * @param viewsBean         具体的控件
     */
    public View parseViews(ViewInfo viewsBean,DragTouchListener.ClickListener clickListener) {

        switch (viewsBean.getType()) {
            case Constant.VIEW_TEXT://TextView
                return  createTextView( viewsBean,clickListener);
            case Constant.VIEW_IMAGE://ImageView
                return  createImageView(viewsBean,clickListener);
            case Constant.VIEW_VIDEO:
                return  createVideoView(viewsBean,clickListener);
        }

        return  new View(context);

    }


    private FrameLayout.LayoutParams getLayoutParams(ViewInfo viewsBean) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewsBean.getWidth(), viewsBean.getHeight());
        params.leftMargin = viewsBean.getLeft();
        params.topMargin = viewsBean.getTop();
        return params;
    }

    private TextView createTextView( ViewInfo viewsBean,DragTouchListener.ClickListener clickListener) {
        FrameLayout.LayoutParams params = getLayoutParams( viewsBean);
        TextView textView = new TextView(context);
        textView.setTag(R.id.view_info_tag, viewsBean);

        textView.setText(viewsBean.getTextText());

        if (viewsBean.getTextColor() != 0) {
            textView.setTextColor(viewsBean.getTextColor());
        }
        if (viewsBean.getTextSize() != 0) {
            textView.setTextSize((float) viewsBean.getTextSize());
        }
        switch (viewsBean.getTextTypeface()) {
            case 0:
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case 1:
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                break;
        }

        switch (viewsBean.getTextGravity()) {
            case 0:
                textView.setGravity(Gravity.LEFT);
                break;
            case 1:
                textView.setGravity(Gravity.CENTER);
                break;
            case 2:
                textView.setGravity(Gravity.RIGHT);
                break;
        }
        textView.setLayoutParams(params);
        setDragTouchListener(textView,clickListener);
        return  textView;
    }


    private ImageView createImageView(ViewInfo viewsBean,DragTouchListener.ClickListener clickListener) {
        FrameLayout.LayoutParams params = getLayoutParams( viewsBean);
        ImageView imageView = new ImageView(context);
        imageView.setTag(R.id.view_info_tag, viewsBean);
        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        switch (viewsBean.getImgScaleType()) {
            case 0:
                scaleType = ImageView.ScaleType.CENTER;
                break;
            case 1:
                scaleType = ImageView.ScaleType.CENTER_CROP;
                break;
            case 2:
                scaleType = ImageView.ScaleType.CENTER_INSIDE;
                break;
            case 3:
                scaleType = ImageView.ScaleType.FIT_CENTER;
                break;
            case 4:
                scaleType = ImageView.ScaleType.FIT_XY;
                break;
        }

        imageView.setScaleType(scaleType);
        imageView.setPadding(2, 2, 2, 2);
        imageView.setBackgroundColor(Color.TRANSPARENT);

        Glide.with(context).load(viewsBean.getUrl()).into(imageView);
        imageView.setLayoutParams(params);
        setDragTouchListener(imageView,clickListener);
        return  imageView;

    }


    private VideoView createVideoView(ViewInfo viewsBean,DragTouchListener.ClickListener clickListener) {
        FrameLayout.LayoutParams params = getLayoutParams( viewsBean);
        VideoView videoView = new VideoView(context);
        videoView.setTag(R.id.view_info_tag, viewsBean);

        if (viewsBean.isUrlIsLocal()) {
            videoView.setVideoURI(Uri.parse(viewsBean.getUrl()));
        } else {
            videoView.setVideoPath(viewsBean.getUrl());
        }

        videoView.setLayoutParams(params);
        setDragTouchListener(videoView,clickListener);



        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.requestFocusFromTouch();
                mp.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.e("播放完成了！");
            }
        });
        return videoView;

    }

    private void setDragTouchListener(View view2SetListener,DragTouchListener.ClickListener clickListener) {
        DragTouchListener dragTouchListener = new DragTouchListener(view2SetListener);
        view2SetListener.setOnTouchListener(dragTouchListener);
        dragTouchListener.setClickListener(clickListener);
    }



    public TextView newTextView(String text,long sliderID,DragTouchListener.ClickListener clickListener){
        TextView textView = new TextView(context);

        ViewInfo viewInfoNew = new ViewInfo(System.currentTimeMillis(),sliderID,Constant.VIEW_TEXT);
        viewInfoNew.setTextText(text);

        textView.setTag(R.id.view_info_tag, viewInfoNew);
        textView.setPadding(2, 2, 2, 2);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setText(text);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 120);
        params.leftMargin = 80;
        params.topMargin = 60;
        textView.setLayoutParams(params);
        setDragTouchListener(textView, clickListener);
        return  textView;
    }


    /***
     * 增加ImageView
     * @param otherUrl
     */
    public ImageView newImageView(String otherUrl, long sliderID, DragTouchListener.ClickListener clickListener) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(380, 200);
        params.leftMargin = 80;
        params.topMargin = 60;

        ViewInfo viewInfo = new ViewInfo(System.currentTimeMillis(),sliderID,Constant.VIEW_IMAGE);
        viewInfo.setImgScaleType(1);
        viewInfo.setUrl(otherUrl);

        ImageView imageView = new ImageView(context);
        imageView.setTag(R.id.view_info_tag, viewInfo);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setPadding(2, 2, 2, 2);
        imageView.setBackgroundColor(Color.TRANSPARENT);

        imageView.setLayoutParams(params);

        Glide.with(context).load(otherUrl).into(imageView);

        setDragTouchListener(imageView,clickListener);
        return imageView;

    }


    public VideoView newVideoView(Intent data, String videoUrl,long sliderID,DragTouchListener.ClickListener clickListener) {

        ViewInfo viewInfo = new ViewInfo(System.currentTimeMillis(),sliderID,Constant.VIEW_VIDEO);
        VideoView videoView = new VideoView(context);
        videoView.setTag(R.id.view_info_tag, viewInfo);

        if (null != data) {
            Uri uri = data.getData();
            videoView.setVideoURI(uri);
            try {
                viewInfo.setUrl(getPath(context, uri));
            } catch (Exception e) {
                viewInfo.setUrl(uri.toString());
                e.printStackTrace();
            }
            viewInfo.setUrlIsLocal(true);
        } else {
            videoView.setVideoPath(videoUrl);
            viewInfo.setUrl(videoUrl);
            viewInfo.setUrlIsLocal(false);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 600);
        params.leftMargin = 80;
        params.topMargin = 60;

        videoView.setLayoutParams(params);
        setDragTouchListener(videoView,clickListener);

        return  videoView;
    }



}
