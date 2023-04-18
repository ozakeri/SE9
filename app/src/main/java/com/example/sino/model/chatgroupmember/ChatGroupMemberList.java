package com.example.sino.model.chatgroupmember;

import androidx.room.PrimaryKey;

import com.example.sino.model.db.AppUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGroupMemberList {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("maxMember")
    @Expose
    private int maxMember;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notifyAct")
    @Expose
    private Boolean notifyAct;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("chatGroupMembers")
    @Expose
    public List<ChatGroupMember> chatGroupMembers = null;

    private Integer privilegeTypeEn;
    private Boolean adminIs;
    private Long appUserId;
    private Long chatGroupId;
    private transient List<Long> notAppUserIdList;
    private transient AppUser appUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNotifyAct() {
        return notifyAct;
    }

    public void setNotifyAct(Boolean notifyAct) {
        this.notifyAct = notifyAct;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ChatGroupMember> getChatGroupMembers() {
        return chatGroupMembers;
    }

    public void setChatGroupMembers(List<ChatGroupMember> chatGroupMembers) {
        this.chatGroupMembers = chatGroupMembers;
    }

    public Integer getPrivilegeTypeEn() {
        return privilegeTypeEn;
    }

    public void setPrivilegeTypeEn(Integer privilegeTypeEn) {
        this.privilegeTypeEn = privilegeTypeEn;
    }

    public Boolean getAdminIs() {
        return adminIs;
    }

    public void setAdminIs(Boolean adminIs) {
        this.adminIs = adminIs;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(Long chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public List<Long> getNotAppUserIdList() {
        return notAppUserIdList;
    }

    public void setNotAppUserIdList(List<Long> notAppUserIdList) {
        this.notAppUserIdList = notAppUserIdList;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
