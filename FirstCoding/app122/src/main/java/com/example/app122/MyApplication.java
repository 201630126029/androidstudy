package com.example.app122;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication extends Application {
    private static final ExecutorService threadPool = new ThreadPoolExecutor(4, 8, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"AsyncTask #" + mCount.getAndIncrement());
        }
    });

    public static void execute(Runnable runnable){
        threadPool.execute(runnable);
    }

    void test(){
//        AsyncTask
    }
}
