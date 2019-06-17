package com.example.home_music;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements View.OnClickListener ,
OnScrollChangeListener{

    private List<Map<String, String>> data;
    private MediaPlayer mMediaPlayer=new MediaPlayer();
    private int playIndex;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mProgressBar.setProgress(mMediaPlayer.getCurrentPosition()*100/mMediaPlayer.getDuration());
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                case 2:

                    default:
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button play = findViewById(R.id.play),
                pause = findViewById(R.id.pause),
                pre = findViewById(R.id.pre),
                next = findViewById(R.id.next);
        mTextView = findViewById(R.id.text);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mProgressBar.setOnScrollChangeListener(this);
        data = getData();
        playIndex=0;
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        else {
            initMediaPlayer();
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.reset();
                playIndex=(playIndex+1)%3;
                initMediaPlayer();
                mMediaPlayer.start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }
                else {
                    Toast.makeText(MainActivity.this, "权限禁止", Toast.LENGTH_SHORT).show();
                }
                default:
        }
    }

    private void initMediaPlayer(){
        Map<String,String> song=data.get(playIndex);
        super.onResume();
        try {
            Log.i("xuanqisnow", song.get("path"));

            mMediaPlayer.setDataSource(song.get("path"));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTextView.setText("名字:"+song.get("name")+"\n"+"作者:"+song.get("author")+"\n"+"时间:"+mMediaPlayer.getDuration());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!mMediaPlayer.isPlaying()){
                    mMediaPlayer.start();
                    mHandler.sendEmptyMessageDelayed(1, 0);
                }

                break;
            case R.id.pause:
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mHandler.removeMessages(1);
                }
                break;
            case R.id.pre:
                playIndex=(playIndex+2)%3;
                mMediaPlayer.reset();
                initMediaPlayer();
                mMediaPlayer.start();
                break;
            case R.id.next:
                playIndex=(playIndex+1)%3;
                mMediaPlayer.reset();
                initMediaPlayer();
                mMediaPlayer.start();
                default:

        }
    }

    /**
     * 得到关于文件地址作者的信息
     * @return
     */
    private List<Map<String, String>> getData(){
        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String name = ""+i;
            String author = i+"的作者";
            String path = new File(Environment.getExternalStorageDirectory(), i+".mp3").getPath();
            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("author", author);
            map.put("path", path);
            data.add(map);
        }
        return data;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        mMediaPlayer.seekTo();
    }
}
