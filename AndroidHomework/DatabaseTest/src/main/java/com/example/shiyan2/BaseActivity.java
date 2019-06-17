package com.example.shiyan2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("xuanqis", "oncreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("xuanqis", "ondestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("xuanqis", "onpause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("xuanqis", "onresume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("xuanqis", "onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("xuanqis", "onstop");
    }
}
