package com.ecs.sign.view.edit.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ecs.sign.R;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.view.edit.EditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * View 解析工具类
 * 使用先#init()宽高。以计算View缩放比例。
 */
public class ViewUtils {

    private final static String TAG = "zwcc";

    private static ViewUtils instance;

    private int videoViewNum;


    private List<View> mViewList = new ArrayList<>();

    private List<VideoView> mVideoViewList = new ArrayList();
    private View currentView;

    public ViewUtils() {
    }





    public void pause() {
        for (VideoView videoView : mVideoViewList) {
            videoView.pause();
        }
    }

    public void play() {
        for (VideoView videoView : mVideoViewList) {
            if (!videoView.isPlaying()) {
                LogUtils.d("video view="+videoView.getDuration());
                videoView.start();
            }
        }
    }

    public void destory() {
        for (VideoView videoView : mVideoViewList) {
            videoView.suspend();
        }
    }


    public interface OnAllVideoCompletedListener {
        /**
         * 播放界面中 所有的video view 播放完成后 回调此函数
         */
        void onCompleted();
    }


    /**
     * 将整个 播放界面解析到container
     *
     * @param context
     * @param canvasView
     * @param sliderInfo
     */
    public void parseSlider2Container(EditActivity context, ViewGroup canvasView, SliderInfo sliderInfo, OnAllVideoCompletedListener completedListener) {

        List<ViewInfo> viewList = sliderInfo.getViews();
        String bgUrl = sliderInfo.getBackgroundUrl();
        videoViewNum = 0;
        mVideoViewList.clear();

        //设置canvas 背景图片
        setCanvasBackground(context, canvasView, bgUrl);

        for (ViewInfo view : viewList) {
            parseViews2Layout(context, canvasView, view, completedListener);
        }

    }


    /**
     * 设置播放界面背景图片，如果不存在则设置成黑色
     *
     * @param context
     * @param canvasView
     * @param bgUrl
     */
    private void setCanvasBackground(Context context, final ViewGroup canvasView, String bgUrl) {
        LogUtils.d("background url = "+bgUrl);
        if (bgUrl != null) {
            Glide.with(context).load(bgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    canvasView.setBackground(resource);
                }
            });
        }
    }


    /**
     * 将 Json 解析出的具体ViewBean 转化成对应的View 并添加到 容器中。(单个 )
     * 目前只支持 TextView  ImageView VideoView
     *
     * @param context           上下文
     * @param container         放控件的容器
     * @param viewsBean         具体的控件
     * @param completedListener 如果是 视频，则设置视频播放完成的监听 以做跳转界面
     */
    public void parseViews2Layout(EditActivity context, ViewGroup container, ViewInfo viewsBean, OnAllVideoCompletedListener completedListener) {

        switch (viewsBean.getType()) {
            case Constant.VIEW_TEXT://TextView
                addTextView2Layout(context, container, viewsBean);
                break;
            case Constant.VIEW_IMAGE://ImageView
                addImageView2Layout(context, container, viewsBean);
                break;
            case Constant.VIEW_VIDEO:
                addVideoView2Layout(context, container, viewsBean, completedListener);
                break;
        }

    }


    private FrameLayout.LayoutParams getLayoutParams(ViewInfo viewsBean) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewsBean.getWidth(), viewsBean.getHeight());
        params.leftMargin = viewsBean.getLeft();
        params.topMargin = viewsBean.getTop();
        return params;
    }

    private void addTextView2Layout(EditActivity context, ViewGroup container,ViewInfo viewsBean) {
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
        container.addView(textView, params);
    }


    private void addImageView2Layout(EditActivity context, ViewGroup container, ViewInfo viewsBean) {
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
        container.addView(imageView, params);

    }


    private void addVideoView2Layout(EditActivity context, ViewGroup container, ViewInfo viewsBean, final OnAllVideoCompletedListener completedListener) {
        FrameLayout.LayoutParams params = getLayoutParams( viewsBean);
        VideoView videoView = new VideoView(context);
        videoView.setTag(R.id.view_info_tag, viewsBean);
        mVideoViewList.add(videoView);
        LogUtils.d("mVideoViewList="+mVideoViewList.size());
        videoViewNum += 1;
        if (viewsBean.isUrlIsLocal()) {
            videoView.setVideoURI(Uri.parse(viewsBean.getUrl()));
        } else {
            videoView.setVideoPath(viewsBean.getUrl());
        }


        container.addView(videoView, params);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                videoView.requestFocusFromTouch();
//                mp.start();
            }
        });

        videoView.pause();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoViewNum -= 1;
                if (videoViewNum == 0 && completedListener != null) {
                    //all video finished
                    Log.e(TAG, "onCompletion: 11111");
                    completedListener.onCompleted();
                }
            }
        });
    }


    private void setDragTouchListener(EditActivity context,View view2SetListener, String viewType) {
        mViewList.add(view2SetListener);
        
        DragTouchListener dragTouchListener = new DragTouchListener(view2SetListener);
        view2SetListener.setOnTouchListener(dragTouchListener);
        dragTouchListener.setClickListener(new DragTouchListener.ClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("onClick :"+viewType);
                showSelectedViewsShape(context,view2SetListener);
                context.showWidgetAttr(viewType);
            }
        });
    }

    /**
     * 清除所有View的边框,并给当前view增加选中边框
     */
    private void showSelectedViewsShape(Context context,View selectedView) {
        if (currentView!=null){
            currentView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        currentView = selectedView;
        currentView.setBackground(context.getResources().getDrawable(R.drawable.shape_view_selected, null));

    }




}
