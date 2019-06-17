package com.example.homewordk_6_2;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DownloadService downloadService;
    //用于更新进度的Handler
    private Handler mHandler = new Handler();
    private ProgressBar progressBar;
    private TextView textStatus;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadService = ((DownloadService.DownloadBinder) service).getService();
        }

    };

    /**
     * 启动下载的服务，并进行绑定
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startDownload = findViewById(R.id.start_download);
        Button pauseDownload = findViewById(R.id.pause_download);
        Button cancelDownload = findViewById(R.id.cancel_download);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        textStatus = findViewById(R.id.textStatus);
        //设置进度条的最大值
        progressBar.setMax(100);

        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        Log.e("MainActivity", "onCreate");
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * 处理监听器的点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (downloadService == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.start_download:
                String url = "https://one.xuanjis.com/%E8%B5%84%E6%BA%90/snipaste/Snipaste-1.16.2-x64.zip";
                downloadService.startDownload(url);
                //开始Handler循环
                mHandler.postDelayed(mRunnable, 200);
                break;
            case R.id.pause_download:
                downloadService.pauseDownload();
                textStatus.setText("Downloat task is paused!");
                mHandler.removeCallbacks(mRunnable);
                break;
            case R.id.cancel_download:
                downloadService.cancelDownload();
                progressBar.setProgress(0);
                textStatus.setText("Downloat task is canceld!");
                mHandler.removeCallbacks(mRunnable);
                break;
            default:
                break;
        }
    }

    /**
     * 权限获取的回调函数，如果没有授权，会显示相关提示
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //没有授予权限
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity销毁时移除，并置空，防止内存泄露
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        Log.e("MainActivity", "onDestroy");
        unbindService(connection);
    }

    /**
     * 返回键的点击事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Runnable消息用于更新进度条显示，单击"开始下载"按钮时首次将Runnable消息压入消息队列
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //获取百分的下载进度
            int currentProgress = downloadService.getCurrentDownloadProgress();
            //获取下载状态信息
            int currentStatus = downloadService.getCurrentStatus();

            /*******************Begin****************/
            //根据下载的状态和进度更新进度条显示和textStatus文字显示
            //能进入run方法的下载状态只会有TYPE_SUCCESS(0)，TYPE_FAILED(1)和TYPE_DOWLOADING(4)
            //TYPE_SUCCESS(0)，TYPE_FAILED(1)时不再定时发送Runnable对象到消息队列
            //只有TYPE_DOWLOADING(4)时需要定时将Runnable对象压入消息队列

//            downloadService.startForeground(1, downloadService.getNotification("Downloading...", 0));

            progressBar.setProgress(currentProgress);
            switch (currentStatus) {
                case DownloadTask.TYPE_SUCCESS:
                    textStatus.setText("成功");
                    Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    break;
                case DownloadTask.TYPE_PAUSED:
                    textStatus.setText("暂停");
                    Toast.makeText(MainActivity.this, "停止中", Toast.LENGTH_SHORT).show();
                    break;
                case DownloadTask.TYPE_FAILED:
                    textStatus.setText("失败");
                    Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    break;
                case DownloadTask.TYPE_DOWLOADING:
                    textStatus.setText("下载中:"+currentProgress);

                    Toast.makeText(MainActivity.this, "下载中", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 200);
                    break;
                case DownloadTask.TYPE_CANCELED:
                    textStatus.setText("取消");
                    Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                    break;
            }

            mHandler.postDelayed(mRunnable, 200);



            /******************End******************/
        }
    };
}
