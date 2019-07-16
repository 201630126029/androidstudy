package com.example.binderstudy.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.binderstudy.Book;
import com.example.binderstudy.R;
import com.example.binderstudy.server.BookManager;
import com.example.binderstudy.server.RemoteService;
import com.example.binderstudy.server.Stub;

import java.util.List;

/**
 * 客户端活动
 */
public class ClientActivity extends AppCompatActivity {

    private BookManager bookManager;
    private boolean isConnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没绑定，进行绑定
                if (!isConnection) {
                    attemptToBindService();
                    return;
                }

                //出错啦，直接是空
                if (bookManager == null) {
                    return;
                }

                try {
                    Book book = new Book();
                    book.setPrice(101);
                    book.setName("编码");
                    bookManager.addBook(book);

                    Log.d("ClientActivity", bookManager.getBooks().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 绑定服务
     */
    private void attemptToBindService() {

        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction("com.baronzhang.ipc.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnection = true;
            //返回供客户端使用的对象，同进程就直接是服务中的对象，不同进程是个代理
            bookManager = Stub.asInterface(service);
            if (bookManager != null) {
                try {
                    List<Book> books = bookManager.getBooks();
                    Log.d("ClientActivity", books.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnection) {
            unbindService(serviceConnection);
        }
    }
}
