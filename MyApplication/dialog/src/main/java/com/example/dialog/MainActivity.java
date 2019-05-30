package com.example.dialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {
    TimePicker timepicker; 			// 时间拾取器
    Calendar c; 					// 日历对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timepicker=findViewById(R.id.timePicker1);
        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置闹钟
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                AlarmManager alarm =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timepicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timepicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                //设置一个闹钟
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                Toast.makeText(MainActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
