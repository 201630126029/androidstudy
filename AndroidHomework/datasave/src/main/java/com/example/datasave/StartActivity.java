package com.example.datasave;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_start);
        img = findViewById(R.id.startImg);
//        Animation animation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.rotate);
//        // 启动动画
//        img.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


}
