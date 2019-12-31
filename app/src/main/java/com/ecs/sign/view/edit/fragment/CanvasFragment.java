package com.ecs.sign.view.edit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.ecs.sign.view.edit.view.DragTouchListener;
import com.ecs.sign.view.edit.window.TextInputWindow;
import com.ecs.sign.view.edit.view.ViewHelper;
import com.ecs.sign.view.edit.view.EditWindowHelper;
import com.ecs.sign.view.edit.window.ImageChooseWindow;
import com.ecs.sign.view.edit.window.VideoChooseWindow;
import com.ecs.sign.view.edit.window.attr.ColorPickerWindow;
import com.ecs.sign.view.edit.window.attr.TextAlignWindow;
import com.ecs.sign.view.edit.window.attr.TextSizeWindow;
import com.ecs.sign.view.edit.window.attr.TextTypefaceWindow;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ecs.sign.base.common.util.DataKeeper.saveBitmap2JpgFile;
import static com.ecs.sign.base.common.util.StringUtils.getRealFilePath;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public class CanvasFragment extends BaseFragment<CanvasPresenter> implements CanvasContract.CanvasView {

    private static final String FILE_PATH = "com.ecs.signage.fileprovider";
    private static final int REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY = 100;
    private static final int REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA = 101;
    private static final int REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL = 200;

    private static final int REQUEST_CODE_SLIDER_BG_GALLERY = 300;
    private static final int REQUEST_CODE_SLIDER_BG_CAMERA = 301;


    @BindView(R.id.canvas_fragment_container)
    FrameLayout canvasView;

    private EditActivity editActivity;
    private int index;

    private SliderInfo sliderInfo;
    private ViewHelper viewHelper;
    private EditWindowHelper windowHelper;

    private String currentPhotoPath;
    private List<Uri> uriList;

    private View currentView;
    private List<View> viewList;
    private List<VideoView> videoViewList = new ArrayList();
    private String currentSliderPath;


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
        initSlider();
    }

    @Override
    protected void initEvent() {
        canvasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = canvasView;
                changeSelectedViewStatus();
                editActivity.resetCanvas();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(" Canvas Fragment onStop");
//        saveCanvasInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(" Canvas Fragment onDestroy");
        suspendVideoView();
    }


    private void initSlider() {
        sliderInfo = editActivity.templateInfo.getSliderInfoList().get(index);

        currentSliderPath  = DataKeeper.getSlidePath(sliderInfo);
        setCanvasBackground(sliderInfo.getBackgroundUrl());

        List<ViewInfo> viewInfoList = sliderInfo.getViews();
        for (ViewInfo viewInfo : viewInfoList) {
            View viewAdd = viewHelper.parseViews(viewInfo, new DragTouchListener.ClickListener() {
                @Override
                public void onClick(View v) {
                    handleViewClickedListener(v, viewInfo.getType());
                }
            });
            addViewToList(viewAdd);
            if (viewInfo.getType().equals(Constant.VIEW_VIDEO)) {
                videoViewList.add((VideoView) viewAdd);
            }
        }
    }

    /**
     * 点击了Widget Type，在画板上添加对应类型的新view ,由EditActivity 中的 widgetType RecyclerView 点击事件触发。
     *
     * @param viewType 对应viewType 种类的index
     */
    public void createNewView(int viewType) {
        LogUtils.d("CanvasFragment createNewView type=" + viewType);
        switch (viewType) {
            case Constant.ID_TYPE_TEXT:
                windowHelper.popupTextWindow("", new TextInputWindow.OnTextChangedListener() {
                    @Override
                    public void onTextChanged(String text) {
                        TextView textView = viewHelper.newTextView(text, sliderInfo.getId(), new DragTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view) {
                                handleViewClickedListener(view, Constant.VIEW_TEXT);
                            }
                        });
                        addViewToList(textView);
                    }
                });
                break;
            case Constant.ID_TYPE_IMAGE:
                windowHelper.popupImageChooseWindow(new ImageChooseWindow.OnLinkTypeSelectedListener() {
                    @Override
                    public void onNetUrl(String url) {
                        createNewImageView(url);
                    }

                    @Override
                    public void onGallery() {
                        chooseFromGallery(REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY);
                    }

                    @Override
                    public void onCamera() {
                        chooseFromCamera(REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA);
                    }
                });
                break;
            case Constant.ID_TYPE_VIDEO:
                windowHelper.popupVideoChooseWindow(new VideoChooseWindow.OnLinkSendListener() {
                    @Override
                    public void onNetUrl(String url) {
                        createNewVideoView(null, url);
                    }

                    @Override
                    public void onLocal() {
                        chooseVideoFromLocal();
                    }
                });
                break;
            case Constant.ID_TYPE_BACKGROUND:
                windowHelper.popupImageChooseWindow(new ImageChooseWindow.OnLinkTypeSelectedListener() {
                    @Override
                    public void onNetUrl(String url) {
                        setCanvasBackground(url);
                    }

                    @Override
                    public void onGallery() {
                        chooseFromGallery(REQUEST_CODE_SLIDER_BG_GALLERY);
                    }

                    @Override
                    public void onCamera() {
                        chooseFromCamera(REQUEST_CODE_SLIDER_BG_CAMERA);
                    }
                });
                break;
        }
    }

    /**
     * 改变当前view的属性。 比如TextView 的 textSize .textColor等，由EditActivity 中的 widgetAttr RecyclerView 点击事件触发。
     *
     * @param attrID
     */
    public void changeViewAttr(int attrID) {
        ViewInfo viewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);

        switch (attrID) {
            case Constant.ID_VIEW_DUPLICATE:
                duplicateView();
                break;
            case Constant.ID_VIEW_DELETE:
                deleteView();
                break;
            case Constant.ID_TEXT_TEXT:
                windowHelper.popupTextWindow(viewInfo.getTextText(), new TextInputWindow.OnTextChangedListener() {
                    @Override
                    public void onTextChanged(String text) {
                        if (viewInfo.getType().equals(Constant.VIEW_TEXT)) {
                            ((TextView) currentView).setText(text);
                            viewInfo.setTextText(text);
                        }
                    }
                });
                break;
            case Constant.ID_TEXT_SIZE:
                windowHelper.chooseTextSize(new TextSizeWindow.OnTextSizeChangedListener() {
                    @Override
                    public void onSizeChanged(float size) {
                        if (viewInfo.getType().equals(Constant.VIEW_TEXT)) {
                            ((TextView) currentView).setTextSize(size);
                            viewInfo.setTextSize(size);
                        }
                    }
                });
                break;
            case Constant.ID_TEXT_COLOR:
                windowHelper.chooseTextColor(viewInfo.getTextColor(), new ColorPickerWindow.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        if (viewInfo.getType().equals(Constant.VIEW_TEXT)) {
                            ((TextView) currentView).setTextColor(color);
                            viewInfo.setTextColor(color);
                        }
                    }
                });
                break;
            case Constant.ID_TEXT_ALIGN:
                windowHelper.chooseTextAlign(viewInfo.getTextGravity(), new TextAlignWindow.OnAlignChangedListener() {
                    @Override
                    public void alignChanged(int index) {
                        if (viewInfo.getType().equals(Constant.VIEW_TEXT)) {
                            switch (index) {
                                case 0:
                                    ((TextView) currentView).setGravity(Gravity.LEFT);
                                    break;
                                case 1:
                                    ((TextView) currentView).setGravity(Gravity.CENTER);
                                    break;
                                case 2:
                                    ((TextView) currentView).setGravity(Gravity.RIGHT);
                                    break;
                            }
                            viewInfo.setTextGravity(index);
                        }
                    }
                });
                break;
            case Constant.ID_TEXT_TYPEFACE:
                windowHelper.chooseTextTypeface(viewInfo.getTextTypeface(), new TextTypefaceWindow.OnTypefaceChangedListener() {
                    @Override
                    public void onTypefaceChanged(int index) {
                        if (viewInfo.getType().equals(Constant.VIEW_TEXT)) {
                            switch (index) {
                                case 0:
                                    ((TextView) currentView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    break;
                                case 1:
                                    ((TextView) currentView).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    break;
                                case 2:
                                    ((TextView) currentView).setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                                    break;
                            }
                            viewInfo.setTextTypeface(index);
                        }
                    }
                });
                break;
            case Constant.ID_IMG_IMG:

                windowHelper.popupImageChooseWindow(new ImageChooseWindow.OnLinkTypeSelectedListener() {
                    @Override
                    public void onNetUrl(String url) {
                        if (StringUtils.isUrl(url)){
                            Glide.with(editActivity).load(url).into((ImageView) currentView);
                            viewInfo.setUrl(url);
                        }
                    }
                    @Override
                    public void onGallery() {
                        chooseFromGallery(REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY);
                    }
                    @Override
                    public void onCamera() {
                        chooseFromCamera(REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA);
                    }
                });

                break;
            case Constant.ID_IMG_ALIGN:

                break;

            case Constant.ID_VIDEO_VIDEO:

                break;

            case Constant.ID_VIDEO_MUTE:

                break;


        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ViewInfo currentViewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);

            switch (requestCode) {
                case REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY: //点击相册 新建ImageView 或者替换当前ImageView的图片
                    uriList = Matisse.obtainResult(data);
                    if (currentViewInfo!=null&&currentViewInfo.getType().equals(Constant.VIEW_IMAGE)){
                        Glide.with(editActivity).load(uriList.get(0)).into((ImageView) currentView);
                        currentViewInfo.setUrl(getRealFilePath(editActivity, uriList.get(0)));
                    }else {
                        createNewImageView(getRealFilePath(editActivity, uriList.get(0)));
                    }
                    break;
                case REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA: // 点击相机 新建 ImageView 或者替换当前ImageView的图片
                    if (currentPhotoPath != null) {
                        if (currentViewInfo!=null&&currentViewInfo.getType().equals(Constant.VIEW_IMAGE)){
                            Glide.with(editActivity).load("file://" + currentPhotoPath).into((ImageView) currentView);
                            currentViewInfo.setUrl("file://" + currentPhotoPath);
                        }else {
                            createNewImageView("file://" + currentPhotoPath);
                        }
                    }
                    currentPhotoPath = null;
                    break;
                case REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL: //从本地 选择视频 新建VideoView
                    createNewVideoView(data, null);
                    break;
                case REQUEST_CODE_SLIDER_BG_GALLERY: //从图册 改变背景图片
                    uriList = Matisse.obtainResult(data);
                    setCanvasBackground(uriList.get(0));
                    sliderInfo.setBackgroundUrl(getRealFilePath(editActivity, uriList.get(0)));
                    break;
                case REQUEST_CODE_SLIDER_BG_CAMERA: //从相机拍照 改变背景图片
                    sliderInfo.setBackgroundUrl("file://" + currentPhotoPath);
                    setCanvasBackground("file://" + currentPhotoPath);
                    break;

            }
        }
    }


    @Override
    public void setCanvasBackground(String bgUrl) {
        LogUtils.d("background url = " + bgUrl);
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
     * @param uri URI
     */
    private void setCanvasBackground(Uri uri) {
        LogUtils.d("background url = " + uri);
        if (uri != null) {
            Glide.with(context).load(uri).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    canvasView.setBackground(resource);
                }
            });
        }
    }

    private void chooseVideoFromLocal() {
        Intent intent = new Intent();
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL);
    }

    private void chooseFromGallery(int type) {
        Matisse.from(CanvasFragment.this)
                .choose(MimeType.ofImage())
                .countable(true)
//                        .capture(true)//使用拍照功能
                .captureStrategy(new CaptureStrategy(true, FILE_PATH)) //是否拍照功能，并设置拍照后图片的保存路径
                .maxSelectable(1)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(false) // Default is `true`
                .forResult(type);
    }

    private void chooseFromCamera(int type) {
        MediaStoreCompat mediaStoreCompat = new MediaStoreCompat(editActivity, CanvasFragment.this);
        mediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.ecs.sign"));
        mediaStoreCompat.dispatchCaptureIntent(editActivity, type);
        //因为是指定Uri所以onActivityResult中的data为空 只能再这里获取拍照的路径
        currentPhotoPath = mediaStoreCompat.getCurrentPhotoPath();
    }

    private void addViewToList(View nView) {
        viewList.add(nView);
        canvasView.addView(nView);
    }


    /**
     * 创建新的VideoView
     *
     * @param data
     * @param videoUrl
     */
    private void createNewVideoView(Intent data, String videoUrl) {
        VideoView videoView = viewHelper.newVideoView(data, videoUrl, sliderInfo.getId(), new DragTouchListener.ClickListener() {
            @Override
            public void onClick(View view) {
                handleViewClickedListener(view, Constant.VIEW_VIDEO);
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.requestFocusFromTouch();
                mp.start();
                int duration = videoView.getDuration();
                LogUtils.e("当前视频时间：" + duration);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.e("播放完成了！");
            }
        });
        addViewToList(videoView);
        videoViewList.add(videoView);
    }


    /**
     * 创建新的ImageView 并增加拖动事件
     *
     * @param url
     */
    private void createNewImageView(String url) {
        ImageView imageView = viewHelper.newImageView(url, sliderInfo.getId(), new DragTouchListener.ClickListener() {
            @Override
            public void onClick(View view) {
                handleViewClickedListener(view, Constant.VIEW_IMAGE);
            }
        });
        addViewToList(imageView);
    }

    /**
     * 处理
     *
     * @param view
     * @param viewType
     */
    private void handleViewClickedListener(View view, String viewType) {
        currentView = view;
        changeSelectedViewStatus();
        editActivity.showWidgetAttr(viewType);
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
    private void duplicateView() {
    }

    /**
     * 移除当前选中的 view
     */
    private void deleteView() {
        viewList.remove(currentView);
        canvasView.removeView(currentView);
        editActivity.showWidgetType(false);

        ViewInfo currentViewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);
        //如果是 视频则 从视频列表移除
        if (currentViewInfo.getType().equals(Constant.VIEW_VIDEO)) {
            videoViewList.remove(currentView);
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
     */
    public void saveCanvasInfo() {
        //保存当前页的预览图片
        saveSliderPreviewImage();


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
            sliderInfo.addView(viewInfo);

            if (viewInfo.getType().equals(Constant.VIEW_VIDEO)) {
                int duration = ((VideoView) view).getDuration();
                if (duration > sliderInfo.getPlayTime()) {
                    sliderInfo.setPlayTime(duration);
                }
                LogUtils.e("当前视频长度：" + duration);
            }

        }
    }

    /**
     * 1. 保存 模板信息的时候 ，存不存到 指定目录下。
     * 2.1 如果存 ，删除view的时候 要检查 指定目录下存不存在对应资源。存在则删除。
     * 2.2 如果不存， 在导出模板的时候 在保存到指定目录下在 打包（传输完成在删除？），这样存不存在问题？
     *
     * 3. 删除模板的时候 ，对应文件夹删除。
     */

}
