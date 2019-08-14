package com.example.app1.part1.single;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 专门执行图片缓存的功能
 * @author xuanqis
 */
public class ImageCache {
    LruCache<String, Bitmap> mImageCache;
    public ImageCache(){
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

    public void put(final String url, final Bitmap bitmap){
        mImageCache.put(url, bitmap);
    }

    public Bitmap get(String url){
        return mImageCache.get(url);
    }
}
