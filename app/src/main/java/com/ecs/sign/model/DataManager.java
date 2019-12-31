package com.ecs.sign.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.http.HttpHelperImp;
import com.ecs.sign.model.http.IHttpHelper;
import com.ecs.sign.model.room.DaoHelper;
import com.ecs.sign.model.room.IDaoHelper;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.model.sp.ISharedPreferenceHelper;
import com.ecs.sign.model.sp.SharedPreferenceHelperImp;
import com.ecs.sign.view.edit.bean.Widget;
import com.ecs.sign.view.edit.bean.WidgetHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zw
 * @time 2019/11/21
 * @description 数据管理类 Sp ,http ,dao
 */
public class DataManager implements IDaoHelper, IHttpHelper, ISharedPreferenceHelper {

    private DaoHelper daoHelper;
    private ISharedPreferenceHelper sharedPreferenceHelper;
    private IHttpHelper httpHelper;


    private static DataManager instance;

    private List<TemplateInfo> templateInfoList = new ArrayList<>();


    private DataManager(Context applicationContext) {
        this.daoHelper = DaoHelper.getInstance(applicationContext);
        this.sharedPreferenceHelper = new SharedPreferenceHelperImp();
        this.httpHelper = new HttpHelperImp();

    }

    public static DataManager getInstance(Context applicationContext) {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager(applicationContext);
                }
            }
        }
        return instance;
    }

    //TODO 有问题。↓ 下标越界
    public TemplateInfo getTemplate(int index){
        return templateInfoList.get(index);
    }


    //TODO 有问题。↑
    @Override
    public void updateTemplate(TemplateInfo info, CallBack callBack) {

        deleteTemplate(info, new CallBack() {
            @Override
            public void onSucceed() {
                insertTemplate(info,callBack);
            }

            @Override
            public void onFailed() {
                callBack.onFailed();
            }
        });
    }

    @Override
    public void insertTemplate(TemplateInfo info, CallBack callBack) {
        daoHelper.insertTemplate(info).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Long aLong) {
                insertSlidersAndViewsIntoTemplate(info,callBack);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFailed();
            }
        });
    }

    @Override
    public void deleteTemplate(TemplateInfo info, CallBack callBack) {
        daoHelper.deleteTemplate(info).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                if (templateInfoList.contains(info)){
                    templateInfoList.remove(info);
                }
                callBack.onSucceed();
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFailed();
            }
        });
    }

    @Override
    public void renameTemplate(TemplateInfo info, CallBack callBack) {
        daoHelper.updateTemplate(info).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Integer aLong) {
                callBack.onSucceed();
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFailed();
            }
        });
    }





    @Override
    public void getAllTemplates(TemplateCallBack templateCallBack) {

        daoHelper.getAllTemplates()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<TemplateInfo>, SingleSource<List<SliderInfo>>>() {
                    @Override
                    public SingleSource<List<SliderInfo>> apply(List<TemplateInfo> list) throws Exception {
                        templateInfoList.clear();
                        templateInfoList.addAll(list);

                        return daoHelper.getAllSliders();
                    }
                }).flatMap(new Function<List<SliderInfo>, SingleSource<List<ViewInfo>>>() {
            @Override
            public SingleSource<List<ViewInfo>> apply(List<SliderInfo> sliderInfoList) throws Exception {

                for (SliderInfo sliderInfo : sliderInfoList) {
//                    String previewUrl = sliderInfo.getPreviewUrl();
//                    if (previewUrl!=null){
//                        File file = new File( sliderInfo.getPreviewUrl());
//                        if (file.exists()){
//                            Bitmap bitmap =  BitmapFactory.decodeFile(file.toString());
//                            sliderInfo.setPreviewBitmap(bitmap);
//                        }
//                    }

                    for (TemplateInfo templateInfo : templateInfoList) {
                        if (templateInfo.getId() == sliderInfo.getTemplateId()) {
                            templateInfo.addSlider(sliderInfo);
                        }
                    }
                }

                return daoHelper.getAllViews();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ViewInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<ViewInfo> viewInfoList) {

                        LogUtils.d("viewInfoList .size== "+viewInfoList.size());
                        for (TemplateInfo templateInfo:templateInfoList){
                            List<SliderInfo> sliderInfoList = templateInfo.getSliderInfoList();
                            for (SliderInfo sliderInfo : sliderInfoList){
                                for (ViewInfo viewInfo :viewInfoList){
                                    if (sliderInfo.getId() == viewInfo.getSliderId())
                                    sliderInfo.addView(viewInfo);
                                }
                            }
                        }
                        templateCallBack.onResult(templateInfoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        templateCallBack.onError(e.getMessage());
                    }
                });


//        //先获取到所有Temp
//        Disposable disposableTemp = daoHelper.getAllTemplates()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<TemplateInfo>>() {
//                    @Override
//                    public void accept(final List<TemplateInfo> templateInfoList) throws Exception {
//                        //再获取到所有slider
//                        Log.d("zwcc", "daoUtil: templateInfoList  ="+templateInfoList.size());
//                        Disposable disposableSlider = daoHelper.getAllSliders()
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.io())
////                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<List<SliderInfo>>() {
//                                    @Override
//                                    public void accept(final List<SliderInfo> infoList) throws Exception {
//                                        //把slider 放到对应的Template的list集合中 。
//                                        Log.d("zwcc", "daoUtil: infoList  ="+infoList.size());
//
//                                        for (TemplateInfo templateInfo : templateInfoList) {
//                                            for (SliderInfo sliderInfo : infoList) {
//                                                if (sliderInfo.getTemplateId() == templateInfo.getId()) {
//                                                    templateInfo.addSlider(sliderInfo);
//                                                }
//                                            }
//                                        }
//                                        //最后获取到所有Views
//                                        Disposable disposableViews = daoHelper.getAllViews()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ViewInfo>>() {
//                                                    @Override
//                                                    public void accept(List<ViewInfo> viewInfoList) throws Exception {
//
//                                                        for (SliderInfo sliderInfo : infoList){
//                                                            for(ViewInfo viewInfo : viewInfoList){
//                                                                if (sliderInfo.getId()==viewInfo.getSliderId()){
//                                                                    sliderInfo.addView(viewInfo);
//                                                                }
//                                                            }
//                                                        }
//                                                        templateCallBack.onResult(templateInfoList);
//                                                    }
//                                                }, new Consumer<Throwable>() {
//                                                    @Override
//                                                    public void accept(Throwable throwable) throws Exception {
//                                                        templateCallBack.onError(throwable.getMessage());
//                                                    }
//                                                });
//
//                                    }
//                                });
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        templateCallBack.onError(throwable.getMessage());
//                    }
//                });




    }

    /**
     * Template 已经存在 template表中
     * @param templateInfo
     * @param callBack
     */
    public void insertSlidersAndViewsIntoTemplate(TemplateInfo templateInfo, final CallBack callBack){
        final List<SliderInfo> sliderInfoList = templateInfo.getSliderInfoList();
        daoHelper.insertSliderList(sliderInfoList)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long[] longs) {

                        List<ViewInfo> allViewInfoList = new ArrayList<>();
                        for (SliderInfo sliderInfo :sliderInfoList){
                            List<ViewInfo> viewInfoList = sliderInfo.getViews();
                            allViewInfoList.addAll(viewInfoList);
                        }
                        if (allViewInfoList.size()==0){
                            callBack.onSucceed();
                        }else {

                            daoHelper.insertViewList(allViewInfoList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long[]>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Long[] longs) {
                                    callBack.onSucceed();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    callBack.onFailed();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed();
                    }
                });
    }


}
