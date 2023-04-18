
package com.example.sino.model.documentversion;


import com.example.sino.model.ResultBean;
import com.google.gson.annotations.SerializedName;

public class DocumentVersion {

    @SerializedName("SUCCESS")
    public String success;
    @SerializedName("RESULT")
    public ResultBean result;

}
