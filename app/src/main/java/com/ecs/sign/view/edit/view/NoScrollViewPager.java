package com.ecs.sign.view.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author zw
 * @time 2019/12/16
 * @description
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isScroll = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    /**
     *  是否拦截
     *  拦截:会走到自己的onTouchEvent方法里面来
     *  不拦截:事件传递给子孩子
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll){
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isScroll) {
            return super.onTouchEvent(ev);
        }else {
            return true;
        }

    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

}
