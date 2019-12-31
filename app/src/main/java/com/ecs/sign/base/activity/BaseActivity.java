package com.ecs.sign.base.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecs.sign.R;
import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.presenter.AbstractBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity<T extends AbstractBasePresenter> extends AppCompatActivity implements BaseView {


    protected T mPresenter;

    private ProgressDialog mProgressDialog;
    protected Toast mToast;

    //ButterKnife
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initScreenAdaption();
        setContentView(getActivityLayoutId());
        //ButterKnife
        unbinder = ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        initPresenter();
        initView();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            LogUtils.e("BaseActivity mPresenter 不为空" + mPresenter.getClass());
        }
        initData();
        initEvent();
    }


    //    protected abstract void initScreenAdaption();//初始化屏幕适配
    protected abstract int getActivityLayoutId();////布局中Fragment的ID

    protected abstract void initPresenter();// 初始化数据,请求网络数据等
    protected abstract void initView();// 初始化数据,请求网络数据等
    protected abstract void initData();// 初始化数据,请求网络数据等

    protected abstract void initEvent();// 初始化监听事件。

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unbinder = null;
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }


    @Override
    public void showLoading() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(final String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        }
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast.setText(text);
                    mToast.show();
                }
            });
        } else {
            mToast.setText(text);
            mToast.show();
        }
    }


    @Override
    public void showErr() {
        showToast(getResources().getString(R.string.error_msg));
    }

    @Override
    public Context getContext() {
        return BaseActivity.this;
    }
}