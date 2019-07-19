// IBookManager.aidl
package com.example.a2.aidl;

import com.example.a2.aidl.Book;
import com.example.a2.aidl.IOnNewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unRegisterListener(IOnNewBookArrivedListener listener);
}
