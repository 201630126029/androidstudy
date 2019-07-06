package com.example.app14.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 处理网络请求的类
 * @author xuanqis
 */
public class HttpUtil {
    /**
     * 访问服务器，获取数据
     * @param address 网址
     * @param callback 回调的对象
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
