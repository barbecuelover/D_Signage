package com.ecs.sign.presenter.edit;

import android.view.View;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.presenter.AbstractBasePresenter;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.view.edit.EditActivity;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public interface CanvasContract {

    interface CanvasView extends BaseView {

        void setCanvasBackground(String bgUrl);
    }

    interface CanvasFragmentPresenter extends AbstractBasePresenter<CanvasView> {

        void initSlider(EditActivity activity,int sliderIndex);

        void createNewView(int viewType);

        void createNewTextView(String text, BaseActivity activity);
    }


}
