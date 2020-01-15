package com.ecs.sign.view.edit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ecs.sign.R;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.DataKeeper;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.common.util.StringUtils;
import com.ecs.sign.base.fragment.BaseFragment;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.presenter.edit.CanvasContract;
import com.ecs.sign.presenter.edit.CanvasPresenter;
import com.ecs.sign.view.edit.EditActivity;
import com.ecs.sign.view.edit.view.EditWindowHelper;
import com.ecs.sign.view.edit.view.ViewHelper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ecs.sign.base.common.util.DataKeeper.saveBitmap2JpgFile;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public class CanvasFragment extends BaseFragment<CanvasPresenter> implements CanvasContract.CanvasView {


    @BindView(R.id.canvas_fragment_container)
    FrameLayout canvasView;

    private EditActivity editActivity;
    private int index;

    private SliderInfo sliderInfo;

    private ViewHelper viewHelper;
    private EditWindowHelper windowHelper;


    private String currentSliderPath;

    private View currentView;

    private List<View> viewList;
    private List<VideoView> videoViewList = new ArrayList();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_canvas;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initPresenter() {
        mPresenter = new CanvasPresenter(DataManager.getInstance(getActivity().getApplicationContext()));
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            index = getArguments().getInt(Constant.SLIDER_INDEX);
            LogUtils.d("CanvasFragment  Current index = " + index);
        }

        viewList = new ArrayList<>();
        currentView = canvasView;
        editActivity = (EditActivity) context;
        viewHelper = new ViewHelper(editActivity);
        windowHelper = new EditWindowHelper(editActivity);
        //初始化当前页 ，添加views 到canvas 上
        sliderInfo = editActivity.templateInfo.getSliderInfoList().get(index);
        currentSliderPath  = DataKeeper.getSlidePath(sliderInfo);

        mPresenter.init(sliderInfo,viewHelper);

        setCanvasBackground(sliderInfo.getBackgroundUrl());
    }

    @Override
    protected void initEvent() {
        canvasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = canvasView;
                changeSelectedViewStatus();
//                editActivity.resetCanvas();
                editActivity.showWidgetType(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(" Canvas Fragment onDestroy");
        suspendVideoView();
    }


    /**
     * 点击了Widget Type，在画板上添加对应类型的新view ,由EditActivity 中的 widgetType RecyclerView 点击事件触发。
     *
     * @param viewType 对应viewType 种类的index
     */
    public void createNewView(int viewType) {
        LogUtils.d("CanvasFragment createNewView type=" + viewType);
        mPresenter.createNewView(viewType,windowHelper,viewHelper);
    }

    /**
     * 改变当前view的属性。 比如TextView 的 textSize .textColor等，由EditActivity 中的 widgetAttr RecyclerView 点击事件触发。
     *
     * @param attrID
     */
    public void changeViewAttr(int attrID) {
        mPresenter.changeViewAttr(attrID,currentView,windowHelper);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.handleActivityResult(currentView,requestCode,data,viewHelper);
        }
    }


    @Override
    public void setCanvasBackground(String bgUrl) {
        LogUtils.d("background url = " + bgUrl);
        if (bgUrl != null) {
            Glide.with(context).load(bgUrl)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    canvasView.setBackground(resource);
                }
            });
        }
    }

    @Override
    public void setCanvasBackground(Uri uri) {
        LogUtils.d("background url = " + uri);
        if (uri != null) {
            Glide.with(context).load(uri)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    canvasView.setBackground(resource);
                }
            });
        }
    }
    @Override
    public void chooseVideoFromLocal() {
        Intent intent = new Intent();
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Constant.REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL);
    }

    @Override
    public void chooseFromGallery(int type) {
        Matisse.from(CanvasFragment.this)
                .choose(MimeType.ofImage())
                .countable(true)
//                        .capture(true)//使用拍照功能
                .captureStrategy(new CaptureStrategy(true, Constant.FILE_PATH)) //是否拍照功能，并设置拍照后图片的保存路径
                .maxSelectable(1)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(false) // Default is `true`
                .forResult(type);
    }

    @Override
    public void chooseFromCamera(int type) {
        MediaStoreCompat mediaStoreCompat = new MediaStoreCompat(editActivity, CanvasFragment.this);
        mediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.ecs.sign"));
        mediaStoreCompat.dispatchCaptureIntent(editActivity, type);
        //因为是指定Uri所以onActivityResult中的data为空 只能再这里获取拍照的路径
        mPresenter.setPhotoPath(mediaStoreCompat.getCurrentPhotoPath());
    }

    @Override
    public String getRealFilePath(Uri uri) {
        return StringUtils.getRealFilePath(editActivity,uri);
    }

    @Override
    public void addViewToList(View nView) {
        viewList.add(nView);
        canvasView.addView(nView);
    }

    @Override
    public void addVideoViewToList(VideoView videoView) {
        videoViewList.add(videoView);
    }


    /**
     * 处理
     *
     * @param view
     * @param viewType
     */
    @Override
    public void handleViewClickedListener(View view, String viewType) {
        currentView = view;
        changeSelectedViewStatus();
        editActivity.showWidgetAttr(viewType);
    }

    /**
     * 修改图片
     * @param url
     */
    @Override
    public void changeImage( String url) {
        Glide.with(editActivity).load(url).into((ImageView) currentView);
    }

    @Override
    public void changeImage(Uri uri) {
        Glide.with(editActivity).load(uri).into((ImageView) currentView);
    }


    /**
     * 更改选中view的背景
     */
    private void changeSelectedViewStatus() {
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            view.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        if (currentView != null && currentView != canvasView) {
            currentView.setBackground(editActivity.getResources().getDrawable(R.drawable.shape_view_selected, null));
        }
    }

    /**
     * 释放 VideoView资源
     */
    private void suspendVideoView() {
        for (VideoView videoView : videoViewList) {
            if (videoView != null) {
                videoView.suspend();
            }
        }
    }


    /**
     * 复制当前选中的view
     */
    @Override
    public void duplicateView() {
    }

    /**
     * 移除当前选中的 view
     */
    @Override
    public void deleteView() {
        viewList.remove(currentView);
        canvasView.removeView(currentView);
        editActivity.showWidgetType(false);

        ViewInfo currentViewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);
        //如果是 视频则 从视频列表移除
        if (currentViewInfo.getType().equals(Constant.VIEW_VIDEO)) {
            videoViewList.remove(currentView);
        }
        //如果 对应的Slider目录中 有本地资源文件，则删除
        if (currentViewInfo.isUrlIsLocal() && currentViewInfo.getUrl()!=null && currentViewInfo.getUrl().contains(currentSliderPath)){
            File file = new File(currentViewInfo.getUrl());
            DataKeeper.deleteFile(file);
        }

    }



    /**
     * 获取当前界面的图片，为PageList展示
     *
     * @return
     */
    private Bitmap saveSliderPreviewImage() {
        if (canvasView != null) {
            canvasView.destroyDrawingCache();
            canvasView.setDrawingCacheEnabled(true);
            canvasView.buildDrawingCache();
            Bitmap bitmap = canvasView.getDrawingCache();

            String url = saveBitmap2JpgFile(currentSliderPath, "preview_" + sliderInfo.getId(), bitmap);
            sliderInfo.setPreviewUrl(url);
            return bitmap;
            //指向的当前view的显示对象的缓存bm，如果view里控件增加等改变，bm相应改变
        } else {
            return null;
        }
    }


    /**
     * 切换CanvasFragment 或者离开编辑界面时 调用。
     *
     * //是否需要起工作线程 ？？
     */
    public void saveCanvasInfo() {
        //
        editActivity.templateInfo.setWidth(canvasView.getWidth());
        editActivity.templateInfo.setHeight(canvasView.getHeight());

        LogUtils.e("templateInfo width :" + editActivity.templateInfo.getWidth() );
        //1.保存当前页的预览图片
        saveSliderPreviewImage();
        //2.保存当前页背景图片
        String backgroundUrl = sliderInfo.getBackgroundUrl();
        if (backgroundUrl != null && !backgroundUrl.contains(currentSliderPath)) {
            String fileType = backgroundUrl.substring(backgroundUrl.lastIndexOf("."));
            String fileName = "background_" + sliderInfo.getId() + fileType;
            try {
                // /storage/emulated/0/signage/temp_1573716637070/slider_1573716637084/5002.jpg
                String finalSliderBgPath = DataKeeper.fileCopy(backgroundUrl, currentSliderPath, fileName);
                LogUtils.d("Slider background Path: " + finalSliderBgPath);
                sliderInfo.setBackgroundUrl(finalSliderBgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //3.保存View信息。并存储到对应的slider文件夹下 。
        sliderInfo.removeAllViews();
        LogUtils.d("saveCanvasInfo ="+viewList.size());
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            ViewInfo viewInfo = (ViewInfo) view.getTag(R.id.view_info_tag);
            viewInfo.setWidth(view.getWidth());
            viewInfo.setHeight(view.getHeight());
            viewInfo.setLeft(view.getLeft());
            viewInfo.setTop(view.getTop());
            viewInfo.setSliderId(sliderInfo.getId());


            if (viewInfo.getType().equals(Constant.VIEW_VIDEO)) {
                int duration = ((VideoView) view).getDuration();
                if (duration > sliderInfo.getPlayTime()) {
                    sliderInfo.setPlayTime(duration);
                }
                LogUtils.e("当前视频长度：" + duration);
            }

            //4.如果是本地资源 则保存到 对应的文件目录 下， 网络资源 无需操作。
            //判断是否是原文件，如果图片没变，则不操作。
            if (viewInfo.isUrlIsLocal() && viewInfo.getUrl() != null) {
                String initialFilePath = viewInfo.getUrl();
                if (!initialFilePath.contains(currentSliderPath)) {
                    //copy file to  the currentSliderPath and rename
                    //2.获取文件类型。
                    String fileType = initialFilePath.substring(initialFilePath.lastIndexOf("."));
                    String fileName = "view_" + viewInfo.getRealId() + fileType;
                    try {
                        // /storage/emulated/0/signage/temp_1573716637070/slider_1573716637084/5002.jpg
                        String finalPath = DataKeeper.fileCopy(initialFilePath, currentSliderPath, fileName);
                        LogUtils.d("finalPath: " + finalPath);
                        viewInfo.setUrl(finalPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            sliderInfo.addView(viewInfo);
        }
    }

}
