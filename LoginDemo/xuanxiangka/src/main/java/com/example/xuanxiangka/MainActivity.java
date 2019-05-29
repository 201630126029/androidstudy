package com.example.xuanxiangka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private TabHost mTabHost;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mLayoutInflater=LayoutInflater.from(this);
        mLayoutInflater.inflate(R.layout.tab1, mTabHost.getTabContentView());
        mLayoutInflater.inflate(R.layout.tab2, mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("精选表情").setContent(R.id.left));
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("投稿表情").setContent(R.id.right));

    }
}
