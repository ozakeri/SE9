
package com.example.sino.model.carinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessCarInfoBean {

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
