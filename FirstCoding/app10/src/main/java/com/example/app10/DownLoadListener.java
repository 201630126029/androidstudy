package com.example.app10;

/**
 * 状态的回调函数
 */
public interface DownLoadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
