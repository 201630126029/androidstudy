package com.example.a2.bindpool;

import android.os.RemoteException;

public class ComputImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
