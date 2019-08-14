package com.example.firstcoding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    private Binder myBinder = new Binder();

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
}
