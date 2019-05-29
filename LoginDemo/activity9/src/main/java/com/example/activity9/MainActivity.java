package com.example.activity9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView view1, view2, view3, view4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1=findViewById(R.id.image01);
        view2=findViewById(R.id.image02);
        view3=findViewById(R.id.image03);
        view4=findViewById(R.id.image04);
        view1.setOnClickListener(l);
        view2.setOnClickListener(l);
        view3.setOnClickListener(l);
        view4.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f=null;
            switch (v.getId()){
                case R.id.image01:f=new WeChat_Fragment(); break;
                case R.id.image02:f=new MessageFragment(); break;
                case R.id.image03:f=new FindFragment(); break;
                case R.id.image04:f=new MeFragment(); break;
            }
            ft.add(R.id.fragment, f);
            ft.commit();
        }
    };
}
