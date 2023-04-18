package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuccessActivationBean implements Parcelable {
    private String SUCCESS;
    private ActivationBean RESULT;
    private String ERROR;

    protected SuccessActivationBean(Parcel in) {
        SUCCESS = in.readString();
        RESULT = in.readParcelable(ActivationBean.class.getClassLoader());
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

    public static final Creator<SuccessActivationBean> CREATOR = new Creator<SuccessActivationBean>() {
        @Override
        public SuccessActivationBean createFromParcel(Parcel in) {
            return new SuccessActivationBean(in);
        }

        @Override
        public SuccessActivationBean[] newArray(int size) {
            return new SuccessActivationBean[size];
        }
    };

    public String getSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(String SUCCESS) {
        this.SUCCESS = SUCCESS;
    }

    public ActivationBean getRESULT() {
        return RESULT;
    }

    public void setRESULT(ActivationBean RESULT) {
        this.RESULT = RESULT;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }
}
