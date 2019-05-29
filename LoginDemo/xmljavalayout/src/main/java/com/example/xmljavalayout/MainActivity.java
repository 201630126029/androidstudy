package com.example.xmljavalayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView[] imageViews = new ImageView[12];
    private int[] imagesPath = new int[]{
            R.mipmap.img01,
            R.mipmap.img02,
            R.mipmap.img03,
            R.mipmap.img04,
            R.mipmap.img05,
            R.mipmap.img06,
            R.mipmap.img07,
            R.mipmap.img09,
            R.mipmap.img10,
            R.mipmap.img11,
            R.mipmap.img12

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout layout = findViewById(R.id.layout);
        for (int i = 0; i < imagesPath.length; i++) {
            imageViews[i] = new ImageView(MainActivity.this);
            imageViews[i].setImageResource(imagesPath[i]);
            imageViews[i].setPadding(2,2,2,2);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(315, 236);
            imageViews[i].setLayoutParams(params);
            layout.addView(imageViews[i]);
        }
    }
}
