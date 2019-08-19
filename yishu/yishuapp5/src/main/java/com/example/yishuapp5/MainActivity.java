package com.example.yishuapp5;

import com.example.yishuapp5.utils.MyConstants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

/**
 * @author xuanqis
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private LinearLayout mRemoteViewsContent;

    /**
     * 接收消息的广播，作为另一个进程更新界面的接收器
     */
    private BroadcastReceiver mRemoteViewsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收广播中的RemoteView信息，进行更新
            RemoteViews remoteViews = intent
                    .getParcelableExtra(MyConstants.EXTRA_REMOTE_VIEWS);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 创建当前进程的RemoteView，进行初始化
     */
    private void initView() {
        mRemoteViewsContent = findViewById(R.id.remote_views_content);
        IntentFilter filter = new IntentFilter(MyConstants.REMOTE_ACTION);
        registerReceiver(mRemoteViewsReceiver, filter);
    }

    /**
     * 使用Remote View设置一个当前进程的View
     * @param remoteViews 含有设置Action参数的RemoteView
     */
    private void updateUI(RemoteViews remoteViews) {
//        View view = remoteViews.apply(this, mRemoteViewsContent);
        //得到布局资源的统一的Id
        int layoutId = getResources().getIdentifier("layout_simulated_notification",
                "layout", getPackageName());
        //得到具体的RemoteView出来
        View view = getLayoutInflater().inflate(layoutId, mRemoteViewsContent, false);
        //使用RemoteView来进行具体的更新
        remoteViews.reapply(this, view);
        mRemoteViewsContent.addView(view);
    }

    @Override
    protected void onDestroy() {
        //取消注册广播
        unregisterReceiver(mRemoteViewsReceiver);
        super.onDestroy();
    }

    /**
     * 在Layout里面设置的点击事件的处理规则
     * @param v 点击的View
     */
    public void onButtonClick(View v) {
        if (v.getId() == R.id.button1) {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.button2) {
            Intent intent = new Intent(this, DemoActivity_2.class);
            startActivity(intent);
        }
    }

}
