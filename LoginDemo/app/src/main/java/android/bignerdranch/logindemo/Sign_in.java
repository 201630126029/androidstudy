package android.bignerdranch.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sign_in extends AppCompatActivity {

    //注册的Button
    private Button regButton;
    //用户的学历
    private Spinner spinner;

    private EditText userName, pw_msg, rpw_msg;
    private RadioGroup rg_sex;
    private CheckBox hobby_swim;
    private CheckBox hobby_music;
    private CheckBox hobby_book;
    private TextView showMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        spinner = findViewById(R.id.academic_msg);
        userName = findViewById(R.id.username_msg);
        pw_msg = findViewById(R.id.pwd_msg);
        rpw_msg = findViewById(R.id.rpwd_msg);
        rg_sex = findViewById(R.id.rg_sex);
        hobby_swim = findViewById(R.id.hobby_swim);
        hobby_music = findViewById(R.id.hobby_music);
        hobby_book = findViewById(R.id.hobby_book);
        regButton = findViewById(R.id.signover_button);
        showMsg = findViewById(R.id.showMsg);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegClick(v);
            }
        });
    }

    /**
     * 检查点击了哪些选项，进行设置输出
     * @param v
     */
    public void onRegClick(View v) {
        String txt = "", name_str = "", pw_str = "", rpw_str = "";
        name_str = name_str + userName.getText();
        pw_str = pw_str + pw_msg.getText();
        rpw_str = rpw_str + rpw_msg.getText();
        if (name_str.length() == 0 || pw_str.length() == 0 || (pw_str.compareTo(rpw_str) != 0)) {
            Toast.makeText(this, "您输入的用户名或密码有误", Toast.LENGTH_SHORT).show();
            userName.setText("");
            userName.setFocusable(true);
        }
        txt = txt + name_str + " " + pw_str + "\n";

        if (rg_sex.getCheckedRadioButtonId() == R.id.sex_male) {
            txt = txt + "男";
        } else
            txt = txt + "女";
        txt = txt + "\n";

        if (hobby_swim.isChecked())
            txt = txt + hobby_swim.getText() + " ";
        if (hobby_music.isChecked())
            txt = txt + hobby_music.getText() + " ";
        if (hobby_book.isChecked())
            txt = txt + hobby_book.getText() + "\n";

        txt = txt + spinner.getSelectedItem().toString();

        showMsg.setText(txt);

        System.out.println(txt);
        setSignUpMsg(txt);
        Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置调用创建该Activity的返回值
     * @param signUpMsg
     */
    private void setSignUpMsg(String signUpMsg) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName.getText().toString());
        bundle.putString("pw_msg", pw_msg.getText().toString());
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        //说明Activity执行结束了
        finish();
    }
}
