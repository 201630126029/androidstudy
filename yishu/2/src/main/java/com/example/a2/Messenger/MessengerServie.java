package com.example.a2.Messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.a2.utils.MyConstants;

import java.io.Serializable;

public class MessengerServie extends Service {

    private static final String TAG = "MessengerService";


    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants
                        .MSG_FROM_CLIENT:
                    Log.i(TAG, "receive from client:" +
                            msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage=Message.obtain(null,MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "嗯，你的消息我已经收到了.");
                    replyMessage.setData(bundle);
                    try{
                        client.send(replyMessage);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);

            }
            super.handleMessage(msg);
        }
    }

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    public MessengerServie() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
