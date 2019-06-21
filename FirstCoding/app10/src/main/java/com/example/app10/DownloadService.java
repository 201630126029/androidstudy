package com.example.app10;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.DownloadListener;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {
    private DownLoadTask mDownLoadTask;
    private String downloadURL;
    private DownLoadListener mDownLoadListener= new DownLoadListener(){

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success.", -1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed() {
            mDownLoadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("download failed", -1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPaused() {
            mDownLoadTask=null;
            Toast.makeText(DownloadService.this, "下载暂停", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            mDownLoadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "下载取消", Toast.LENGTH_LONG).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload(String url){
            if(mDownLoadTask == null){
                downloadURL=url;
                mDownLoadTask = new DownLoadTask(mDownLoadListener);
                mDownLoadTask.execute(downloadURL);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "开始下载", Toast.LENGTH_LONG).show();
            }
        }

        public void pauseDownload(){
            if(mDownLoadTask != null){
                mDownLoadTask.pauseDownload();
            }
        }

        public void cancelDownload(){
            if(mDownLoadTask != null){
                mDownLoadTask.cancelDownload();
            }
            //应用的删除文件操作我觉得没处理好
            if(downloadURL != null){
                String fileName = downloadURL.substring(downloadURL.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory+fileName);
                if(file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
            }
        }
    }


    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress >= 0){
            builder.setContentText(progress+"%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();

    }
}
