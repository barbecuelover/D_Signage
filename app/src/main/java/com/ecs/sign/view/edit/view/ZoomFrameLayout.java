package com.ecs.sign.view.edit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 1.双击：
 * 正常情况下，双击会放大，
 * 放大情况下，双击回到正常。
 *
 * 2.控制缩放范围
 * 只能比原始画板大，不能小
 * 缩放是等比缩放 ，所以直接用X 来参照
 *
 * 3.放大模式 时滑动，不能出layout范围，否则四周有空白。
 *      滑动 和 手势缩放 不能冲突。
 *
 */
public class ZoomFrameLayout extends FrameLayout implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "zwcc";

    private static final float SCALE_MAX = 2.0f; //最大放大倍数
    private static final float SCALE_MID = 1.5f; //双击放大倍数
    private static final  float SCALE_INIT = 1.0f;//正常大小

    //双击缩放中 ，每次缩放的时间。（为了显得过程不是很突兀，将一次缩放过程分解成若干个小的缩放过程，类似一个动画的效果）
    private final int delayMillis = 16;
    //是否在双击的缩放过程中。
    private boolean isScaleIng = false;
   //缩放的手势检测
    private ScaleGestureDetector mScaleGestureDetector = null;
    //双击检测
    private GestureDetector mGestureDetector;
    // 放大模式下最小移动距离
//    private int touchSlop;


    public ZoomFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ZoomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZoomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
//        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScaleGestureDetector = new ScaleGestureDetector(context,this);
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap: ");
                if (isScaleIng){
                    //正在缩放中，双击不给予反应
                    return true;
                }
                // x,y 为缩放中心
                float x = e.getX();
                float y = e.getY();
                //如果处于正常状态，或者很小比例的 放大状态， 双击则放大
                //反之 双击则恢复到正常比例
                float scaleValue = getScaleX() < SCALE_MID ? SCALE_MID: SCALE_INIT;
                Log.d(TAG, "from:"+getScaleX() + "  ---to："+scaleValue);
                //postDelayed() 分解缩放过程 相当于 缩放动画。
                ZoomFrameLayout.this.postDelayed(new ScaleRunnable(scaleValue, x, y), delayMillis);
                isScaleIng = true;
                return true;
            }
        });
    }


    class ScaleRunnable implements Runnable{

        static final float BIGGER = 1.04f;
        static final float SMALLER = 0.96f;

        // x,y缩放中心点
        private float x;
        private float y;
        //当前缩放倍数
        private float currentScale;
        //最终目标缩放倍数
        private float targetScale;
        //单次缩放倍数
        private float tmpScale;


        public ScaleRunnable(float targetScale, float x, float y) {
            this.x =x;
            this.y = y;
            this.targetScale =targetScale;
            this.currentScale = getScaleX();
            // 根据 当前 和目标的 缩放比值 ，确定是放大还是缩小
            tmpScale = (getScaleX()<targetScale? BIGGER:SMALLER);
        }

        @Override
        public void run() {
            currentScale  = getScaleX();

            //这次应该缩放的倍数。  =  当前倍数  * 单词缩放倍数
            float onceTargetScale = currentScale * tmpScale;
            setScaleX(onceTargetScale);
            setScaleY(onceTargetScale);
            setPivotX(x);
            setPivotY(y);
           // ZoomFrameLayout.this.set;
            if (((tmpScale > 1f) && (onceTargetScale < targetScale)) || ((tmpScale < 1f) && (targetScale < onceTargetScale))) {
                //如果 还没放大到目标值 或者 缩小的目标值，继续缩放。
                Log.d(TAG, "run:  scale  ing ");
                ZoomFrameLayout.this.postDelayed(this, delayMillis);

            } else {
                Log.d(TAG, "run:  scale complete");
                //超出或者等于 目标比例范围 则设置为目标的缩放比例，并且结束 分解过程。
                setScaleX(targetScale);
                setScaleY(targetScale);
                setPivotX(x);
                setPivotY(y);
                isScaleIng = false;
            }


        }
    }



    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        float curScale = getScaleX();
        float tarScale = curScale * scaleFactor;

        if ((tarScale < SCALE_MAX && scaleFactor > 1.0f) || (tarScale > SCALE_INIT && scaleFactor < 1.0f)) {
            Log.d(TAG, "onScale: BETWEEN");
        }else if (tarScale >= SCALE_MAX && scaleFactor > 1.0f && curScale <SCALE_MAX){
            Log.d(TAG, "onScale: MAX");
            tarScale = SCALE_MAX;
        }else if(tarScale < SCALE_INIT && scaleFactor < 1.0f && curScale> SCALE_INIT){
            Log.d(TAG, "onScale: MIN");
            tarScale = SCALE_INIT;
        }else {
            Log.d(TAG, "onScale: Already MIN or MAX");
            return true;
        }
        scaleLayout(tarScale,detector.getFocusX(), detector.getFocusY());
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }



    private int lastPointerCount;
    private float lastX;
    private float lastY;



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);

        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        //每当触摸点发生变化时，lastX , lastY
        if (pointerCount!=lastPointerCount){
            lastX = x;
            lastY = y;
        }
        lastPointerCount = pointerCount;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (getScaleX() > SCALE_INIT){
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
                case MotionEvent.ACTION_MOVE:
                    //只有在放大模式中 并且当前没在手势缩放操作过程中，才能移动
                    if (getScaleX() > SCALE_INIT && !isScaleIng){
                        requestDisallowInterceptTouchEvent(true);
                    }else {
                        return true;
                    }
                    //获得滑动的 X,Y的相对值
                    float dx = x - lastX;
                    float dy = y - lastY;

                    //所谓的移动只是 重新设置了当前的中心点，方向与相对滑动方向相反。
                    float toX = getPivotX()-dx;
                    float toY = getPivotY() -dy;

                    //中心点范围不能超过 Layout的范围，否则 四周会显示多余的空白。
                    int max2X =  getWidth();
                    int max2Y = getHeight();

                    // 如果中心点范围 超过范围 则将他们设置成 最大或最小。
                    toX = toX<0? 0:toX;
                    toX = toX>max2X? max2X:toX;

                    toY = toY<0?0:toY;
                    toY = toY>max2Y? max2Y:toY;

                    //重新设置中心点，即完成所谓的移动效果。
                    setPivotX(toX);
                    setPivotY(toY);

                    //更新已操作过的 x,y坐标
                    lastX =x;
                    lastY = y;
                    Log.d(TAG, "onTouch: move x to:"+toX+ "---y to:"+toY);
                    break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        lastPointerCount = 0;
                    break;
        }

        return false;
    }

    /**
     * @param scale  缩放到的比例
     * @param focusX 缩放中心点x坐标
     * @param focusY 缩放中心点y坐标
     */
    private void scaleLayout(float scale,float focusX,float focusY){
        setScaleX(scale);
        setScaleY(scale);
        setPivotX(focusX);
        setPivotY(focusY);
    }

}
