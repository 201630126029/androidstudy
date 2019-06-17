package com.example.home_download;

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
    private boolean isFirstIn=true;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadService = ((DownloadService.DownloadBinder) service).getService();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startDownload = findViewById(R.id.start_download);
        Button pauseDownload = findViewById(R.id.pause_download);
        Button cancelDownload = findViewById(R.id.cancel_download);
        progressBar = findViewById(R.id.progressBar);
        textStatus = findViewById(R.id.textStatus);
        progressBar.setMax(100);//设置进度条的最大值

        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent); // 启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        Log.e("MainActivity", "onCreate");
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (downloadService == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.start_download:
                progressBar.setProgress(0);
                String url = "https://one.xuanjis.com/%E8%B5%84%E6%BA%90/Android%E4%B9%A6/%E6%99%BA%E8%83%BD%E7%BB%88%E7%AB%AF%E5%BA%94%E7%94%A8%E5%8F%82%E8%80%83%E4%B9%A6.zip";
                downloadService.startDownload(url);
                mHandler.postDelayed(mRunnable, 200);//开始Handler循环
                break;
            case R.id.pause_download:
                downloadService.pauseDownload();
                textStatus.setText("下载暂停");
                mHandler.removeCallbacks(mRunnable);
                break;
            case R.id.cancel_download:
                downloadService.cancelDownload();
                progressBar.setProgress(0);
                textStatus.setText("下载取消");
                mHandler.removeCallbacks(mRunnable);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
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

    //返回键的点击事件
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
            //获取下载进度
            int currentProgress = downloadService.getCurrentDownloadProgress();
            //获取下载状态信息
            int currentStatus = downloadService.getCurrentStatus();

            /*******************Begin****************/
            //根据下载的状态和进度更新进度条显示和textStatus文字显示
            //能进入run方法的下载状态只会有TYPE_SUCCESS(0)，TYPE_FAILED(1)和TYPE_DOWLOADING(4)
            //TYPE_SUCCESS(0)，TYPE_FAILED(1)时不再定时发送Runnable对象到消息队列
            //只有TYPE_DOWLOADING(4)时需要定时将Runnable对象压入消息队列

            switch (currentStatus) {
                case DownloadTask.TYPE_SUCCESS:
                    textStatus.setText("下载成功");
                    break;
                case DownloadTask.TYPE_FAILED:
                    textStatus.setText("下载失败");
                    break;
                case DownloadTask.TYPE_PAUSED:
                    textStatus.setText("下载暂停");
                    break;
                case DownloadTask.TYPE_DOWLOADING:
                    progressBar.setProgress(currentProgress);
                    textStatus.setText("正在下载");
                    mHandler.postDelayed(mRunnable, 200);

                    break;
                default:
            }


            /******************End******************/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        //不是新创建的第一次
        if(!isFirstIn){
            mHandler.post(mRunnable);
        }
        isFirstIn=false;
    }
}
