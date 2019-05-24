package android.bignerdranch.logindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 */
public class Login_opt extends AppCompatActivity {
    //登录按钮
    private Button login_button;
    //用户名输入框和密码输入框
    private EditText usernameET, passwordET;
    //注册按钮
    private Button signup_button;

    private String userName = "123";
    private String pw_msg = "123456";
    private String input_userName = "";
    private String input_password;
    private static final int REQUEST_CODE_SIGNUP = 0;

    public Login_opt() {
        input_password = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_opt);

        login_button = findViewById(R.id.login_button);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        signup_button =  findViewById(R.id.signin_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_userName = usernameET.getText().toString();
                input_password = passwordET.getText().toString();
                if (input_userName.length() == 0 || input_password.length() == 0 ||
                        !input_userName.equals(userName) || !input_password.equals(pw_msg)) {
                    new AlertDialog.Builder(Login_opt.this).setTitle("登录信息有误")
                            .setMessage("请输入正确的用户名和密码")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //finish();
                                }
                            }).show();
                    usernameET.setText(input_userName);
                    passwordET.setText(input_password);
                    //获取焦点 光标出现
                    usernameET.requestFocus();
                }
                else {
                    String msg = "欢迎进入DIY!! \n您输入的用户名是:" + input_userName + "\n密码是：" + input_password;
                    Toast.makeText(Login_opt.this, msg, Toast.LENGTH_LONG).show();
                }
            }
        });


        //跳转进入登陆的页面
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_opt.this, Sign_in.class);
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //首先一定要调用父类的处理方法
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_SIGNUP) {
            if (data == null)
                return;

            Bundle bundle = data.getExtras();
            userName = bundle.getString("userName", "Admin");
            pw_msg = bundle.getString("pw_msg", "I love Android");
        }
    }
}
