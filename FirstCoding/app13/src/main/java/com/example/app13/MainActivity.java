package com.example.app13;

import android.app.AlarmManager;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ThemedSpinnerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime()+10*1000;
//        manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime,);

        Button button = new Button(this);
        new Thread(()->{

        }).start();
    }
}
