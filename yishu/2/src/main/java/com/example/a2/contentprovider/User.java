package com.example.a2.contentprovider;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private static final long serialVersionCode = 1L;
    public int userId;
    public String userName;
    public boolean isMale;

    public User book;
    public User(){

    }

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

    public static final Creator<User> CREATOR =
            new Creator<User>(){

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

    @Override
    public String toString() {
        return "userId: "+userId+" username: "+userName+" ismale: "+isMale;
    }
}
