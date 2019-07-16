package com.example.binderstudy.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.binderstudy.Book;
import com.example.binderstudy.server.BookManager;
import com.example.binderstudy.server.Stub;

import java.util.List;


/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 05/01/2018
 * 客户端的代理类，将数据进行序列话，按格式传给服务端
 */
public class Proxy implements BookManager {

    private static final String DESCRIPTOR = "com.baronzhang.ipc.server.BookManager";

    private IBinder remote;

    public Proxy(IBinder remote) {
        this.remote = remote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<Book> getBooks() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        List<Book> result;

        try {
            //加个token
            data.writeInterfaceToken(DESCRIPTOR);
            //调用服务端，将序列化的数据传进去，这里是没有数据要传
            remote.transact(Stub.TRANSAVTION_getBooks, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(Book.CREATOR);
        } finally {
            replay.recycle();
            data.recycle();
        }
        return result;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (book != null) {
                data.writeInt(1);
                //将book写入到data中
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(Stub.TRANSAVTION_addBook, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}
