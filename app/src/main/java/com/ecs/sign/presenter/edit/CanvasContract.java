package com.ecs.sign.presenter.edit;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.presenter.AbstractBasePresenter;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.view.edit.EditActivity;
import com.ecs.sign.view.edit.view.EditWindowHelper;
import com.ecs.sign.view.edit.view.ViewHelper;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public interface CanvasContract {

    interface CanvasView extends BaseView {

        void setCanvasBackground(String bgUrl);
        void setCanvasBackground(Uri bgUrl);

        void addViewToList(View viewAdd);
        void addVideoViewToList(VideoView videoView);
        void handleViewClickedListener(View v, String viewType);

        void changeImage(String url);
        void changeImage(Uri uri);

        void chooseVideoFromLocal();
        void chooseFromGallery(int type);

        void chooseFromCamera(int type);

        String getRealFilePath(Uri uri);


        void deleteView();
        void duplicateView();

    }

    interface CanvasFragmentPresenter extends AbstractBasePresenter<CanvasView> {

        void init(SliderInfo sliderInfo,ViewHelper viewHelper);

        void createNewView(int viewType,EditWindowHelper windowHelper,ViewHelper viewHelper);
        void changeViewAttr(int attrID,View currentView,EditWindowHelper windowHelper);

        void handleActivityResult(View currentView,int requestCode, @Nullable Intent data,ViewHelper viewHelper);

        void setPhotoPath(String path);
    }


}
