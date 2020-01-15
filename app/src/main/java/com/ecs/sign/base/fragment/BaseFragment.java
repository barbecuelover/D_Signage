package com.ecs.sign.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecs.sign.base.BaseView;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.presenter.AbstractBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author maoqitian
 * @Description MVP Fragment 基类
 * @Time 2018/12/14 0014 22:47
 */
public abstract class BaseFragment<T extends AbstractBasePresenter> extends Fragment implements BaseView {

    //ButterKnife
    private Unbinder mUnbinder;
    protected T mPresenter;
    View view;
    protected BaseActivity context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        if(mPresenter != null){
            LogUtils.d("BaseFragment mPresenter 不为空" + mPresenter.getClass());
            mPresenter.attachView(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder= ButterKnife.bind(this,view);
        context =(BaseActivity) getActivity();
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter != null){
            mPresenter.detachView();
        }
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mPresenter != null){
            mPresenter = null;
        }
    }
    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initPresenter();// 初始化数据,请求网络数据等
    protected abstract void initData();// 初始化数据,请求网络数据等
    protected abstract void initEvent();// 初始化监听事件。



    @Override
    public void showLoading() {
        context.showLoading();
    }

    @Override
    public void hideLoading() {
        context.hideLoading();
    }

    @Override
    public void showToast(String msg) {
        context.showToast(msg);
    }

    @Override
    public void showErr() {
        context.showErr();
    }
}
