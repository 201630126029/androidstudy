package com.example.app11;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuanqis
 */
public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView mPositionText;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLocale = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //地图初始化，要在设置视图之前
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //创建定位客户类，并且为他注册一个监听器，当定位信息获取到了会回调监听器的方法
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //开始用来显示经纬度的view
        mPositionText = findViewById(R.id.position_text_view);
        //权限的集合
        List<String> permissionList = new ArrayList<>();

        //地图的视图，地图的操作的地图
        mMapView = findViewById(R.id.bdmapView);
        mBaiduMap = mMapView.getMap();

        //设置显示自己的位置
        mBaiduMap.setMyLocationEnabled(true);

        //申请权限，先加到集合里面，然后再转换成数组，一块获取
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissions, 1);
        } else {
            requestLocation();
        }

    }

    private void requestLocation() {
        //对定位进行初始化，开始服务定位，注意百度地图的定位服务是在单独的进程中的
        initLocation();
        mLocationClient.start();
    }

    /**
     * 根据定位的地址来更新地图视图
     * @param location 定位信息
     */
    private void navigateTo(BDLocation location) {
        //只有第一次才会定位
        if (isFirstLocale) {
            //经纬度设置的类
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //更新的类
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            //跳到对应的地方
            mBaiduMap.animateMapStatus(update);
            //设置19f，可以设置的范围是3-19
            update = MapStatusUpdateFactory.zoomTo(19f);
            //跳到19f
            mBaiduMap.animateMapStatus(update);
            isFirstLocale = false;

            //个人位置的显示，在地图上显示小圆点
            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
            locationBuilder.latitude(location.getLatitude());
            locationBuilder.longitude(location.getLongitude());
            MyLocationData locationData = locationBuilder.build();
            mBaiduMap.setMyLocationData(locationData);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this,
                                    "权限禁止将无法正常使用", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(MainActivity.this,
                            "发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 对定位的参数进行配置
     */
    private void initLocation() {
        //用于配置参数的类
        LocationClientOption option = new LocationClientOption();
        //添加这个才能准确，不然很不准
        option.setCoorType("bd09ll");
        //设置每5s自动的刷新，进行定位
        option.setScanSpan(5000);
        //需要详细的地址信息，包括省市县信息等
        option.setIsNeedAddress(true);
        //设置模式为只使用设备进行定位
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //将属性类设置为客户的属性
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位并销毁地图视图
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
                    //StringBuild是单线程模式下使用，StringBuffer多线程使用
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
                    currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
                    currentPosition.append("省份：").append(bdLocation.getProvince()).append("\n");
                    currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
                    currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
                    currentPosition.append("定位方式：");
                    if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS定位");
                    } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }
                    mPositionText.setText(currentPosition);
                }
            });
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }
}
