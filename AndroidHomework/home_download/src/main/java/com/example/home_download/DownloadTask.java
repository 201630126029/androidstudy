package com.example.home_download;


import android.os.AsyncTask;
import android.util.Log;

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
    private volatile boolean isCanceled = false;
    private volatile boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener, TaskInfo info) {
        this.listener = listener;
        this.taskInfo = info;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            // 记录已下载的文件长度
            long downloadedLength = 0;
            String downloadUrl = taskInfo.getUrl();
            String fileName = taskInfo.getName();
            String directory = taskInfo.getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLen(downloadUrl);
            taskInfo.setContentLen(contentLength);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等，说明已经下载完成了
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                // 跳过已下载的字节
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    Log.i("xuanqisnow", isPaused+"");
                    if (isCanceled) {
                        response.body().close();
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        response.body().close();
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        taskInfo.setCompletedLen(total + downloadedLength);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        if (progress > 1) {
                            Log.e("DownloadTask:", progress + "");
                        }
                         publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
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

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            taskInfo.setStatus(TYPE_DOWLOADING);
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            /************Begin************
             * 这里不会处理正在下载的情况
             */

            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:


                /***********End**************/
        }
    }

    public void pauseDownload() {
        Log.i("xuanqisnow", "执行到了pausedown");
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }


    /**
     * 得到要下载的文件的长度
     *
     * @param downloadUrl url
     * @return
     * @throws IOException
     */
    private long getContentLen(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try{
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {

                long contentLength = response.body().contentLength();
                response.close();
                return contentLength;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}
