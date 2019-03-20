package com.example.first;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //两个一起形成view对象
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText); //绑定控件

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String text = editText.getText().toString();//获取用户信息
                textView.setText(text);
            }
        });
    }


}
