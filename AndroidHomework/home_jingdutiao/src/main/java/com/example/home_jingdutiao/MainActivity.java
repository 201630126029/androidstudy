package com.example.home_jingdutiao;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_start;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = findViewById(R.id.btn_start);
        pb = findViewById(R.id.pb);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsynTask().execute(1, 500);
            }
        });
    }

    class MyAsynTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            String ret;
            Integer bushu, sleeptime;
            bushu = params[0];
            sleeptime = params[1];
            for (Integer i = 1; i <= 10; i += bushu) {
                publishProgress(i);
                SystemClock.sleep(sleeptime);
            }
            ret = "更新完毕……";
            return ret;
        }

        @Override
        protected void onPreExecute() {
            pb.setProgress(0);
            btn_start.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int p = pb.getMax() / 10 * values[0];
            pb.setProgress(p);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            btn_start.setEnabled(true);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }
}
