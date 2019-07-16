package com.example.quanweizhinan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView=findViewById(R.id.text);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.execute(new Runnable() {
                    @Override
                    public void run() {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.currentThread().sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                textView.setText("你好");
                            }
                        });
                    }
                });
            }
        });
    }
}
