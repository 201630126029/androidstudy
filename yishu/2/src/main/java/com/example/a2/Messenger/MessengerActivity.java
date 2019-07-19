package com.example.a2.Messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a2.R;
import com.example.a2.utils.MyConstants;

/**
 * 客户端Activity
 */
public class MessengerActivity extends AppCompatActivity {
    private static final String TAG = "MessengerActivity";
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants
                        .MSG_FROM_SERVICE:
                    Log.i(TAG, "receiver from service:"+msg.getData().getString("reply"));
                break;
                default:super.handleMessage(msg);
            }
        }
    }
    /**
     * 服务器的Messenger
     */
    private Messenger mServer;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServer=new Messenger(service);
            Message msg = Message.obtain();
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client.");
            msg.setData(data);
            msg.replyTo=mGetReplyMessenger;
            try{
                mServer.send(msg);
            }catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerServie.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
