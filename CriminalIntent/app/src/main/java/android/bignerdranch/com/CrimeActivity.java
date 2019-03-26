package android.bignerdranch.com;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();  //得到FragmentManager
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new CrimeFragment();  //创建fragment
            //1. 创建并返回fragment.2 参数为容器视图资源ID和新创建的CrimeFragment，使用容器视图资源ID
            //来作为fragment队列里面的唯一身份标志，最后再commit一下，旋转和回收内存等，fragmentmanager会
            //把fragment保存下来
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}