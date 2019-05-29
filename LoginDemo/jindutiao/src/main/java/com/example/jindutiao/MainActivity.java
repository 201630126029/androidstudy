package com.example.jindutiao;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progress = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progressBar);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0x111){
                    progressBar.setProgress(progress);
                }
                else {
                    Toast.makeText(MainActivity.this, "耗时操作已完成", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    progress = doWork();
                    Message message = new Message();
                    if(progress < 100){
                        message.what = 0x111;
                        handler.sendMessage(message);
                    }
                    else{
                        message.what=0x110;
                        handler.sendMessage(message);
                        break;
                    }
                }
            }
        }).start();
    }
    private int doWork(){
        progress += Math.random()*10;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return progress;
    }
}
