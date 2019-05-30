package com.example.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    //声明第一个动作
    private static final String action1="zuckerberg";
    //声明第二个动作
    private static final String action2="mayun";
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action1)){
            //回复该动作收到广播
            Toast.makeText(context, "MyReceiver收到:"+action1+"的广播",
                    Toast.LENGTH_SHORT).show();
        }else if (intent.getAction().equals(action2)){
            //回复该动作收到广播
            Toast.makeText(context, "MyReceiver收到:"+action2+"的广播",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
