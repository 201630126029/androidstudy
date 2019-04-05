package com.example.wyc_3;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity{
    private Button loginButton;
    private Button sinUpButton;
    private EditText usernameET;
    private EditText passwordET;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        loginButton = (Button)findViewById(R.id.login_button);
        sinUpButton = (Button)findViewById(R.id.sign_up_button);
        usernameET = (EditText)findViewById(R.id.username);
        passwordET = (EditText)findViewById(R.id.password);

        sinUpButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameET.getText().toString().equals("") || passwordET.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"用户名或密码为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = "您输入的用户名是"+usernameET.getText()+",密码是"+passwordET.getText();
                if(!usernameET.getText().toString().equals(username)){
                    msg+=",但是这个用户名不存在";
                }
                else if(!passwordET.getText().toString().equals(password)){
                    msg+=",但是密码错误";
                }
                else{
                    ArrayList<String> hobby = intent.getStringArrayListExtra("hobbies");
                    int len = hobby.size();
                    if(len != 0) {
                        msg += ", 欢迎进入！喜欢";
                        for (int i = 0; i < len; i++) {
                            msg += hobby.get(i);
                            if (i != len - 1) {
                                msg += "、";
                            }
                        }
                        msg += "的人。";
                    }
                    Intent intent1 = new Intent(LoginActivity.this,AlbumActivity.class);
                    startActivity(intent1);
                }
                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
