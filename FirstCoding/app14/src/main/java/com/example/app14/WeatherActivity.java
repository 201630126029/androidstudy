package com.example.app14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app14.gson.Forecast;
import com.example.app14.gson.Weather;
import com.example.app14.service.AutoUpdateService;
import com.example.app14.util.HttpUtil;
import com.example.app14.util.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 具体的天气的活动，
 *
 * @author xuanqis
 */
public class WeatherActivity extends AppCompatActivity {
    /**
     * 最外层的滚动条
     */
    private ScrollView weatherLayout;

    /**
     * 最上面显示的显示的天气所在城市
     */
    private TextView titleCity;

    /**
     * 天气的获取时间
     */
    private TextView titleUpdateTime;


    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout mSwipeRefresh;
    /**
     * 当前的天气id
     */
    private String mWeatherId;

    public DrawerLayout mDrawerLayout;

    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置占据状态栏
        fitSystem();

        setContentView(R.layout.activity_weather);

        //初始控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navButton = findViewById(R.id.nav_button);

        //设置滑动窗口打开和关闭的监听，设置占据状态栏
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                Log.i(MyApplication.AppTag, "打开滑动窗口");
                if (Build.VERSION.SDK_INT >= 21) {

                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                fitSystem();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        //设置点击右上角打开滑动窗口
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        //得到县的weatherid，发起请求
        mWeatherId = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(mWeatherId);

        //刷新就是根据城市的weatherid来发起请求，更新天气
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        //如果已经有图片，那么就直接加载，在应用打开的时候，是会直接清空sharedpreference，将会更新图片
        String bingPic = sharedPreferences.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 进行UI的设置
     *
     * @param weather 需要设置的天气对象
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        //得到具体的时间
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "C";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        //去掉所有的View
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView minText = view.findViewById(R.id.min_text);
            TextView maxText = view.findViewById(R.id.max_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        //显示天气信息完成，再启动更新的服务
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }


    /**
     * 根据城市的weatherid，向服务器发起请求，并进行显示
     *
     * @param weatherId 城市的weatherid
     */
    public void requestWeather(final String weatherId) {
        //请求的网址
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=a6f31634438a4dbc970c7b43bb77c353";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取城市天气信息失败",
                                Toast.LENGTH_LONG).show();
                        mSwipeRefresh.setRefreshing(false);
                    }

                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //得到城市数据，解析成对应的类
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,
                                    "获取天气信息失败：" + responseText, Toast.LENGTH_LONG).show();
                        }
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 向服务器发起请求，把图片存入sharedpreference并设置为背景
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        WeatherActivity.this
                ).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 占据状态栏，将状态栏设置为透明，美化界面效果
     */
    private void fitSystem() {
        if (Build.VERSION.SDK_INT >= 21) {

            //decorView是包括标题在内的window的子控件
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            //设置状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}

