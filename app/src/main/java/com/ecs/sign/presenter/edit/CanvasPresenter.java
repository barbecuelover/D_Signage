package com.ecs.sign.presenter.edit;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.ecs.sign.R;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.common.util.StringUtils;
import com.ecs.sign.base.presenter.RxBasePresenter;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.view.edit.view.DragTouchListener;
import com.ecs.sign.view.edit.view.EditWindowHelper;
import com.ecs.sign.view.edit.view.ViewHelper;
import com.ecs.sign.view.edit.window.ImageChooseWindow;
import com.ecs.sign.view.edit.window.TextInputWindow;
import com.ecs.sign.view.edit.window.VideoChooseWindow;
import com.ecs.sign.view.edit.window.attr.ColorPickerWindow;
import com.ecs.sign.view.edit.window.attr.TextAlignWindow;
import com.ecs.sign.view.edit.window.attr.TextSizeWindow;
import com.ecs.sign.view.edit.window.attr.TextTypefaceWindow;
import com.zhihu.matisse.Matisse;

import java.util.List;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public class CanvasPresenter extends RxBasePresenter<CanvasContract.CanvasView> implements CanvasContract.CanvasFragmentPresenter{

    private  SliderInfo sliderInfo;
    private String currentPhotoPath;
    private boolean isChanged =false;

    public CanvasPresenter(DataManager dataManager) {
        super(dataManager);
    }



    @Override
    public void init(SliderInfo slider,ViewHelper viewHelper) {

        this.sliderInfo =slider;

        List<ViewInfo> viewInfoList = sliderInfo.getViews();
        for (ViewInfo viewInfo : viewInfoList) {
            View viewAdd = viewHelper.parseViews(viewInfo, new DragTouchListener.ClickListener() {
                @Override
                public void onClick(View v) {
                    view.handleViewClickedListener(v, viewInfo.getType());
                }
            });
            view.addViewToList(viewAdd);
            if (viewInfo.getType().equals(Constant.VIEW_VIDEO)) {
                view.addVideoViewToList((VideoView) viewAdd);
            }
        }

    }

    @Override
    public void createNewView(int viewType,EditWindowHelper windowHelper,ViewHelper viewHelper) {

        LogUtils.d("CanvasFragment createNewView type=" + viewType);
        switch (viewType) {
            case Constant.ID_TYPE_TEXT:
                windowHelper.popupTextWindow("", new TextInputWindow.OnTextChangedListener() {
                    @Override
                    public void onTextChanged(String text) {
                        TextView textView = viewHelper.newTextView(text, sliderInfo.getId(), new DragTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view) {
                                CanvasPresenter.this.view.handleViewClickedListener(view, Constant.VIEW_TEXT);
                            }
                        });
                        view.addViewToList(textView);
                    }
                });
                break;
            case Constant.ID_TYPE_IMAGE:
                windowHelper.popupImageChooseWindow(new ImageChooseWindow.OnLinkTypeSelectedListener() {
                    @Override
                    public void onNetUrl(String url) {
                        createNewImageView(url,viewHelper);
                    }

                    @Override
                    public void onGallery() {
                        view.chooseFromGallery(Constant.REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY);
                    }

                    @Override
                    public void onCamera() {
                        view.chooseFromCamera(Constant.REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA);
                    }
                });
                break;
            case Constant.ID_TYPE_VIDEO:
                windowHelper.popupVideoChooseWindow(new VideoChooseWindow.OnLinkSendListener() {
                    @Override
                    public void onNetUrl(String url) {
                        createNewVideoView(null, url,viewHelper);
                    }

                    @Override
                    public void onLocal() {
                        view.chooseVideoFromLocal();
                    }
                });
                break;
            case Constant.ID_TYPE_BACKGROUND:
                windowHelper.popupImageChooseWindow(new ImageChooseWindow.OnLinkTypeSelectedListener() {
                    @Override
                    public void onNetUrl(String url) {
                        view.setCanvasBackground(url);
                    }

                    @Override
                    public void onGallery() {
                        view.chooseFromGallery(Constant.REQUEST_CODE_SLIDER_BG_GALLERY);
                    }

                    @Override
                    public void onCamera() {
                        view.chooseFromCamera(Constant.REQUEST_CODE_SLIDER_BG_CAMERA);
                    }
                });
                break;
        }


    }

    @Override
    public void changeViewAttr(int attrID,View currentView,EditWindowHelper windowHelper ) {
        ViewInfo viewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);
        switch (attrID) {
            case Constant.ID_VIEW_DUPLICATE:
                view.duplicateView();
                break;
            case Constant.ID_VIEW_DELETE:
                view.deleteView();
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
                            view.changeImage(url);
                            viewInfo.setUrl(url);
                        }
                    }
                    @Override
                    public void onGallery() {
                        view.chooseFromGallery(Constant.REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY);
                    }
                    @Override
                    public void onCamera() {
                        view.chooseFromCamera(Constant.REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA);
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
    public void handleActivityResult(View currentView,int requestCode, @Nullable Intent data,ViewHelper viewHelper) {
        List<Uri> uriList;
        ViewInfo currentViewInfo = (ViewInfo) currentView.getTag(R.id.view_info_tag);

        switch (requestCode) {
            case Constant.REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY: //点击相册 新建ImageView 或者替换当前ImageView的图片
                uriList = Matisse.obtainResult(data);
                if (currentViewInfo!=null&&currentViewInfo.getType().equals(Constant.VIEW_IMAGE)){
                    view.changeImage((uriList.get(0)));
                    currentViewInfo.setUrl( view.getRealFilePath( uriList.get(0)));
                }else {
                    createNewImageView(view.getRealFilePath(uriList.get(0)),viewHelper);
                }
                break;
            case Constant.REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA: // 点击相机 新建 ImageView 或者替换当前ImageView的图片
                if (currentPhotoPath != null) {
                    if (currentViewInfo!=null&&currentViewInfo.getType().equals(Constant.VIEW_IMAGE)){
                        view.changeImage("file://" + currentPhotoPath);
//                        Glide.with(editActivity).load().into((ImageView) currentView);
                        currentViewInfo.setUrl("file://" + currentPhotoPath);
                    }else {
                        createNewImageView("file://" + currentPhotoPath,viewHelper);
                    }
                }
                currentPhotoPath = null;
                break;
            case Constant.REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL: //从本地 选择视频 新建VideoView
                createNewVideoView(data, null,viewHelper);
                break;
            case Constant.REQUEST_CODE_SLIDER_BG_GALLERY: //从图册 改变背景图片
                uriList = Matisse.obtainResult(data);
                view.setCanvasBackground(uriList.get(0));
                sliderInfo.setBackgroundUrl(view.getRealFilePath(uriList.get(0)));
                break;
            case Constant.REQUEST_CODE_SLIDER_BG_CAMERA: //从相机拍照 改变背景图片
                sliderInfo.setBackgroundUrl("file://" + currentPhotoPath);
                view.setCanvasBackground("file://" + currentPhotoPath);
                break;

        }

    }

    @Override
    public void setPhotoPath(String path) {
        currentPhotoPath = path;
    }


    /**
     * 创建新的VideoView
     *
     * @param data
     * @param videoUrl
     */
    private void createNewVideoView(Intent data, String videoUrl ,ViewHelper viewHelper) {
        VideoView videoView = viewHelper.newVideoView(data, videoUrl, sliderInfo.getId(), new DragTouchListener.ClickListener() {
            @Override
            public void onClick(View view) {
                CanvasPresenter.this.view.handleViewClickedListener(view, Constant.VIEW_VIDEO);
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
        view.addViewToList(videoView);
        view.addVideoViewToList(videoView);
    }


    /**
     * 创建新的ImageView 并增加拖动事件
     *
     * @param url
     */
    private void createNewImageView(String url,ViewHelper viewHelper) {
        ImageView imageView = viewHelper.newImageView(url, sliderInfo.getId(), new DragTouchListener.ClickListener() {
            @Override
            public void onClick(View view) {
                CanvasPresenter.this.view.handleViewClickedListener(view, Constant.VIEW_IMAGE);
            }
        });
        view.addViewToList(imageView);
    }

}
