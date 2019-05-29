package com.example.tuxiangqiehuanqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {
    private ImageSwitcher switcher;
    private int[] arrayPictures = new int[]{
            R.drawable.img01,
            R.drawable.img02,
            R.drawable.img03,
            R.drawable.img04,
            R.drawable.img05,
            R.drawable.img06,
            R.drawable.img07,
            R.drawable.img08,
            R.drawable.img09

    };
    private int index;

    private float touchDownX, touchUpX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switcher = findViewById(R.id.imageSwitch);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image1 = new ImageView(MainActivity.this);
                image1.setImageResource(arrayPictures[index]);
                return image1;
            }
        });

        switcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDownX = event.getX();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    touchUpX = event.getX();
                    if (touchUpX - touchDownX > 100) {
                        Toast.makeText(MainActivity.this, ""+(touchUpX-touchDownX),Toast.LENGTH_LONG).show();
                        index = index == 0 ? arrayPictures.length - 1 : index - 1;
                        switcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this
                                , R.anim.slide_in_right));
                        switcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this
                                , R.anim.slide_out_left));
                        switcher.setImageResource(arrayPictures[index]);
                    } else if (touchUpX - touchDownX < 100) {
                        index = index == arrayPictures.length - 1 ? 0 : index + 1;
                        switcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this
                                , R.anim.slide_in_right));
                        switcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this
                                , R.anim.slide_out_left));
                        switcher.setImageResource(arrayPictures[index]);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
