package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivationBean implements Parcelable {

    @SerializedName("userId")
    @Expose
    private Long serverUserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("tokenPass")
    @Expose
    private String bisPassword;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyType")
    @Expose
    private String companyType;
    @SerializedName("pictureBytes")
    @Expose
    private List<Integer> pictureBytes;

    public Long getServerUserId() {
        return serverUserId;
    }

    public void setServerUserId(Long serverUserId) {
        this.serverUserId = serverUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBisPassword() {
        return bisPassword;
    }

    public void setBisPassword(String bisPassword) {
        this.bisPassword = bisPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public List<Integer> getPictureBytes() {
        return pictureBytes;
    }

    public void setPictureBytes(List<Integer> pictureBytes) {
        this.pictureBytes = pictureBytes;
    }

    protected ActivationBean(Parcel in) {
        if (in.readByte() == 0) {
            serverUserId = null;
        } else {
            serverUserId = in.readLong();
        }
        name = in.readString();
        family = in.readString();
        username = in.readString();
        bisPassword = in.readString();
        companyName = in.readString();
        companyType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (serverUserId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverUserId);
        }
        dest.writeString(name);
        dest.writeString(family);
        dest.writeString(username);
        dest.writeString(bisPassword);
        dest.writeString(companyName);
        dest.writeString(companyType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivationBean> CREATOR = new Creator<ActivationBean>() {
        @Override
        public ActivationBean createFromParcel(Parcel in) {
            return new ActivationBean(in);
        }

        @Override
        public ActivationBean[] newArray(int size) {
            return new ActivationBean[size];
        }
    };
}
