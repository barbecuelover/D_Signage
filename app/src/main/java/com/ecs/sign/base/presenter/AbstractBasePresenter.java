package com.ecs.sign.base.presenter;

import com.ecs.sign.base.BaseView;

/**
 * @author zw
 * @time 2019/11/21
 * @description
 */
public interface AbstractBasePresenter <T extends BaseView>{

    /**
     * 绑定View
     * @param view
     */
    void attachView(T view);

    /**
     * 解绑View
     */
    void detachView();

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    boolean isViewAttached();

    /**
     * 获取连接的view
     */
    T getView();
}
