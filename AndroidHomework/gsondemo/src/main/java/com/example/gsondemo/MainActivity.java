package com.example.gsondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private TextView tvWeatherCity,tvWeather,tvWeatherTemp,tvImg1,tvImg2;
    private String weatherinfo="{\"city\":\"北京\",\"cityid\":\"101010100\"," +
            "\"temp1\":\"-2℃\",\"temp2\":\"16℃\",\"weather\":\"晴\"," +
            "\"img1\":\"n0.gif\",\"img2\":\"d0.gif\",\"ptime\":\"18:00\"}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        init();
        //JSON格式解析
        Gson gson=new Gson();
        Weather weather = gson.fromJson(weatherinfo, Weather.class);
        //设置到对应的控件上显示天气信息
        setWeatherData(weather);
    }
    public void init()
    {
        tvWeatherCity=(TextView)findViewById(R.id.weatherCity);
        tvWeather=(TextView)findViewById(R.id.weather);
        tvWeatherTemp=(TextView)findViewById(R.id.weatherTemp);
        tvImg1=(TextView)findViewById(R.id.img1);
        tvImg2=(TextView)findViewById(R.id.img2);
    }
    public void setWeatherData(Weather weather)
    {
        tvWeatherCity.setText(weather.getCity());
        tvWeather.setText(weather.getWeather());
        tvWeatherTemp.setText(weather.getTemp1()+"-"+ weather.getTemp2());
        tvImg1.setText(weather.getImg1());
        tvImg2.setText(weather.getImg2());
    }
}
