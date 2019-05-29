package com.example.test1;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg = findViewById(R.id.rg);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rg.getChildAt(i);
                    if (rb.isChecked()) {
                        if (rb.getText().toString().equals("B. 100")) {
                            Toast.makeText(MainActivity.this, "正确", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("答案是100.");
                            builder.setPositiveButton("确定", null).show();
                        }
                        break;  
                    }
                }
            }
        });
    }
}
