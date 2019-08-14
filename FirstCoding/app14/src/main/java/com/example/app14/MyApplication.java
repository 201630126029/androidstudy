package com.example.app14;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private static Context mContext;
    public static final String AppTag="xuanqis";
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        LitePal.initialize(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

}
