package com.example.app14;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app14.db.City;
import com.example.app14.db.County;
import com.example.app14.db.Province;
import com.example.app14.gson.Weather;
import com.example.app14.util.HttpUtil;
import com.example.app14.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 选择省、市、县的碎片
 */
public class ChooseSreaFragment extends Fragment {
    /**
     * 当前的选项选的是什么级别
     */
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    /**
     * 标题，返回按钮，具体的选项
     */
    private TextView titleView;
    private Button backButton;
    private ListView mListView;
    /**
     * listView的适配和数据
     */
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表,市列表，县列表 数据
     */
    private List<Province> mProvinces;
    private List<City> mCities;
    private List<County> mCounties;

    /**
     * 选中的省，市
     */
    private Province mSelectedProvince;
    private City mSelectedCity;
    /**
     * 当前在选什么级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("xuanqis", "onCreateView");

        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleView = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        mListView = view.findViewById(R.id.list_view);
        //首先直接将数据设为空进行显示
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    //设为选中
                    mSelectedProvince = mProvinces.get(position);
                    //查询该省的所有市数据并进行显示
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    //设为选中
                    mSelectedCity = mCities.get(position);
                    //查询该市的所有数据并进行显示
                    queryCounties();
                }
                else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = mCounties.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.mDrawerLayout.closeDrawers();
                        activity.mSwipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    //将数据变回市的数据
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    //变为省级的数据进行选择
                    queryProvinces();
                }

            }
        });
        //第一次选择选泽一个省
        queryProvinces();
    }


    /**
     * 从数据库或服务器中得到需要显示的省，进行显示
     */
    private void queryProvinces() {
        titleView.setText("中国");
        //此处没有返回
        backButton.setVisibility(View.GONE);
        //查找所有的省
        mProvinces = DataSupport.findAll(Province.class);
        //数据库里没有数据
        if (mProvinces.size() > 0) {
            //前面的数据需要清空
            dataList.clear();
            for (Province province : mProvinces) {
                //加入的是String
                dataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            //设置第一个选中
            mListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {

            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 从数据库或服务器中得到需要显示的市，进行显示，省份是全局的SelectedProvince
     */
    private void queryCities() {
        titleView.setText(mSelectedProvince.getProvinceName());
        //这个时候有返回了
        backButton.setVisibility(View.VISIBLE);
        //得到数据
        mCities = DataSupport.where("provinceid = ?", String.valueOf(mSelectedProvince.getId())).find(City.class);
        //没有对应的城市
        if (mCities.size() > 0) {
            dataList.clear();
            for (City city : mCities) {
                dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 跟市一样的
     */
    private void queryCounties() {
        titleView.setText(mSelectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);/**
         * 从数据库或服务器中得到需要显示的省，进行显示
         */
        mCounties = DataSupport.where("cityid = ?", String.valueOf(mSelectedCity.getId())).find(County.class);
        if (mCounties.size() > 0) {
            dataList.clear();
            for (County county : mCounties) {
                dataList.add(county.getCcountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 数据库里没数据，通过这个进行查询并存储在数据库里
     *
     * @param address 数据的地址， 数据的级别，因为查询完毕后要重新去进行显示使用
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        //弹出框框
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    //进行存储，并得到存储的结果
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, mSelectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, mSelectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                //存储成功后要重新进行查询显示
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                //存储成功后要重新进行查询显示
                                queryCities();
                            } else if ("county".equals(type)) {
                                //存储成功后要重新进行查询显示
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示正在加载弹出框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载...");
            //设置点击框外不取消，只有点击返回才取消
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭弹出框
     */
    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
