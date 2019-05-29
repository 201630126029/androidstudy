package com.example.timer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {
    int year, month, day;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePicker=findViewById(R.id.datePicker);
        Calendar calendar =Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                MainActivity.this.year=year;
                MainActivity.this.month=monthOfYear;
                MainActivity.this.day=dayOfMonth;
                show(MainActivity.this.year,MainActivity.this.month,MainActivity.this.day);
            }
        });
    }

    private void show(int year, int month, int day){
        String str = year+"年"+(month+1)+"月"+day+"日";
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }
}
