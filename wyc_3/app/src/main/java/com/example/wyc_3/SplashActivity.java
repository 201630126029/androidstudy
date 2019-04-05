package com.example.wyc_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "fuck";
    private Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "这个activity是启动了的");
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    Log.d(TAG, "休眠前的时间" + date.getTime());
                    sleep(2000);//使程序休眠五秒
                    Log.d(TAG, "休眠后的时间" + date.getTime());
                    Intent it=new Intent(getApplicationContext(),SignUp.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
