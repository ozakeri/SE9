
package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PersonRequentBean implements Parcelable {

    @SerializedName("SUCCESS")
    public String success;
    @SerializedName("RESULT")
    public ResultBean result;

    protected PersonRequentBean(Parcel in) {
        success = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonRequentBean> CREATOR = new Creator<PersonRequentBean>() {
        @Override
        public PersonRequentBean createFromParcel(Parcel in) {
            return new PersonRequentBean(in);
        }

        @Override
        public PersonRequentBean[] newArray(int size) {
            return new PersonRequentBean[size];
        }
    };
}
