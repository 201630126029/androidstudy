package com.example.loginandregister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends Activity implements RadioGroup.OnCheckedChangeListener, Button.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText r_username;
    private EditText r_password;
    private EditText r_makesure;
    private Button realRegisButton;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String study;
    private RadioGroup radioGroup_gender;
    private String sex;
    private CheckBox sing;
    private CheckBox dance;
    private CheckBox basketball;
    private CheckBox read;
    private Map<String, String> loveMap = new HashMap<String, String>();
    private String hobbies = " ";
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        r_username = findViewById(R.id.r_username);
        r_password = findViewById(R.id.r_password);
        r_makesure = findViewById(R.id.r_makesure);

        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        radioGroup_gender.setOnCheckedChangeListener(this);

        spinner = findViewById(R.id.spinner);
        data_list = new ArrayList<String>();
        data_list.add("小学");
        data_list.add("初中");
        data_list.add("高中");
        data_list.add("专科");
        data_list.add("本科");
        data_list.add("硕士研究生");
        data_list.add("博士研究生");
        data_list.add("博士后");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        //选择item的选择点击监听事件
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String select = spinner.getSelectedItem().toString();
                study = select;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                String str = "请选择学历水平";
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });

        sing = findViewById(R.id.sing);
        dance = findViewById(R.id.dance);
        basketball = findViewById(R.id.basketball);
        read = findViewById(R.id.read);
        sing.setOnCheckedChangeListener(this);
        dance.setOnCheckedChangeListener(this);
        basketball.setOnCheckedChangeListener(this);
        read.setOnCheckedChangeListener(this);

        realRegisButton = findViewById(R.id.register_real);
        realRegisButton.setOnClickListener(this);

        text = findViewById(R.id.textView5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_real:
                String usernameET = r_username.getText().toString();
                String passwordET = r_password.getText().toString();
                String makesureET = r_makesure.getText().toString();
                String msg;
                if (usernameET.equals("")) {
                    msg = "请输入用户名！";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                } else if (passwordET.equals("")) {
                    msg = "请输入密码！";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                } else if (makesureET.equals("")) {
                    msg = "请确认密码！";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                } else if (!(passwordET.equals(makesureET))) {
                    msg = "请确认输入密码是否正确！";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                } else if (loveMap.isEmpty()) {
                    msg = "请选择至少一个爱好！";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                } else {
                    for (String key : loveMap.keySet()) {
                        String value = loveMap.get(key);
                        if (!(value.equals(""))){
                            hobbies = hobbies + value + " ";
                        }
                    }
                    msg = "新用户名： " + usernameET + "\n密码： " + passwordET + "\n性别： " + sex + "\n学历： " + study + "\n爱好：" + hobbies;

                    text.setText(msg);
                    text.setTextSize(20);
                    text.setTextColor(Color.parseColor("#96111211"));
                    Intent data = new Intent();
                    data.putExtra("r_username", usernameET);
                    data.putExtra("r_password", passwordET);
                    data.putExtra("r_sex", sex);
                    data.putExtra("r_study", study);
                    data.putExtra("r_hobbies", hobbies);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton_checked = group.findViewById(checkedId);
        String gender = radioButton_checked.getText().toString();
        sex = gender;
    }

    @Override
    public void onCheckedChanged(CompoundButton checkbox, boolean checked) {
        switch (checkbox.getId()) {
            case R.id.sing:
                if (checked) {
                    loveMap.put("sing", sing.getText().toString());
                } else {
                    loveMap.remove("sing");
                }
                break;
            case R.id.dance:
                if (checked) {
                    loveMap.put("dance", dance.getText().toString());
                } else {
                    loveMap.remove("dance");
                }
                break;
            case R.id.basketball:
                if (checked) {
                    loveMap.put("basketball", basketball.getText().toString());
                } else {
                    loveMap.remove("basketball");
                }
                break;
            case R.id.read:
                if (checked) {
                    loveMap.put("read", read.getText().toString());
                } else {
                    loveMap.remove("read");
                }
                break;
            default:
        }
    }
}
