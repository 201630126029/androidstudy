package com.example.app13;

public interface HttpCallbackListener {
    /**
     * 服务器成功响应调用
     * @param response 返回的结果
     */
    void onFinish(String response);

    /**
     * 网络操作发生错误的调用
     * @param e 错误
     */
    void onError(Exception e);
}
