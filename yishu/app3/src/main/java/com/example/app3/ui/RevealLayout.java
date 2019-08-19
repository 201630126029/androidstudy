package com.example.app3.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.app3.R;

import java.util.ArrayList;


/**
 * 一个特殊的LinearLayout,任何放入内部的clickable元素都具有波纹效果，当它被点击的时候，
 * 为了性能，尽量不要在内部放入复杂的元素
 * note: long click listener is not supported current for fix compatible bug.
 * @author xuanqis
 */
public class RevealLayout extends LinearLayout implements Runnable {

    private static final String TAG = "DxRevealLayout";
    private static final boolean DEBUG = true;

    /**
     * 画笔
     */

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 点击的View的宽高
     */
    private int mTargetWidth;
    private int mTargetHeight;

    /**
     * 点击的View的宽与高的最小值和最大值
     */
    private int mMinBetweenWidthAndHeight;
    private int mMaxBetweenWidthAndHeight;



    /**
     * 点击的中心点所在的坐标
     */
    private float mCenterX;
    private float mCenterY;


    /**
     * 半径最大值、半径单位变化值、当前的半径、父view的左上角的坐标数组
     */
    private int mMaxRevealRadius;
    private int mRevealRadiusGap;
    private int mRevealRadius = 0;
    private int[] mLocationInScreen = new int[2];

    private boolean mShouldDoAnimation = false;
    private boolean mIsPressed = false;
    private int INVALIDATE_DURATION = 40;



    private View mTouchTarget;
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable =
            new DispatchUpTouchEventRunnable();

    public RevealLayout(Context context) {
        super(context);
        init();
    }

    public RevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 进行初始化工作，设置画笔的颜色
     */
    private void init() {
        //有些ViewGroup设置了不能绘制，要将其进行重置
        setWillNotDraw(false);
        //设置画笔的颜色
        mPaint.setColor(getResources().getColor(R.color.reveal_color));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocationInScreen);
    }

    /**
     * 根据点击的位置，确定波纹的最大半径，还有其他的初始半径等也初始化
     * @param event 点击事件，需要事件的坐标
     * @param view 进行初始化的View
     */
    private void initParametersForChild(MotionEvent event, View view) {

        //这里是相对于点击View的相对坐标，这个不知道为啥好像是相对于RevealLayout的坐标
        mCenterX = event.getX();
        mCenterY = event.getY();

        mTargetWidth = view.getMeasuredWidth();
        mTargetHeight = view.getMeasuredHeight();

        mMinBetweenWidthAndHeight = Math.min(mTargetWidth, mTargetHeight);
        mMaxBetweenWidthAndHeight = Math.max(mTargetWidth, mTargetHeight);

        //初始半径为0
        mRevealRadius = 0;
        //。。。
        mShouldDoAnimation = true;
        //。。。
        mIsPressed = true;
        //增加的单位是宽度和高度最小值的 1/8
        mRevealRadiusGap = mMinBetweenWidthAndHeight / 8;


        //这里以后看到了再看一遍，感觉是有点问题，不对劲

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        // left：点击的View和父View的x坐标的差值
        int left = location[0] - mLocationInScreen[0];
        //transformedCenterX: 点击的点相对于View的偏移 - view相对于父View的偏移
        int transformedCenterX = (int) mCenterX - left;
        //最长的半径的值为
        mMaxRevealRadius = Math.max(transformedCenterX, mTargetWidth - transformedCenterX);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //播放为为false、目标View的宽度为0、目标View为null
        if (!mShouldDoAnimation || mTargetWidth <= 0 || mTouchTarget == null) {
            return;
        }

        //增大半径，有点牛逼，一次到位
        if (mRevealRadius > mMinBetweenWidthAndHeight / 2) {
            mRevealRadius += mRevealRadiusGap * 4;
        } else {
            mRevealRadius += mRevealRadiusGap;
        }

        this.getLocationOnScreen(mLocationInScreen);
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        //画布的显示范围
        int left = location[0] - mLocationInScreen[0];
        int top = location[1] - mLocationInScreen[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();

        //保存现有状态
        canvas.save();
        //设置画布的显示范围
        canvas.clipRect(left, top, right, bottom);
        //绘制波纹
        canvas.drawCircle(mCenterX, mCenterY, mRevealRadius, mPaint);
        //restore和save要配对使用
        canvas.restore();

        if (mRevealRadius <= mMaxRevealRadius) {
            //在指定的时间后，将指定的矩形进行刷新
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        } else if (!mIsPressed) {
            //不需要继续进行绘制了
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //得到相对于屏幕的绝对地址
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            //得到点击事件所在的具体的View
            View touchTarget = getTouchTarget(this, x, y);

            if (touchTarget != null && touchTarget.isClickable() && touchTarget.isEnabled()) {
                mTouchTarget = touchTarget;
                //使用事件去初始化View的相关信息
                initParametersForChild(event, touchTarget);
                //等40ms进行重绘制
                postInvalidateDelayed(INVALIDATE_DURATION);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            mIsPressed = false;
            //抬起进行重绘，颜色消失
            postInvalidateDelayed(INVALIDATE_DURATION);

            mDispatchUpTouchEventRunnable.event = event;
            postDelayed(mDispatchUpTouchEventRunnable, 200);
            return true;
        } else if (action == MotionEvent.ACTION_CANCEL) {
            mIsPressed = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 得到ViewGroup的点击的点在哪个孩子中
     * @param view
     * @param x
     * @param y
     * @return 点所在的孩子
     */
    private View getTouchTarget(View view, int x, int y) {
        View target = null;
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }

        return target;
    }

    /**
     * 判断点击的点(x,y)是否在View内
     * @param view 判断的View
     * @param x x坐标
     * @param y y坐标
     * @return 点是否在View内
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return view.isClickable() && y >= top && y <= bottom
                && x >= left && x <= right;
    }

    @Override
    public boolean performClick() {
        postDelayed(this, 400);
        return true;
    }

    @Override
    public void run() {
        super.performClick();
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        @Override
        public void run() {
            if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
                return;
            }

            if (isTouchPointInView(mTouchTarget, (int) event.getRawX(), (int) event.getRawY())) {
                mTouchTarget.performClick();
            }
        }
    }

}
