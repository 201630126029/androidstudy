package com.example.hefeng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainActivity extends AppCompatActivity {

    private  TextView show;
    private static final String TAG = "xuanqis";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = findViewById(R.id.show);

        HeWeather.getWeatherNow(MainActivity.this, "CN101250302", Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                show.setText(new Gson().toJson(dataObject).toString());
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }
}
