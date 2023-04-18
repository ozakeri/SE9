package com.example.sino.model;

import com.example.sino.model.dailyEvent.DailyEvent;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.documentversion.Document;
import com.example.sino.model.reception.AttachFileSettingListJsonArray;
import com.example.sino.model.reception.JsonArrayAttach;
import com.example.sino.model.reception.PrcData;
import com.example.sino.model.reception.ProSrv;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultBean{

    @SerializedName("userPermissionList")
    private List<String> userPermissionList;

    @SerializedName("chatMessageReceiverList")
    public List<ChatMessageReceiver> chatMessageReceiverList = null;

    @SerializedName("document")
    public Document document;

    @SerializedName("person")
    public Person person;

    @SerializedName("proSrvList")
    public List<ProSrv> proSrvList = null;

    @SerializedName("proSrv")
    public ProSrv proSrv;

    @SerializedName("attachFileId")
    public String attachFileId;

    @SerializedName("prcData")
    public PrcData prcData;

    @SerializedName("dailyEvent")
    public DailyEvent dailyEvent;

    @SerializedName("attachFileSettingListJsonArray")
    public List<AttachFileSettingListJsonArray> attachFileSettingListJsonArray = null;

    @SerializedName("jsonArrayAttach")
    public List<JsonArrayAttach> jsonArrayAttach = null;

    @SerializedName("dailyEventList")
    public List<DailyEvent> dailyEventList;

    public List<String> getUserPermissionList() {
        return userPermissionList;
    }

    public void setUserPermissionList(List<String> userPermissionList) {
        this.userPermissionList = userPermissionList;
    }

    public List<ChatMessageReceiver> getChatMessageReceiverList() {
        return chatMessageReceiverList;
    }

    public void setChatMessageReceiverList(List<ChatMessageReceiver> chatMessageReceiverList) {
        this.chatMessageReceiverList = chatMessageReceiverList;
    }
}
