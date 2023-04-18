package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuccessPermissionBean {
    private String SUCCESS;
    private ResultBean RESULT;
    private String ERROR;

    public String getSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(String SUCCESS) {
        this.SUCCESS = SUCCESS;
    }

    public ResultBean getRESULT() {
        return RESULT;
    }

    public void setRESULT(ResultBean RESULT) {
        this.RESULT = RESULT;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }
}
