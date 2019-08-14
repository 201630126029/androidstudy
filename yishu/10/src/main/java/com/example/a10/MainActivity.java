package com.example.a10;

import android.app.IntentService;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService
                        .class);
                intent.putExtra("task_action", "com.example.action.TASK1");
                startService(intent);
                intent.putExtra("task_action", "com.example.action.TASK2");
                startService(intent);
                intent.putExtra("task_action", "com.example.action.TASK3");
                startService(intent);
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
