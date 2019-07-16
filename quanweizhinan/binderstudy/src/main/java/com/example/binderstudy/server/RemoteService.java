package com.example.binderstudy.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.example.binderstudy.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回binder，提供服务
 */
public class RemoteService extends Service {

    private List<Book> books = new ArrayList<>();

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("三体");
        book.setPrice(88);
        books.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }

    /**
     * 具体的服务端对客户端传过来的数据进行处理的对象，实现了抽象类未实现的具体方法逻辑
     */
    private final Stub bookManager = new Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book == null) {
                    return;
                }

                book.setPrice(book.getPrice() * 2);
                books.add(book);

                Log.e("Server", "books: " + book.toString());
            }
        }
    };
}
