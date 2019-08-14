package com.example.firstcoding;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import java.util.concurrent.ConcurrentHashMap;

public class BaseActivity extends AppCompatActivity {
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(name.toString(), "连接成功...");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(name.toString(), "连接断开...");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xuanqis", "连接");
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
//        Log.d("xuanqis", "断开");
//        unbindService(mServiceConnection);
        super.onPause();
    }
}
