package com.example.app14;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.mtp.MtpStorageInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.app14.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author xuanqis
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //如果原先用过，那么就直接显示这个城市
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("weather", null) != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            String url = "https://free-api.heweather.net/s6/weather/now?location=beijing" +
                    "&key=a6f31634438a4dbc970c7b43bb77c353";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("xuanqis", "error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("xuanqis", response.body().string());
                }
            });
            finish();
        }
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
//        != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case 1:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "权限被禁止", Toast.LENGTH_LONG).show();
//                }
//                break;
//                default:
//                    break;
//        }
//    }
}
