package com.ecs.sign.presenter.edit;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.presenter.AbstractBasePresenter;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.socket.transport.SocketFileClientCallBack;
import com.ecs.sign.view.edit.bean.Widget;
import com.ecs.sign.view.edit.fragment.CanvasFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author zw
 * @time 2019/11/22
 * @description
 */
public interface EditContract {

    interface EditView extends BaseView {

        void initSliders(TemplateInfo templateInfo);

        void showWidgetAttr(String  viewType);
        void showWidgetType(boolean first);

        //改变模式 编辑,播放
        void changeToPlayModel(boolean play);

        void replaceCanvas(CanvasFragment fragment);

        void refreshSliderStatus();
    }

    interface EditActivityPresenter extends AbstractBasePresenter<EditView> {

        void updateTemplate(CallBack callBack);

        void changeBottomWidgetList(String viewType);

        void getTemplate(int index);

        void addNewSlider();

        void replaceCanvasAndResetStatus(int position);

        void addNewView2Canvas(int viewType);

        /**
         * 长按 slider list item 时 的监听。 增加 选中状态。弹出slider可操作的属性
         * @param slider
         */
        void changeSelectSliderStatus(int slider);

        /**
         * 重置 slider list 选中状态 ，和current 状态
         */
        void resetStatus();

        void changeCurrentWidgetAttr(int attrID);

        void transferTemplate(String ip, TemplateInfo templateInfo, SocketFileClientCallBack clientCallBack);

    }
}
