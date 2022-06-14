package com.gap.sino.util.volly;

import com.gap.sino.util.volly.attach.SavedAttachFile;
import com.gap.sino.util.volly.chatgrpupemember.ChatGroupMemberList;
import com.gap.sino.util.volly.chatmessage.SavedChatMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RESULTBean {

    @SerializedName("ChatGroupMemberList")
    @Expose
    public List<ChatGroupMemberList> chatGroupMemberList = null;
    @SerializedName("attacheFileChecksum")
    @Expose
    public String attacheFileChecksum;
    @SerializedName("attachFileSize")
    @Expose
    public Integer attachFileSize;
    @SerializedName("fileBytes")
    @Expose
    public List<Integer> fileBytes = null;
    @SerializedName("savedAttachFile")
    @Expose
    public SavedAttachFile savedAttachFile;
    @SerializedName("savedChatMessage")
    @Expose
    public SavedChatMessage savedChatMessage;
    @SerializedName("max")
    @Expose
    public int max;
}

