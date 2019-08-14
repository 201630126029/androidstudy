package com.example.a10;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class MyService extends IntentService {
    private static final String TAG = "MyService";

    MyService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("task_action");
        Log.d(TAG, "receiver task #" + action);
        SystemClock.sleep(3000);
        if ("com.example.action.TASK1".equals(action)) {
            Log.d(TAG, "handle task:" + action);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "service destroyed");
        super.onDestroy();
    }
}