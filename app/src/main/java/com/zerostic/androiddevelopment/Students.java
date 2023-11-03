package com.zerostic.androiddevelopment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Students implements Parcelable {
    String name;
    int age;

    public Students(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Students(Parcel parcel){
        name = parcel.readString();
        age = parcel.readInt();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static final Creator<Students> CREATOR = new Creator<Students>() {
        @Override
        public Students createFromParcel(Parcel in) {
            return new Students(in);
        }

        @Override
        public Students[] newArray(int size) {
            return new Students[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(age);
    }

}
