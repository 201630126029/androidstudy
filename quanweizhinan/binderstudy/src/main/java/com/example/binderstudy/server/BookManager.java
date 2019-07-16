package com.example.binderstudy.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.example.binderstudy.Book;

import java.util.List;


/**
 * 这个类用来定义服务端 RemoteService 具备什么样的能力
 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 05/01/2018
 * 里面是放了两个方法
 */
public interface BookManager extends IInterface {

    List<Book> getBooks() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
