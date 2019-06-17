package com.example.homewordk_6_2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.homewordk_6_2.ok.DownloadListener;
import com.example.homewordk_6_2.ok.TaskInfo;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private TaskInfo info;
    private DownloadBinder mBinder = new DownloadBinder();


    private DownloadListener listener = new DownloadListener() {
        /****************Begin************/
        //根据要求实现DownloadListener的5个方法

        /**
         * 用于通知当前的下载进度
         * @param progress
         */
        @Override
        public void onProgress(int progress) {
            Log.i("haha", "onProgress");
            info.setStatus(DownloadTask.TYPE_DOWLOADING);
//            startForeground(1, getNotification("Downloading...", getCurrentDownloadProgress()));

        }

        @Override
        public void onSuccess() {
            Log.i("haha", "onSuccess");
            info.setStatus(DownloadTask.TYPE_SUCCESS);
//            startForeground(1, getNotification("onSuccess...", getCurrentDownloadProgress()));

        }

        @Override
        public void onFailed() {
            Log.i("haha", "onFailed");
            info.setStatus(DownloadTask.TYPE_FAILED);
//            startForeground(1, getNotification("onFailed", getCurrentDownloadProgress()));

        }

        @Override
        public void onPaused() {
            Log.i("haha", "oonPaused");
            info.setStatus(DownloadTask.TYPE_PAUSED);
//            startForeground(1, getNotification("onPaused...", getCurrentDownloadProgress()));


        }

        @Override
        public void onCanceled() {
            Log.i("haha", "onCanceled");
            info.setStatus(DownloadTask.TYPE_CANCELED);
//            startForeground(1, getNotification("onCanceled...", getCurrentDownloadProgress()));

        }


        /****************End**************/

    };


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

    /**
     * 开始下载任务，将任务的信息放到taskInfo中去
     *
     * @param url
     */
    public void startDownload(String url) {
        if (downloadTask == null) {
            this.info = new TaskInfo(url);
            /***************Begin*************/
            //创建TaskInfo实例，并作创建downloadTask实例的参数
            //然后启动异步任务，开始下载

            info.setStatus(DownloadTask.TYPE_DOWLOADING);

            downloadTask = new DownloadTask(listener, info);

            //这里的statues需要改
            downloadTask.execute();





            /***************End**************/
//            startForeground(1, getNotification("Downloading...", 0));
            Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseDownload() {
        if (downloadTask != null) {
            downloadTask.pauseDownload();
        }
    }

    public void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancelDownload();
        } else {
            String downloadUrl = info.getUrl();
            if (downloadUrl != null) {
                // 取消下载时需将文件删除，并将通知关闭
                //String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                //String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
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

    /**
     * 获取已完成的长度除以总的长度来作为进度
     *
     * @return 百分制的进度
     */
    int getCurrentDownloadProgress() {
        //计算下载进度
        int currentProgress = (int) ((float) info.getCompletedLen() / (float) info.getContentLen() * 100);
        return currentProgress;
    }

    /**
     * 获取下载的状态
     *
     * @return
     */
    int getCurrentStatus() {
        return info.getStatus();
    }

    /**
     * 获取通知服务管理器
     *
     * @return 系统的通知管理器
     */
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
//    private Notification getNotification(String title, int progress) {
//        /**************************Begin**********************/
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // 创建一个Notification对象
//        Notification.Builder notification = new Notification.Builder(this);
//
//        notification.setProgress(100, progress, false);
//        // 设置打开该通知，该通知自动消失
////        notification.setAutoCancel(true);
//        // 设置通知的图标
//        notification.setSmallIcon(R.mipmap.ic_launcher);
//        // 设置通知内容的标题
//        notification.setContentTitle(title);
//        // 设置通知内容
////        notification.setContentText(title);
//        //设置使用系统默认的声音、默认震动
//        notification.setDefaults(Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE);
//        //设置发送时间
//        notification.setWhen(System.currentTimeMillis());
//        // 创建一个启动其他Activity的Intent
////        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
////        PendingIntent pi = PendingIntent.getActivity(
////                MainActivity.this, 0, intent, 0);
//        //设置通知栏点击跳转
////        notification.setContentIntent(pi);
//        //发送通知
////        notificationManager.notify(NOTIFYID, notification.build());
//        return notification.build();
//
//        /*************************End************************/
//
//    }
}
