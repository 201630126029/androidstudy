package com.example.datasave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{
    private Button loginButton;
    private Button registerButton;
    private EditText usernameET;
    private EditText passwordET;
    private static final int REQUEST_CODE_GO_TO_REGIST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton= findViewById(R.id.login_button);
        registerButton= findViewById(R.id.register_button);
        usernameET= findViewById(R.id.username);
        passwordET= findViewById(R.id.password);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.login_button:
                String username=usernameET.getText().toString();
                String password=passwordET.getText().toString();
                String msg;
                if(username.equals("Admin")&&password.equals("I Love Android")) {
                     msg= "欢迎进入DIY";
                }else{
                     msg= "用户名或密码不正确，请重试";
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_button:
                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivityForResult(i,REQUEST_CODE_GO_TO_REGIST);
                break;
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GO_TO_REGIST:
                //判断注册是否成功  如果注册成功
                if (resultCode == RESULT_OK) {
                    //则获取data中的账号和密码  动态设置到EditText中
                    String r_username = data.getStringExtra("r_username");
                    String r_password = data.getStringExtra("r_password");
                    usernameET.setText(r_username);
                    passwordET.setText(r_password);
                    //显示其他信息
                    String r_sex = data.getStringExtra("r_sex");
                    String r_study = data.getStringExtra("r_study");
                    String r_hobbies = data.getStringExtra("r_hobbies");
                    String msg = "性别： " + r_sex + "\n学历： " + r_study + "\n爱好：" + r_hobbies;
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
