package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sino.model.db.User;

public class RegisterBean implements Parcelable {
    private String mobileNo;

    protected RegisterBean(Parcel in) {
        mobileNo = in.readString();
    }

    public static final Creator<RegisterBean> CREATOR = new Creator<RegisterBean>() {
        @Override
        public RegisterBean createFromParcel(Parcel in) {
            return new RegisterBean(in);
        }

        @Override
        public RegisterBean[] newArray(int size) {
            return new RegisterBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mobileNo);
    }

    public String getMobileNo() {
        return mobileNo;
    }
}
