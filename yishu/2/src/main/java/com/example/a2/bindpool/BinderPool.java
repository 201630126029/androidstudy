package com.example.a2.bindpool;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * @author xuanqis
 * 单例模式，设为volatile，防止指令重排序
 * 具有服务端BinderPoolImpl的代理
 *
 */
public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BIND_NONE = -1;
    public static final int BIND_COMPUT = 0;
    public static final int BIND_SECURITY_CENTER = 1;

    /**
     * 客户端的ApplicationContext
     */
    private Context mContext;

    /**
     * 客户端的代理BinderPool的Proxy，封装在BinderPool类里面
     */
    private IBinderPool mBinderPool;
    /**
     * 单例，懒加载模式的加载，这里设置为volatile防止指令重排序
     */
    private static volatile BinderPool sInstance;
    /**
     * 用来控制？？？这里为什么要这个
     */
    private CountDownLatch mconnectBinderPoolCountDownLatch;

    /**
     * 对于每个进程，有唯一的一个服务端BinderPoolImpl的代理，注意这里单例的多线程处理
     * @param context 活动的上下文，如果当前进程没有Binder池的代理的话，就利用这个上下文进行创建
     * @return BinderPool类的单例
     */
    public static BinderPool getInstance(Context context){
        //处理多线程的问题，首先判断是不是null，是的话就获取锁，再判断是不是null，再创建单例
        if (sInstance==null){
            synchronized (BinderPool.class){
                if (sInstance==null){
                    sInstance=new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * 一个活动创建Binder池就会直接将活动所在的Application与服务连接起来
     * @param context 活动上下文
     */
    private BinderPool(Context context){
        mContext=context.getApplicationContext();
        connectBinderPoolService();
    }

    /**
     * 将活动的应用和服务进行绑定，执行之后就有BinderPool的代理了，客户端
     */
    private synchronized void connectBinderPoolService(){
        mconnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try{
            //等待绑定完成，才执行结束
            mconnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *BinderPool的实现类，运行在服务端，静态内部类，只会有一个
     */
    public static class BinderPoolImpl extends IBinderPool.Stub{
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            Binder binder = null;
            switch (binderCode) {
                case BIND_SECURITY_CENTER:
                    binder=new SecurityCenterImpl();
                    break;
                case BIND_COMPUT:
                    binder=new ComputImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

    /**
     * 连接类，其实是将服务器的Binder封装到了客户端的BinderPool里面
     */
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mconnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 失败重连，运行在线程池里面
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "bind died...");
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBinderPool=null;
            connectBinderPoolService();
        }
    };

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try{
            if (mBinderPool != null){
                binder=mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

}
