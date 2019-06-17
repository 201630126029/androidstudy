package com.example.home_xingyundachoujiang;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_ok;
    TextView tv_result;
    private Thread luckThread;
    private Handler mHandler;
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_result.setText(msg.obj.toString());
            }
        };
    }
    private  void init(){
        btn_start = findViewById(R.id.btn_start);
        btn_ok= findViewById(R.id.btn_ok);
        tv_result = findViewById(R.id.result);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (luckThread==null || !luckThread.isAlive()) {
                    //flag = true;
                    luckThread = new LuckThread();
                    luckThread.start();
                    Log.e("===", "start");
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (luckThread != null && luckThread.isAlive()) {
                    luckThread.interrupt();
                    luckThread = null;
                    //flag = false;
                    Toast.makeText(getApplicationContext(), "大奖已经诞生了！", Toast.LENGTH_LONG).show();
                    Log.e("===", "end");
                }
            }
        });
    }

    class LuckThread extends Thread{
        @Override
        public void run() {
            super.run();
            String[] names=getData();
            try
            {
                while(!Thread.interrupted())
                {
                    Thread.sleep(500);
                    Random rand =new Random();
                    int r=rand.nextInt(names.length);
                    String luckman=names[r];
                    Message msg=Message.obtain();
                    msg.obj=luckman;
                    mHandler.sendMessage(msg);
                    Log.e("====",luckman);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getData(){
        List<String> data= new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            data.add("2016301260"+i);
        }
        return data.toArray(new String[data.size()]);
    }
}
