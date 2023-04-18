
package com.example.sino.model.chatgroupmember;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessChatGroupMemberBean {

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
