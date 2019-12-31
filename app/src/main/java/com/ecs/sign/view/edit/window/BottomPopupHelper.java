package com.ecs.sign.view.edit.window;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.ecs.sign.R;

/**
 * @author zw
 * @time 2019/12/11
 * @description
 */
public class BottomPopupHelper {

    private View view;

    public BottomPopupHelper(View view) {
        this.view =view;
    }

    public PopupWindow initPopWindow() {
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        PopupWindow linkWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //防止 软键盘可能会挡住PopupWindow
        linkWindow.setFocusable(true);
        linkWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //popWindow.setAnimationStyle(R.anim.add_new_in);  //设置加载动画
        linkWindow.setAnimationStyle(R.style.popup_window_anim_style);
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        linkWindow.setTouchable(true);
        linkWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        //要为popWindow设置一个背景 ,setOutsideTouchable（）才会生效;
        //而且，只有加上它之后，PopupWindow才会对手机的返回按钮有响应
        linkWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return linkWindow;
    }


}
