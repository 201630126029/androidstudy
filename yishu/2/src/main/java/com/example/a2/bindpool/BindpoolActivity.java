package com.example.a2.bindpool;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a2.R;

public class BindpoolActivity extends AppCompatActivity {
    private static final String TAG ="BinderPoolActivity";
    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindpool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork(){
        BinderPool binderPool=BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BIND_SECURITY_CENTER);
        mSecurityCenter=SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG, "visit IsecurityBinder");
        String msg = "hello android...";
        Log.d(TAG, "content:"+msg);
        try{
            String password = mSecurityCenter.encrypt(msg);
            Log.d(TAG, "加密："+password);
            Log.d(TAG, "解密："+mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "visit icompute...");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BIND_COMPUT);
        mCompute=ComputImpl.asInterface(computeBinder);
        try{
            Log.d(TAG, "3+5="+mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
