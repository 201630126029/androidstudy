package com.example.a2.contentprovider;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public int booId;
    public String bookName;

    public Book(int booId, String bookName){
        this.booId=booId;
        this.bookName=bookName;
    }

    public Book(){

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(booId);
        dest.writeString(bookName);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>(){

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in){
        booId=in.readInt();
        bookName=in.readString();
    }

    @Override
    public String toString() {
        return "bookId: "+booId +" bookName: "+bookName;
    }
}
