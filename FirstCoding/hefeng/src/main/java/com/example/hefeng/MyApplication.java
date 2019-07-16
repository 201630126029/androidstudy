package com.example.hefeng;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("xuanqis", "初始化");
        HeConfig.init("HE1907061632271763", "b28234a549bb4dffaa437844402cc85f");
        HeConfig.switchToFreeServerNode();
    }

    void test(){

    }

    @Override
    public int hashCode() {

        return super.hashCode();
    }
}
