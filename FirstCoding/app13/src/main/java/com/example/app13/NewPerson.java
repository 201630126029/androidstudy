package com.example.app13;

import android.os.Parcel;
import android.os.Parcelable;

public class NewPerson implements Parcelable {
    private String name;
    private int age;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    public static final Parcelable.Creator<NewPerson> CREATOR=new Parcelable.Creator<NewPerson>(){

        @Override
        public NewPerson createFromParcel(Parcel source) {
            NewPerson person = new NewPerson();
            person.name=source.readString();
            person.age=source.readInt();
            return person;
        }

        @Override
        public NewPerson[] newArray(int size) {
            return new NewPerson[size];
        }
    };
}
