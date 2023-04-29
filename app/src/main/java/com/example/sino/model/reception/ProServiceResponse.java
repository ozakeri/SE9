
package com.example.sino.model.reception;


import com.example.sino.model.ResultBean;
import com.google.gson.annotations.SerializedName;

public class ProServiceResponse {

    @SerializedName("SUCCESS")
    public String success;
    @SerializedName("RESULT")
    public ResultBean result;
    @SerializedName("ERROR")
    public String ERROR;

}
