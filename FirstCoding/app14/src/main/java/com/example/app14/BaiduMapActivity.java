package com.example.app14;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.app14.gson.Weather;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends AppCompatActivity {
        public LocationClient mLocationClient;
        private MapView mMapView;
        private BaiduMap mBaiduMap;
        private boolean isFirstLocale=true;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SDKInitializer.initialize(getApplicationContext());
            setContentView(R.layout.activity_baidu_map);
            mLocationClient = new LocationClient(getApplicationContext());
            mLocationClient.registerLocationListener(new MyLocationListener());
            List<String> permissionList = new ArrayList<>();

            mMapView=findViewById(R.id.bdmapView);
            mBaiduMap=mMapView.getMap();
            mBaiduMap.setMyLocationEnabled(true);
            if (ContextCompat.checkSelfPermission(BaiduMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(BaiduMapActivity.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(BaiduMapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(BaiduMapActivity.this, permissions, 1);
            }
            else {
                requestLocation();
            }

        }

        private void requestLocation(){
            initLocation();
            mLocationClient.start();
        }

        private void navigateTo(BDLocation location){
            if(isFirstLocale){
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                update=MapStatusUpdateFactory.zoomTo(19f);
                mBaiduMap.animateMapStatus(update);
                isFirstLocale=false;

                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(location.getLatitude());
                locationBuilder.longitude(location.getLongitude());
                MyLocationData locationData = locationBuilder.build();
                mBaiduMap.setMyLocationData(locationData);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case 1:
                    if(grantResults.length > 0){
                        for (int grantResult : grantResults) {
                            if(grantResult != PackageManager.PERMISSION_GRANTED){
                                Toast.makeText(BaiduMapActivity.this, "权限禁止将无法正常使用", Toast.LENGTH_LONG).show();
                                finish();
                                return;
                            }
                        }
                        requestLocation();
                    }
                    else {
                        Toast.makeText(BaiduMapActivity.this, "发生未知错误", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }

        private void initLocation(){
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");
            option.setScanSpan(5000);
            option.setIsNeedAddress(true);
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
            mLocationClient.setLocOption(option);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mLocationClient.stop();
            mMapView.onDestroy();
            mBaiduMap.setMyLocationEnabled(false);
        }

        class MyLocationListener implements BDLocationListener {

            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaiduMapActivity.this);
                        sharedPreferences.edit().clear().apply();
                        Intent intent = new Intent(BaiduMapActivity.this, WeatherActivity.class);
                        //百度地图得不到县级的城市代码，只能得到岳麓区的代码，需要进行转换
                        intent.putExtra("weather_id", "CN101250302");

                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }
