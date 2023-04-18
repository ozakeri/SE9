
package com.example.sino.model.chatgroupmember;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.model.db.AppUser;
import com.example.sino.utils.converters.AppUserConverter;
import com.example.sino.utils.converters.LongListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "ChatGroupMember")
public class ChatGroupMember {

    @SerializedName("maxMember")
    @Expose
    public Integer maxMember;
    @SerializedName("notifyAct")
    @Expose
    public Boolean notifyAct;
    @SerializedName("name")
    @Expose
    public String name;
    @PrimaryKey
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("processStatus")
    @Expose
    public Integer processStatus;
    @SerializedName("privilegeTypeEn_text")
    @Expose
    public String privilegeTypeEnText;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("adminIs")
    @Expose
    public Boolean adminIs;
    @SerializedName("userId")
    @Expose
    public Integer userId;
    @SerializedName("processStatus_text")
    @Expose
    public String processStatusText;
    @SerializedName("expireDate")
    @Expose
    public String expireDate;
    @SerializedName("privilegeTypeEn")
    @Expose
    public Integer privilegeTypeEn;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("startDate")
    @Expose
    public String startDate;
    @SerializedName("userCreationId")
    @Expose
    public Integer userCreationId;
    private Long appUserId;
    private Long chatGroupId;
    @TypeConverters(LongListConverter.class)
    private List<Long> notAppUserIdList;
    @TypeConverters(AppUserConverter.class)
    private AppUser appUser;

    public Integer getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(Integer maxMember) {
        this.maxMember = maxMember;
    }

    public Boolean getNotifyAct() {
        return notifyAct;
    }

    public void setNotifyAct(Boolean notifyAct) {
        this.notifyAct = notifyAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getPrivilegeTypeEnText() {
        return privilegeTypeEnText;
    }

    public void setPrivilegeTypeEnText(String privilegeTypeEnText) {
        this.privilegeTypeEnText = privilegeTypeEnText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAdminIs() {
        return adminIs;
    }

    public void setAdminIs(Boolean adminIs) {
        this.adminIs = adminIs;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProcessStatusText() {
        return processStatusText;
    }

    public void setProcessStatusText(String processStatusText) {
        this.processStatusText = processStatusText;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getPrivilegeTypeEn() {
        return privilegeTypeEn;
    }

    public void setPrivilegeTypeEn(Integer privilegeTypeEn) {
        this.privilegeTypeEn = privilegeTypeEn;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getUserCreationId() {
        return userCreationId;
    }

    public void setUserCreationId(Integer userCreationId) {
        this.userCreationId = userCreationId;
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
