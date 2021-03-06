package com.example.app3;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 测试动画滑动、拖动滑动、Edit Text的活动
 */
public class TestActivity extends Activity implements OnClickListener,
        OnLongClickListener {

    private static final String TAG = "TestActivity";

    //作为what
    private static final int MESSAGE_SCROLL_TO = 1;
    //滑动的帧
    private static final int FRAME_COUNT = 30;
    //等待时间
    private static final int DELAYED_TIME = 33;

    private Button mButton1;
    private View mButton2;

    private int mCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //弹性滑动
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        //scrollTo是内容的滑动，映像更深刻了
                        mButton1.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                }

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    /**
     * 为两个View设置监听
     */
    private void initView() {
        mButton1 = findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
        mButton2 = findViewById(R.id.button2);
        mButton2.setOnLongClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "button1.leftxuanqis=" + mButton1.getLeft());
            Log.d(TAG, "button1.x=" + mButton1.getX());
        }
    }

    /**
     * 处理Button的点击事件
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        if (v == mButton1) {


            //设置右移100
            mButton1.setTranslationX(100);
//
            Log.d(TAG, "button1.left=" + mButton1.getLeft());
            Log.d(TAG, "button1.x=" + mButton1.getX());

            //在1000ms的时间内将view从原始位置向右移动100像素，这是属性动画
//            ObjectAnimator.ofFloat(mButton1, "translationX", 0, 100)
//                    .setDuration(1000).start();

            //宽度加100， 左外边距加100
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mButton1
//                    .getLayoutParams();
//            params.width += 100;
//            params.leftMargin += 100;
//            mButton1.setLayoutParams(params);
//            mButton1.requestLayout();



//            //控制内容在1000ms内左移
//            final int startX = 0;
//            final int deltaX = 100;
//            ValueAnimator animator = ValueAnimator.ofInt(0,
//                    1).setDuration(1000);
//            animator.addUpdateListener(new AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animator) {
//                    float fraction = animator.getAnimatedFraction();
//                    mButton1.scrollTo(startX + (int) (deltaX * fraction), 0);
//                }
//            });
//            animator.start();

            mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }
}
