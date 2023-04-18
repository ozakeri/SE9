package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuccessRegisterBean implements Parcelable {
    private String SUCCESS;
    private RegisterBean RESULT;
    private String ERROR;

    protected SuccessRegisterBean(Parcel in) {
        SUCCESS = in.readString();
        RESULT = in.readParcelable(RegisterBean.class.getClassLoader());
        ERROR = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SUCCESS);
        dest.writeParcelable(RESULT, flags);
        dest.writeString(ERROR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SuccessRegisterBean> CREATOR = new Creator<SuccessRegisterBean>() {
        @Override
        public SuccessRegisterBean createFromParcel(Parcel in) {
            return new SuccessRegisterBean(in);
        }

        @Override
        public SuccessRegisterBean[] newArray(int size) {
            return new SuccessRegisterBean[size];
        }
    };

    public String getSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(String SUCCESS) {
        this.SUCCESS = SUCCESS;
    }

    public RegisterBean getRESULT() {
        return RESULT;
    }

    public void setRESULT(RegisterBean RESULT) {
        this.RESULT = RESULT;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }
}
