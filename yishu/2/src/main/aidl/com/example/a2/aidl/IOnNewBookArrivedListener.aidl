// IOnNewBookArrivedListener.aidl
package com.example.a2.aidl;

import com.example.a2.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
