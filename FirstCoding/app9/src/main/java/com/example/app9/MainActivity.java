package com.example.app9;

import android.app.Notification;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.sendRequest);
        mTextView = findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendRequest:
                sendRequestWithOkHttp();
            default:
        }
    }

    /**
     * 访问网络
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader br = null;
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://one.xuanjis.com/getdata.json")
                            .addHeader("Connection", "keep-alive")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJsonWithGson(responseData);

//                    okhttp提供了回调，其中因为访问网络不能在主线程中，这里的enqueue是直接新开线程中执行网络请求
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                        }
//                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    /**
     * 使用org的json进行转换
     * @param jsonData 网络返回的数据
     */
    private void parseJsonWithJsonObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.i("xuanqisnow", "id is " + id);

                Log.i("xuanqisnow", "name is " + name);

                Log.i("xuanqisnow", "version is " + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用gson包来进行转化，超级方便
     * @param jsonData
     */
    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();
        List<App> apps = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());
        for (App app : apps) {
            Log.i("xuanqisnow", app.getId());
            Log.i("xuanqisnow", app.getName());
            Log.i("xuanqisnow", app.getVersion());
        }
    }
}
