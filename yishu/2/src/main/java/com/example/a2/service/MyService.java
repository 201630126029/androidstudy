package com.example.a2.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.a2.aidl.Book;
import com.example.a2.aidl.IBookManager;
import com.example.a2.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBookManager;
    }

    private static CopyOnWriteArrayList<Book> mBookList= new CopyOnWriteArrayList<>();

    public static final IBookManager.Stub mBookManager = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

        }
    };
}
