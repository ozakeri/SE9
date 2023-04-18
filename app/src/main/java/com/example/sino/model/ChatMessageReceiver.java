package com.example.sino.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.sino.model.db.ChatMessage;
import com.example.sino.utils.converters.ChatMessageConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMessageReceiver {
    @SerializedName("deliverIs")
    @Expose
    public Boolean deliverIs;
    @SerializedName("dateCreation")
    @Expose
    public String dateCreation;
    @SerializedName("readIs")
    @Expose
    public Boolean readIs;
    @SerializedName("readDate")
    @Expose
    public String readDate;
    @SerializedName("deliverDate")
    @Expose
    public String deliverDate;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status_text")
    @Expose
    public String statusText;

    @TypeConverters(ChatMessageConverter.class)
    @SerializedName("chatMessage")
    @Expose
    public ChatMessage chatMessage;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("receiverUserId")
    @Expose
    public Integer receiverUserId;
    @SerializedName("userCreationId")
    @Expose
    public Integer userCreationId;

    public Boolean getDeliverIs() {
        return deliverIs;
    }

    public void setDeliverIs(Boolean deliverIs) {
        this.deliverIs = deliverIs;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getReadIs() {
        return readIs;
    }

    public void setReadIs(Boolean readIs) {
        this.readIs = readIs;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}
