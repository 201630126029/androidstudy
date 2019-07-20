package com.example.a2;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    static {
        Log.d("xuanqis", "加载了Application");
    }
}
