package com.example.quanweizhinan;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication extends Application {
    private static ExecutorService executor = new ThreadPoolExecutor(4, 8, 60,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
        private  AtomicInteger count= new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread #" + count);
        }
    });

    public static void execute(Runnable runnable){
        Log.i("xuanqis", "要开始执行了");
         executor.execute(runnable);
        Log.i("xuanqis", "完毕");
    }

    private void test(){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("int", 10);
        intent.putExtras(bundle);
    }
}
