package com.example.sino.model.userInfobyid;

import com.example.sino.model.carinfo.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessUserInfoByIdBean {
    @SerializedName("SUCCESS")
    @Expose
    public String success;
    @SerializedName("RESULT")
    @Expose
    public Result result;
    @SerializedName("ERROR")
    @Expose
    public String error;
}
