package com.ecs.sign.presenter.main;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.presenter.AbstractBasePresenter;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author zw
 * @time 2019/11/22
 * @description
 */
public interface MainContract {

    interface MainView extends BaseView {

        void showPlaceHolder(boolean visible);

        void showTemplateList(List<TemplateInfo> list);

        void refreshTemplateList();

        void skipToEditActivity(int sliderIndex);

    }

    interface MainActivityPresenter extends AbstractBasePresenter<MainView> {
        void getTemplates();
        void requestPermissionsAndSkip(RxPermissions rxPermissions, int templateIndex);
        void renameTemplate(TemplateInfo templateInfo);
        void deleteTemplate(TemplateInfo templateInfo);
    }
}
