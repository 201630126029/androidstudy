package com.example.gundongshitu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ll = findViewById(R.id.ll);
        LinearLayout ll2 = new LinearLayout(this);
        ll2.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        ll.addView(scrollView);
        scrollView.addView(ll2);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.cidian);
        TextView textView=new TextView(this);
        textView.setText(R.string.cidian);
        ll2.addView(imageView);
        ll2.addView(textView);
    }
}
