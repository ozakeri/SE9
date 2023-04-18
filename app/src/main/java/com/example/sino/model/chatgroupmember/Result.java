
package com.example.sino.model.chatgroupmember;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("ChatGroupMemberList")
    @Expose
    public List<ChatGroupMemberList> chatGroupMemberList = null;

}
