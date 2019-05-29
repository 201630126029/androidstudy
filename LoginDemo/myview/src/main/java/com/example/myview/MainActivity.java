package com.example.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = findViewById(R.id.mylayout);
        final RabbitView rabbit = new RabbitView(this);
        rabbit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rabbit.bitMapX = event.getX();
                rabbit.bitMapY = event.getY();
                //重新绘制
                rabbit.invalidate();
                return false;
            }
        });

        //注意一定要将其加入布局
        frameLayout.addView(rabbit);
    }

}
