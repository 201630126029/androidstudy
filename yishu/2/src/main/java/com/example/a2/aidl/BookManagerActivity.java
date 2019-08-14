package com.example.a2.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.a2.R;
import com.example.a2.utils.MyConstants;

import java.util.List;

/**
 * @author xuanqis
 * 客户端活动，能够增加书籍，查询书籍列表，在新书到达时接受书籍信息
 */
public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";
    //新书到达的消息what
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager bookManager;
    /**
     * 在主线程处理消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "新来了一本书：" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                bookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list, list type:" + list.getClass());
                Log.i(TAG, "query book list, list type:" + list.toString());
                Book book = new Book(3, "Android进阶");
                bookManager.addBook(book);
                Log.d(TAG, "加入：" + book);
                list = bookManager.getBookList();
                Log.d(TAG, "书单：" + list.toString());
                bookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
            Log.e(TAG, "连接关闭.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        //绑定客户端和服务端
        Intent intent = new Intent(this, BookManagerServer.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        //取消注册监听器
        if (bookManager != null && bookManager.asBinder().isBinderAlive()) {
            try {
                bookManager.unRegisterListener(mIOnNewBookArrivedListener);
                Log.d(TAG, "解除注册" + bookManager);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //解除绑定
        unbindService(mServiceConnection);
        super.onDestroy();
    }


    /**
     * 新书到达的监听器，在客户端进程执行
     */
    private IOnNewBookArrivedListener mIOnNewBookArrivedListener =
            new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            //这里的book是放在obj里面，要注意的是obj的跨进程只能支持系统设置的Parcelable类
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
        }
    };
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Intent intent = new Intent(BookManagerActivity.this,
                    BookManagerServer.class);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    };
}
