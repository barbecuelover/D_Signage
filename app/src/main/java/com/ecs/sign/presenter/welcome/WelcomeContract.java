package com.ecs.sign.presenter.welcome;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.presenter.AbstractBasePresenter;

/**
 * @author zw
 * @time 2019/11/22
 * @description
 */
public interface WelcomeContract {

    interface WelcomeView extends BaseView{
        void playWelcomeVideoAndCountDown();
    }

    interface WelcomeActivityPresenter extends AbstractBasePresenter<WelcomeView>{

    }

}
