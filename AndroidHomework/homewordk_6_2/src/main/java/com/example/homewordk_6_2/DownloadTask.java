package com.example.homewordk_6_2;


import android.os.AsyncTask;
import android.util.Log;

import com.example.homewordk_6_2.ok.DownloadListener;
import com.example.homewordk_6_2.ok.TaskInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<Void, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    public static final int TYPE_DOWLOADING = 4;

    private DownloadListener listener;
    private TaskInfo taskInfo;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    /**
     * 构造函数
     *
     * @param listener
     * @param info
     */
    public DownloadTask(DownloadListener listener, TaskInfo info) {
        this.listener = listener;
        this.taskInfo = info;
    }

    /**
     * 在后台进行下载的任务放在里面
     * @param params
     * @return
     */
    @Override
    protected Integer doInBackground(Void... params) {
        Log.i("haha", "进入doInBacground...");
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            // 记录已下载的文件长度
            long downloadedLength = 0;
            String downloadUrl = taskInfo.getUrl();
            //String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            //String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String fileName = taskInfo.getName();
            String directory = taskInfo.getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLen(downloadUrl);
            taskInfo.setContentLen(contentLength);
//            if (contentLength == 0) {
//                return TYPE_FAILED;
//            } else
            if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等，说明已经下载完成了
                return TYPE_SUCCESS;
            }
            /**
             * 开始注释
             */

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            /**
             * 结束注释
             */
            if (response != null) {
                Log.i("haha", "response不为null");
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                // 跳过已下载的字节
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {

                        total += len;
                        savedFile.write(b, 0, len);
                        taskInfo.setCompletedLen(total + downloadedLength);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        Log.i("haha", "下载了" + progress);
                        if (progress > 1) {
                            Log.e("DownloadTask:", progress + "");
                        }
                        // publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            } else {
                Log.i("haha", "response为null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;

    }

    /**
     * 更新下载任务的进度信息
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            taskInfo.setStatus(TYPE_DOWLOADING);
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 根据doInBackground返回的状态标志更新TaskInfo,
     * 并调用DownloadListener中相关方法发送通知或者显示Toast
     *
     * @param status 状态
     */
    @Override
    protected void onPostExecute(Integer status) {
        taskInfo.setStatus(status);
        switch (status) {
            /************Begin************/

            case TYPE_DOWLOADING:
                listener.onProgress(0);
                break;

            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_SUCCESS:
                listener.onSuccess();
                /***********End**************/
        }
    }

    /**
     * pause标记
     */
    public void pauseDownload() {
        isPaused = true;
    }

    /**
     * 取消标记
     */
    public void cancelDownload() {
        isCanceled = true;
    }


    /**
     * 获取下载的文件的长度
     *
     * @param downloadUrl 要下载文件的URL
     * @return 文件的长度
     * @throws IOException
     */
    private long getContentLen(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {

            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}
