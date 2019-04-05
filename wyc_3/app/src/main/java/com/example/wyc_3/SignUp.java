package com.example.wyc_3;

import android.content.Intent;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    private Spinner spinner;
    private String username;
    private String password;
    private EditText name;
    private EditText pwd1;
    private EditText pwd2;
    private TextView info;
    private String degree;
    private CheckBox hobby_swim;
    private CheckBox hobby_music;
    private CheckBox hobby_book;
    private RadioButton bsex;
    private RadioButton gsex;
    private ArrayList<String> hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        spinner = (Spinner)findViewById(R.id.academic_msg);
        name = (EditText)findViewById(R.id.username_msg);
        pwd1 = (EditText)findViewById(R.id.pwd_msg);
        pwd2 = (EditText)findViewById(R.id.rpwd_msg);
        hobby_book = (CheckBox)findViewById(R.id.hobby_book);
        hobby_music = (CheckBox)findViewById(R.id.hobby_music);
        hobby_swim = (CheckBox)findViewById(R.id.hobby_swim);
        info = (TextView)findViewById(R.id.info);
        hobby = new ArrayList<String>();
        bsex = (RadioButton)findViewById(R.id.sex_male);
        gsex = (RadioButton)findViewById(R.id.sex_female);
    }
    public void onRegClick(View v){

        if(hobby_book.isChecked()){
            hobby.add(hobby_book.getText().toString());
        }
        if(hobby_music.isChecked()){
            hobby.add(hobby_music.getText().toString());
        }
        if(hobby_swim.isChecked()){
            hobby.add(hobby_swim.getText().toString());
        }
        Log.d("fuck", "hobby_size: " + hobby.size());
        degree = spinner.getSelectedItem().toString();
        Toast.makeText(this, degree,Toast.LENGTH_SHORT).show();
        password = pwd1.getText().toString();
        username = name.getText().toString();
        if("".equals(username)||"".equals(password)||"".equals(pwd2.getText().toString())){
            Toast.makeText(this,"用户名为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pwd1.getText().toString().equals(pwd2.getText().toString())){
            Log.d("fuck", "pwd1: "+pwd1.getText().toString());
            Log.d("fuck", "pwd2: "+pwd2.getText().toString());
            Toast.makeText(this,"两次密码不一样！",Toast.LENGTH_SHORT).show();
            return;
        }
        String sex;
        if (bsex.isChecked()){
            sex = "男";
        }else{
            sex = "女";
        }
        String hob= "";
        for (String hoh:
             hobby) {
            hob+=hoh;
            hob+="、";
        }
        info.setText("用户名是" + username + "\n" +
                "密码是" + password + "\n" +
                "性别是" + sex +
                "兴趣是" + hob
        );
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        intent.putExtra("degree", degree);
        intent.putStringArrayListExtra("hobbies", hobby);
        startActivity(intent);
    }

}
