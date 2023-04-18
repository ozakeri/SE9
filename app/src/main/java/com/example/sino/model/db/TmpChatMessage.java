package com.example.sino.model.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.AppUserConverter;
import com.example.sino.utils.converters.DateConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Date;

public class TmpChatMessage {

    public Integer id;
    public Integer senderUserId;
    private String message;
    public String attachFileUserFileName;
    private String attachFileLocalPath;
    private JSONArray attachmentBytes;
    public Integer chatGroupId;
    private Boolean isCreateNewPvChatGroup;
    private String attachmentChecksum;
    private String validUntilDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Integer senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachFileUserFileName() {
        return attachFileUserFileName;
    }

    public void setAttachFileUserFileName(String attachFileUserFileName) {
        this.attachFileUserFileName = attachFileUserFileName;
    }

    public String getAttachFileLocalPath() {
        return attachFileLocalPath;
    }

    public void setAttachFileLocalPath(String attachFileLocalPath) {
        this.attachFileLocalPath = attachFileLocalPath;
    }

    public JSONArray getAttachmentBytes() {
        return attachmentBytes;
    }

    public void setAttachmentBytes(JSONArray attachmentBytes) {
        this.attachmentBytes = attachmentBytes;
    }

    public Integer getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(Integer chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public Boolean getCreateNewPvChatGroup() {
        return isCreateNewPvChatGroup;
    }

    public void setCreateNewPvChatGroup(Boolean createNewPvChatGroup) {
        isCreateNewPvChatGroup = createNewPvChatGroup;
    }

    public String getAttachmentChecksum() {
        return attachmentChecksum;
    }

    public void setAttachmentChecksum(String attachmentChecksum) {
        this.attachmentChecksum = attachmentChecksum;
    }

    public String getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(String validUntilDate) {
        this.validUntilDate = validUntilDate;
    }
}
