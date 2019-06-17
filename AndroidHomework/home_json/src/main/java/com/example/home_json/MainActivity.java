package com.example.home_json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private Spinner sp;
    private Spinner ssp;

    private List<CityCode> list;
    private List<JSONObject> mJSONObjects;
    private TextView tvWeatherCity,tvWeather,tvWeatherTemp,tvImg1,tvImg2;
    private String weatherinfo="{\"city\":\"北京\",\"cityid\":\"101010100\"," +
            "\"temp1\":\"-2℃\",\"temp2\":\"16℃\",\"weather\":\"晴\"," +
            "\"img1\":\"n0.gif\",\"img2\":\"d0.gif\",\"ptime\":\"18:00\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.cityCode);
        sp = findViewById(R.id.cityList);
        ssp=findViewById(R.id.shengList);

        ReadJsonTask mytask = new ReadJsonTask();
        mytask.execute();

    }

    class ReadJsonTask extends AsyncTask {
        @Override
        protected String doInBackground(Object[] params) {
            /**
             * 返回读取的整个json文件的
             */
            try {
                InputStream in = MainActivity.this.getResources().getAssets().open("citycode.json");
                //获取文件的字节数
                int length = in.available();
                //创建byte数组
                byte[] buffer = new byte[length];
                //将文件中的数据读到byte数组中
                in.read(buffer);
                String line = new String(buffer);
                return line;
//                result = convertToBean(line);
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * 将得到的json数据转换成对象的List
         *
         * @param json 完整的json数据
         * @return 城市信息的List
         */
        public List<CityCode> convertToBean(String json) {
            List<CityCode> result = new ArrayList<CityCode>();
            try {
                JSONObject obj = new JSONObject(json);
                //得到省的数组
                JSONArray jsons = obj.getJSONArray("城市代码");
                for (int n = 0; n < jsons.length(); n++) {
                    JSONObject sheng = jsons.getJSONObject(n);
                    JSONArray shi = sheng.getJSONArray("市");
                    for (int i = 0; i < shi.length(); i++) {
                        JSONObject rss = shi.optJSONObject(i);
                        CityCode m = new CityCode();
                        m.setCode(rss.getString("编码"));
                        m.setCity(rss.getString("市名"));
                        result.add(m);
                    }
                }
            } catch (JSONException e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            setSheng(getSheng(result.toString()));
        }

        /**
         *
         * @param json
         * @return
         */
        public String[] getSheng(String json) {
            List<String> result = new ArrayList<>();
            mJSONObjects=new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(json);
                //得到省的数组
                JSONArray jsons = obj.getJSONArray("城市代码");
                for (int n = 0; n < jsons.length(); n++) {
                    JSONObject sheng = jsons.getJSONObject(n);
                    String shengming = sheng.getString("省");
                    result.add(shengming);
                    mJSONObjects.add(sheng);
                }
            } catch (JSONException e) {
            }
            return result.toArray(new String[result.size()]);
        }

        public List<CityCode> getShi(JSONObject sheng) {
            List<CityCode> result = new ArrayList<>();
            try {
                JSONArray shi = sheng.getJSONArray("市");
                for (int i = 0; i < shi.length(); i++) {
                    JSONObject rss = shi.optJSONObject(i);
                    CityCode m = new CityCode();
                    m.setCode(rss.getString("编码"));
                    m.setCity(rss.getString("市名"));
                    result.add(m);
                }
            } catch (JSONException e) {
            }
            return result;
        }

        public void setShi(List<CityCode> result){
            list =  result;
            String[] cities = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                cities[i] = list.get(i).getCity();
            }
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, cities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(adapter);

            AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                           long arg3) {
                    CityCode c = list.get(arg2);
                    String id = c.getCode();
                    tv.setText(id);
                    //初始化
                    init();

                    url = "http://www.weather.com.cn/data/cityinfo/";
                    url += c.getCode()+".html";
                    new MyTask().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            };
            sp.setOnItemSelectedListener(listener);
        }

        public void setSheng(String[] sheng){

            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, sheng);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ssp.setAdapter(adapter);

            AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                           long arg3) {
                    JSONObject json = mJSONObjects.get(arg2);
                    setShi(getShi(json));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            };
            ssp.setOnItemSelectedListener(listener);
        }
    }

    public void init()
    {
        tvWeatherCity= findViewById(R.id.weatherCity);
        tvWeather= findViewById(R.id.weather);
        tvWeatherTemp= findViewById(R.id.weatherTemp);
        tvImg1= findViewById(R.id.img1);
        tvImg2= findViewById(R.id.img2);
    }
    public void setWeatherData(Weather weather)
    {
        Log.i("xuanqisnowcity", weather.toString());
        tvWeatherCity.setText(weather.getCity());
//        Log.i("xuanqisnowweeather", weather.getWeather());
        tvWeather.setText(weather.getWeather());
        tvWeatherTemp.setText(weather.getTemp1()+"-"+ weather.getTemp2());
        tvImg1.setText(weather.getImg1());
        tvImg2.setText(weather.getImg2());
    }



    private String url;



    class MyTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url).build();
            try {
                Response response = client.newCall(request).execute();
                weatherinfo=response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Gson gson=new Gson();
            Log.i("xuanqisnow", weatherinfo);

            try {
                JSONObject object = new JSONObject(weatherinfo);

                weatherinfo=object.getString("weatherinfo");

                Weather weather = gson.fromJson(weatherinfo, Weather.class);
                //设置到对应的控件上显示天气信息
                setWeatherData(weather);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
