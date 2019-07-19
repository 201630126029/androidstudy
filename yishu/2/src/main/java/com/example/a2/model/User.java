package com.example.a2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class User implements Parcelable {
    private static final long serialVersionCode = 1L;
    public int userId;
    public String userName;
    public boolean isMale;

    public User book;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(isMale?1:0);
        dest.writeParcelable(book, 0);
    }

    public static final Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>(){

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in){
        userId = in.readInt();
        userName=in.readString();
        isMale = in.readInt()==1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());


    }
}
