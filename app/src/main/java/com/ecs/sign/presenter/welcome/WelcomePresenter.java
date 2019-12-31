package com.ecs.sign.presenter.welcome;

import com.ecs.sign.model.DataManager;
import com.ecs.sign.base.presenter.RxBasePresenter;

/**
 * @author zw
 * @time 2019/11/22
 * @description
 */
public class WelcomePresenter extends RxBasePresenter<WelcomeContract.WelcomeView> implements WelcomeContract.WelcomeActivityPresenter {

    private DataManager dataManager;
    public WelcomePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(WelcomeContract.WelcomeView view) {
        super.attachView(view);
        view.playWelcomeVideoAndCountDown();
    }
}
