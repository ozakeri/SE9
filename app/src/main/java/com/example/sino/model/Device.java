package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Device implements Parcelable {

    private final String imei;
    private final String deviceName;
    private final String osName;
    private final String osVersion;

    public Device(String imei, String deviceName, String osName, String osVersion) {
        this.imei = imei;
        this.deviceName = deviceName;
        this.osName = osName;
        this.osVersion = osVersion;
    }

    protected Device(Parcel in) {
        imei = in.readString();
        deviceName = in.readString();
        osName = in.readString();
        osVersion = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imei);
        parcel.writeString(deviceName);
        parcel.writeString(osName);
        parcel.writeString(osVersion);
    }
}
