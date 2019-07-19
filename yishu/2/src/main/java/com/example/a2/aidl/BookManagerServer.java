package com.example.a2.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author xuanqis
 * 服务端服务，进行书籍的管理，可以取消注册监听器
 */
public class BookManagerServer extends Service {

    private static final String TAG = "BookManagerServer";

    /**
     * 判断服务是否已经死亡的原子变量，用在添加书籍的线程
     */
    private AtomicBoolean isServiceDestroy = new AtomicBoolean(false);
    /**
     * 书籍的列表，这里虽然是CopyOnWriteArrayList，但是在传递到客户端的过程中会转化为ArrayList
     * CopyOnWriteArrayList不是ArrayList的子类
     */
    private CopyOnWriteArrayList<Book> mBookLists = new CopyOnWriteArrayList<>();
    /**
     * 管理监听器的表，可以根据binder来对应监听器，同一个binder在里面只会有一个监听器
     */
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners=
            new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mBookLists;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookLists.add(book);
        }

        @Override
        public void registerListener(com.example.a2.aidl.IOnNewBookArrivedListener listener)
                throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unRegisterListener(com.example.a2.aidl.IOnNewBookArrivedListener listener)
                throws RemoteException {
            mListeners.unregister(listener);
        }
    };
    public BookManagerServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookLists.add(new Book(1, "Android"));
        mBookLists.add(new Book(2, "IOS"));
        new Thread(new ServerWorker()).start();
    }


    private void onNewBookArrived(Book book )throws RemoteException{
        mBookLists.add(book);

        final int N = mListeners.beginBroadcast();
        Log.d(TAG, "开始逐个进行监听器的开启:"+N);
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListeners.getBroadcastItem(i);
            if (listener!=null){
                try{
                    listener.onNewBookArrived(book);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }
        mListeners.finishBroadcast();
    }

    /**
     * 每隔5s添加一本书籍
     */
    private class ServerWorker implements Runnable{
        @Override
        public void run() {
            while(!isServiceDestroy.get()){
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookLists.size()+1;
                Book newBook = new Book(bookId, "new Book# "+bookId);
                try{
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
