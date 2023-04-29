
package com.example.sino.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.sino.model.company.Company;
import com.google.gson.annotations.SerializedName;

public class UserInfoResponse implements Parcelable {

    @SerializedName("company")
    public Company company;

    protected UserInfoResponse(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfoResponse> CREATOR = new Creator<UserInfoResponse>() {
        @Override
        public UserInfoResponse createFromParcel(Parcel in) {
            return new UserInfoResponse(in);
        }

        @Override
        public UserInfoResponse[] newArray(int size) {
            return new UserInfoResponse[size];
        }
    };
}
