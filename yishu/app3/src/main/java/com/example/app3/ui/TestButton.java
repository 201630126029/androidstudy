package com.example.app3.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.nineoldandroids.view.ViewHelper;

/**
 * 可以拖动的TextView，中间尝试使用scrollTo，没成功
 * 因为是继承的TextView，所以wrap_content的问题解决
 * Action_down和Action_up交给父类处理
 * @author xuanqis
 */
public class TestButton extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TestButton";
    /**
     * 能够认为发生了滑动的最短距离，单位是像素
     */
    private int mScaledTouchSlop;
    /**
     * 分别记录上次滑动的坐标
     */
    private int mLastX = 0;
    private int mLastY = 0;

    public TestButton(Context context) {
        this(context, null);
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 得到发生滑动的最小距离
     */
    private void init() {
        //得到系统的滑动距离
        mScaledTouchSlop = ViewConfiguration.get(getContext())
                .getScaledTouchSlop();
        Log.d(TAG, "sts:" + mScaledTouchSlop);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //得到的是相对于屏幕左上角的坐标
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                super.onTouchEvent(event);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                //偏移量=现在-原来
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);
                //偏移量进行相加，进行移动
                int translationX = (int) ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(this) + deltaY;
                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                super.onTouchEvent(event);
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

}
