package com.example.app1.part1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.UrlQuerySanitizer;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    LruCache<String, Bitmap> mImageCache;

    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime
    .getRuntime().availableProcessors());

    private Handler mUiHandler =  new Handler(Looper.getMainLooper());

    public ImageLoader(){
        initImageCache();
    }

    /**
     * 初始化Lru缓存，设置缓存大小和计算规则
     */
    private void initImageCache(){
        //最大可用内存
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        //取四分之一作为缓存
        final int cacheSize = maxMemory/4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

    /**
     * 为Image设置一个url对应的图片，另外还使用了LRU缓存
     * @param url
     * @param imageView
     */
    public void displayImage(final String url, final ImageView imageView){
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap==null){
                    return ;
                }
                if (imageView.getTag().equals(url)){
                    updateImageView(imageView, bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    /**
     * 为ImageView设置Bitmap
     * @param imageView
     * @param bmp
     */
    private void updateImageView(final ImageView  imageView, final Bitmap bmp){
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bmp);
            }
        });
    }

    /**
     * 根据位图的URL，访问网络，返回对应的位图
     * @param imageUrl 位图的URL
     * @return 对应的位图
     */
    public Bitmap downloadImage(final String imageUrl){
        Bitmap bitmap = null;
        try{
            URL url=new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bitmap= BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
