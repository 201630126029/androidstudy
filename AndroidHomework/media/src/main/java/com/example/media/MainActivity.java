package com.example.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private NotificationManager mNotificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Button button=findViewById(R.id.notification);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =new NotificationCompat.Builder(this)
                .setContentTitle("标题")
                .setContentText("内容，有点不懂和Info有啥区别")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Huawei_Tune.ogg")))
                .setVibrate(new long[]{0,1000, 1000, 1000, 0, 1000, 0, 1000})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.preview))
                .build();

        mNotificationManager.notify(1, notification);
    }
}
