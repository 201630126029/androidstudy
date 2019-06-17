package com.example.home_asynctask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button xingyun = findViewById(R.id.xingyun),
                dianhuo = findViewById(R.id.dianhuo),
                jingdutiao=findViewById(R.id.jingdutiao);
        xingyun.setOnClickListener(this);
        dianhuo.setOnClickListener(this);
        jingdutiao.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.xingyun:
                 intent= new Intent(MainActivity.this, XingyunActivity.class);
                 break;
            case R.id.dianhuo:
                intent= new Intent(MainActivity.this, DianhuoActivity.class);
                break;
            case R.id.jingdutiao:
                intent= new Intent(MainActivity.this, JingdutiaoActivity.class);
                break;
                default:
        }
        startActivity(intent);
    }
}
