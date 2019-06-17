package com.example.homewordk_6_2.ok;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;


/**
 * 有下载的文件的信息
 */
public class TaskInfo {
    /**
     * 下载成功、失败、暂停、取消、正在下载的状态，0,1,2,3,4
     */
    private int status=-1;

    /**
     * path是文件所在的文件夹的路径
     * contenlen：要下载的文件的总长度
     * completedLen：文件的已经下载的长度
     *
     */
    private String name;
    private String path;
    private String url;
    private long contentLen;
    private volatile long completedLen;
    /**
     * 迄今为止java虚拟机都是以32位作为原子操作，而long与double为64位，当某线程
     * 将long/double类型变量读到寄存器时需要两次32位的操作，如果在第一次32位操作
     * 时变量值改变，其结果会发生错误，简而言之，long/double是非线程安全的，volatile
     * 关键字修饰的long/double的get/set方法具有原子性。
     */


    public TaskInfo(String name, String path, String url) {
        this.name = name;
        this.path = path;
        this.url = url;
    }
    public TaskInfo(String url) {
        this.url = url;
        this.name = url.substring(url.lastIndexOf("/"));
        path = Environment.getExternalStorageDirectory()+ "/Music/";

        File file = new File(path + name);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.completedLen = file.length();
        Log.e("Sevice:completedLen",this.completedLen+"");
        //this.completedLen = 0;
        this.contentLen =0 ;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public long getContentLen() {
        return contentLen;
    }

    public long getCompletedLen() {
        return completedLen;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }

    public void setCompletedLen(long completedLen) {
        this.completedLen = completedLen;
    }
}
