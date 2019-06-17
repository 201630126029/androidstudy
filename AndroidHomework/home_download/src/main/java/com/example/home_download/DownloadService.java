package com.example.home_download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private TaskInfo info; //下载信息的JavaBean


    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            Log.i("xuanqisnow", "onProgress");
            getNotificationManager().notify(1, getNotification("下载中...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            Log.i("xuanqisnow", "onSuccess");
            info.setStatus(DownloadTask.TYPE_SUCCESS);
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("下载成功", -1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Log.i("xuanqisnow", "onFailed");
            info.setStatus(DownloadTask.TYPE_FAILED);
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("下载失败", -1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Log.i("xuanqisnow", "onPause");
            info.setStatus(DownloadTask.TYPE_PAUSED);
            Toast.makeText(DownloadService.this, "下载暂停", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            Log.i("xuanqisnow", "onCancel");
            stopForeground(true);
            Toast.makeText(DownloadService.this, "取消", Toast.LENGTH_SHORT).show();
        }


    };

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        pauseDownload();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void startDownload(String url) {
        if (downloadTask == null) {
            this.info = new TaskInfo(url);
            /***************Begin*************/
            //创建TaskInfo实例，并作创建downloadTask实例的参数
            //然后启动异步任务，开始下载

            downloadTask = new DownloadTask(listener, info);
            info.setStatus(DownloadTask.TYPE_DOWLOADING);
            downloadTask.execute();
            startForeground(1, getNotification("下载中...", 0));
            Toast.makeText(DownloadService.this, "下载中...", Toast.LENGTH_SHORT).show();


            /***************End**************/
            startForeground(1, getNotification("Downloading...", 0));
            Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseDownload() {
        Log.i("xuanqisnow", "downservice的pauseDownload执行了");
        if (downloadTask != null) {
            downloadTask.pauseDownload();
        }
        else {
            Log.i("xuanqisnow", "pause 这个downloatask为空");
        }
    }

    public void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancelDownload();
        } else {
            String downloadUrl = info.getUrl();
            if (downloadUrl != null) {
//                 取消下载时需将文件删除，并将通知关闭
                String fileName = info.getName();
                String directory = info.getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //当前下载进度
    int getCurrentDownloadProgress() {
        //计算下载进度
        int currentProgress = (int) ((float) info.getCompletedLen() / (float) info.getContentLen() * 100);
        return currentProgress;
    }

    //当前的下载状态
    int getCurrentStatus() {
        return info.getStatus();
    }

    /*获取通知服务管理器*/
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 配置通知并获取通知实例后返回该通知对象
     *
     * @param title
     * @param progress
     * @return
     */
    private Notification getNotification(String title, int progress) {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle(title)
                .setProgress(100, progress, false)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pi);

        if(progress >= 0){
            builder.setContentText(progress+"%");
            builder.setProgress(100, progress, false);
        }



        return builder.build();


    }
}
