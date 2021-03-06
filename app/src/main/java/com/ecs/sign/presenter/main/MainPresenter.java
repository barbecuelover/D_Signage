package com.ecs.sign.presenter.main;

import android.Manifest;

import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.common.util.DataKeeper;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.presenter.RxBasePresenter;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.IDaoHelper;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author zw
 * @time 2019/11/26
 * @description
 */
public class MainPresenter extends RxBasePresenter<MainContract.MainView> implements MainContract.MainActivityPresenter {


    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getTemplates() {

        dataManager.getAllTemplates(new IDaoHelper.TemplateCallBack() {
            @Override
            public void onResult(List<TemplateInfo> templateInfoList) {
                view.showTemplateList(templateInfoList);
                if (templateInfoList.size()>0){
                    view.showPlaceHolder(false);
                }else {
                    view.showPlaceHolder(true);
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.e(errorMsg);
                view.showToast(" DB query Error !");
            }
        });


    }

    @Override
    public void requestPermissionsAndSkip(RxPermissions rxPermissions, int  templateIndex) {
        Disposable disposable = rxPermissions.requestEachCombined(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA).subscribe(permission -> { // will emit 2 Permission objects
            if (permission.granted) {
                // `permission.name` is granted !
                view.skipToEditActivity(templateIndex);
            } else if (permission.shouldShowRequestPermissionRationale) {
                // Denied permission without ask never again
                view.showToast("Please check the permissions");
            } else {
                view.showToast("Please check the permissions");
            }
        });
        addEventSubscribe(disposable);
    }

    @Override
    public void renameTemplate(TemplateInfo templateInfo) {
        dataManager.renameTemplate(templateInfo, new CallBack() {
            @Override
            public void onSucceed() {
                view.refreshTemplateList();
                view.showToast("rename succeed ");
            }

            @Override
            public void onFailed() {
                view.showToast("rename failed ");
            }
        });
    }

    @Override
    public void deleteTemplate(TemplateInfo templateInfo) {
        //1.删除template对应的文件夹
        String tempDirPath = DataKeeper.getTemplatePath(templateInfo);
        File file = new File(tempDirPath);
        DataKeeper.deleteFile(file);
        //2.删除数据库
        dataManager.deleteTemplate(templateInfo, new CallBack() {
            @Override
            public void onSucceed() {
                view.showToast("delete succeed ");
                getTemplates();
            }

            @Override
            public void onFailed() {
                view.showToast("delete failed ");
            }
        });
    }


    @Override
    public void attachView(MainContract.MainView view) {
        super.attachView(view);
    }
}
