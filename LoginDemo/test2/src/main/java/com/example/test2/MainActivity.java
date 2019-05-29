package com.example.test2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private CheckBox checkBox1, checkBox2, checkBox3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox1=findViewById(R.id.checkbox1);
        checkBox2=findViewById(R.id.checkbox2);
        checkBox3=findViewById(R.id.checkbox3);
        btnLogin= findViewById(R.id.login);  
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checked = "";
                if(checkBox1.isChecked()){
                    checked += checkBox1.getText();
                }
                if(checkBox2.isChecked()){
                    checked+=checkBox2.getText();
                }
                if(checkBox3.isChecked()){
                    checked+= checkBox3.getText();
                }
                Toast.makeText(MainActivity.this, checked, Toast.LENGTH_LONG).show();
            }
        });
    }
}
