
package com.example.sino.model.carinfo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverters;

import com.example.sino.model.company.Company;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.User;
import com.example.sino.utils.converters.CarListConverter;
import com.example.sino.utils.converters.ChatMessageListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Parcelable {

    @SerializedName("car")
    @Expose
    public Car car;
    @TypeConverters({CarListConverter.class})
    @SerializedName("carList")
    public List<Car> carList;

    @SerializedName("user")
    @Expose
    public AppUser user;

    @SerializedName("company")
    public Company company;

    protected Result(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
