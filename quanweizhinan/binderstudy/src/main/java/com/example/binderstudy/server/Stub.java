package com.example.binderstudy.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;


import com.example.binderstudy.Book;
import com.example.binderstudy.proxy.Proxy;

import java.util.List;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 05/01/2018
 * 处理Binder客户端请求的抽象类，进行反序列化，不进行对 反序列化的对象 执行具体的操作
 * 处理对客户端的结果返回
 */
public abstract class Stub extends Binder implements BookManager {

    private static final String DESCRIPTOR = "com.baronzhang.ipc.server.BookManager";

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * 根据binder，返回对应的能供客户端调用的对象
     *
     * @param binder 连接对应的binder
     * @return binder对应的客户端使用的对象
     */
    public static BookManager asInterface(IBinder binder) {

        //传入空，返回空的
        if (binder == null) {
            return null;
        }

        //先找一下是不是同一进程的对象，是的话就直接返回对应的对象，不是的话就返回代理
        //这里是客户进程进行访问
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof BookManager) {
            return (BookManager) iin;
        }
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {

            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            //得到书籍的List
            case TRANSAVTION_getBooks:
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;

                //添加一本书，首先将书籍反序列化，然后调用添加方法
            case TRANSAVTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            default:
                break;

        }
        return super.onTransact(code, data, reply, flags);
    }

    public static final int TRANSAVTION_getBooks = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
}
