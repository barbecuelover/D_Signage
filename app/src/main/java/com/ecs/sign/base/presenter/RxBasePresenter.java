package com.ecs.sign.base.presenter;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.DataManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zw
 * @time 2019/11/21
 * @description 基于Rx的Presenter封装,控制 事件订阅的生命周期
 */
public class RxBasePresenter<T extends BaseView> implements  AbstractBasePresenter<T>{

    protected T view;

    /**
     * 一个disposable的容器，可以容纳多个disposable 防止订阅之后没有取消订阅的内存泄漏
     */
    private CompositeDisposable compositeDisposable;
    public DataManager dataManager;

    public RxBasePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 将订阅事件 event 加入到 disposable的容器中
     * @param disposable
     */
    protected void addEventSubscribe(Disposable disposable){
        if (compositeDisposable == null ){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    @Override
    public void attachView(T view) {
        this.view = view;
        if (view!=null){
            LogUtils.d("attachView ="+view.getClass());
        }
    }

    @Override
    public void detachView() {
        this.view = null;
        if (compositeDisposable!=null){
            compositeDisposable.clear();
        }
    }

    @Override
    public boolean isViewAttached() {
        return view==null;
    }

    @Override
    public T getView() {
        return view;
    }
}
