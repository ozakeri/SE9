package com.example.sino.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.AppUserConverter;
import com.example.sino.utils.converters.ChatMessageConverter;
import com.example.sino.utils.converters.DateConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "chatMessage")
public class ChatMessage implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer id;
    @ColumnInfo(name = "chatGroupId")
    private Integer chatGroupId;
    @ColumnInfo(name = "dateCreation")
    private String dateCreation;
    @ColumnInfo(name = "attachFileId")
    private Integer attachFileId;
    @ColumnInfo(name = "senderUserId")
    private Integer senderUserId;
    @ColumnInfo(name = "sendDate")
    private String sendDate;
    @ColumnInfo(name = "attachFileSize")
    private Integer attachFileSize;
    @ColumnInfo(name = "attachFileUserFileName")
    private String attachFileUserFileName;
    @ColumnInfo(name = "statusText")
    private String statusText;
    @ColumnInfo(name = "status")
    private Integer status;
    @ColumnInfo(name = "clientMessageId")
    private Long clientMessageId;
    @ColumnInfo(name = "userCreationId")
    private Integer userCreationId;
    @ColumnInfo(name = "serverMessageId")
    private Long serverMessageId;
    @ColumnInfo(name = "validUntilDate")
    @TypeConverters({DateConverter.class})
    private java.util.Date validUntilDate;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "attachFileLocalPath")
    private String attachFileLocalPath;
    @ColumnInfo(name = "attachFileRemoteUrl")
    private String attachFileRemoteUrl;
    @ColumnInfo(name = "deliverIs")
    private Boolean deliverIs;
    @ColumnInfo(name = "deliverDate")
    @TypeConverters({DateConverter.class})
    private java.util.Date deliverDate;
    @ColumnInfo(name = "readIs")
    private Boolean readIs;
    @ColumnInfo(name = "readDate")
    @TypeConverters({DateConverter.class})
    private java.util.Date readDate;
    @ColumnInfo(name = "sendingStatusEn")
    private Integer sendingStatusEn;
    @ColumnInfo(name = "sendingStatusDate")
    @TypeConverters({DateConverter.class})
    private java.util.Date sendingStatusDate;
    @ColumnInfo(name = "attachFileSentSize")
    private Integer attachFileSentSize;
    @ColumnInfo(name = "attachFileReceivedSize")
    private Integer attachFileReceivedSize;
    @ColumnInfo(name = "senderAppUserId")
    private Long senderAppUserId;
    @ColumnInfo(name = "receiverAppUserId")
    private Long receiverAppUserId;
    @ColumnInfo(name = "senderAppUser")
    @TypeConverters({AppUserConverter.class})
    private AppUser senderAppUser;
    @ColumnInfo(name = "localAttachFileExist")
    private boolean localAttachFileExist;
    @ColumnInfo(name = "readDateFrom")
    @TypeConverters({DateConverter.class})
    private java.util.Date readDateFrom;
    @ColumnInfo(name = "senderAppUserIdNot")
    private Long senderAppUserIdNot;
    @ColumnInfo(name = "isCreateNewPvChatGroup")
    private Boolean isCreateNewPvChatGroup;


    public ChatMessage() {
    }

    protected ChatMessage(Parcel in) {
        if (in.readByte() == 0) {
            chatGroupId = null;
        } else {
            chatGroupId = in.readInt();
        }
        dateCreation = in.readString();
        if (in.readByte() == 0) {
            attachFileId = null;
        } else {
            attachFileId = in.readInt();
        }
        if (in.readByte() == 0) {
            senderUserId = null;
        } else {
            senderUserId = in.readInt();
        }
        sendDate = in.readString();
        if (in.readByte() == 0) {
            attachFileSize = null;
        } else {
            attachFileSize = in.readInt();
        }
        attachFileUserFileName = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        statusText = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        if (in.readByte() == 0) {
            clientMessageId = null;
        } else {
            clientMessageId = in.readLong();
        }
        if (in.readByte() == 0) {
            userCreationId = null;
        } else {
            userCreationId = in.readInt();
        }
        if (in.readByte() == 0) {
            serverMessageId = null;
        } else {
            serverMessageId = in.readLong();
        }
        message = in.readString();
        attachFileLocalPath = in.readString();
        attachFileRemoteUrl = in.readString();
        byte tmpDeliverIs = in.readByte();
        deliverIs = tmpDeliverIs == 0 ? null : tmpDeliverIs == 1;
        byte tmpReadIs = in.readByte();
        readIs = tmpReadIs == 0 ? null : tmpReadIs == 1;
        if (in.readByte() == 0) {
            sendingStatusEn = null;
        } else {
            sendingStatusEn = in.readInt();
        }
        if (in.readByte() == 0) {
            attachFileSentSize = null;
        } else {
            attachFileSentSize = in.readInt();
        }
        if (in.readByte() == 0) {
            attachFileReceivedSize = null;
        } else {
            attachFileReceivedSize = in.readInt();
        }
        if (in.readByte() == 0) {
            senderAppUserId = null;
        } else {
            senderAppUserId = in.readLong();
        }
        if (in.readByte() == 0) {
            receiverAppUserId = null;
        } else {
            receiverAppUserId = in.readLong();
        }
        localAttachFileExist = in.readByte() != 0;
        if (in.readByte() == 0) {
            senderAppUserIdNot = null;
        } else {
            senderAppUserIdNot = in.readLong();
        }
        byte tmpIsCreateNewPvChatGroup = in.readByte();
        isCreateNewPvChatGroup = tmpIsCreateNewPvChatGroup == 0 ? null : tmpIsCreateNewPvChatGroup == 1;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public Integer getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(Integer chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Integer getAttachFileId() {
        return attachFileId;
    }

    public void setAttachFileId(Integer attachFileId) {
        this.attachFileId = attachFileId;
    }

    public Integer getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Integer senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getAttachFileSize() {
        return attachFileSize;
    }

    public void setAttachFileSize(Integer attachFileSize) {
        this.attachFileSize = attachFileSize;
    }

    public String getAttachFileUserFileName() {
        return attachFileUserFileName;
    }

    public void setAttachFileUserFileName(String attachFileUserFileName) {
        this.attachFileUserFileName = attachFileUserFileName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(Long clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public Integer getUserCreationId() {
        return userCreationId;
    }

    public void setUserCreationId(Integer userCreationId) {
        this.userCreationId = userCreationId;
    }

    public Long getServerMessageId() {
        return serverMessageId;
    }

    public void setServerMessageId(Long serverMessageId) {
        this.serverMessageId = serverMessageId;
    }

    public Date getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(Date validUntilDate) {
        this.validUntilDate = validUntilDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachFileLocalPath() {
        return attachFileLocalPath;
    }

    public void setAttachFileLocalPath(String attachFileLocalPath) {
        this.attachFileLocalPath = attachFileLocalPath;
    }

    public String getAttachFileRemoteUrl() {
        return attachFileRemoteUrl;
    }

    public void setAttachFileRemoteUrl(String attachFileRemoteUrl) {
        this.attachFileRemoteUrl = attachFileRemoteUrl;
    }

    public Boolean getDeliverIs() {
        return deliverIs;
    }

    public void setDeliverIs(Boolean deliverIs) {
        this.deliverIs = deliverIs;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Boolean getReadIs() {
        return readIs;
    }

    public void setReadIs(Boolean readIs) {
        this.readIs = readIs;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public Integer getSendingStatusEn() {
        return sendingStatusEn;
    }

    public void setSendingStatusEn(Integer sendingStatusEn) {
        this.sendingStatusEn = sendingStatusEn;
    }

    public Date getSendingStatusDate() {
        return sendingStatusDate;
    }

    public void setSendingStatusDate(Date sendingStatusDate) {
        this.sendingStatusDate = sendingStatusDate;
    }

    public Integer getAttachFileSentSize() {
        return attachFileSentSize;
    }

    public void setAttachFileSentSize(Integer attachFileSentSize) {
        this.attachFileSentSize = attachFileSentSize;
    }

    public Integer getAttachFileReceivedSize() {
        return attachFileReceivedSize;
    }

    public void setAttachFileReceivedSize(Integer attachFileReceivedSize) {
        this.attachFileReceivedSize = attachFileReceivedSize;
    }

    public Long getSenderAppUserId() {
        return senderAppUserId;
    }

    public void setSenderAppUserId(Long senderAppUserId) {
        this.senderAppUserId = senderAppUserId;
    }

    public Long getReceiverAppUserId() {
        return receiverAppUserId;
    }

    public void setReceiverAppUserId(Long receiverAppUserId) {
        this.receiverAppUserId = receiverAppUserId;
    }

    public AppUser getSenderAppUser() {
        return senderAppUser;
    }

    public void setSenderAppUser(AppUser senderAppUser) {
        this.senderAppUser = senderAppUser;
    }

    public boolean isLocalAttachFileExist() {
        return localAttachFileExist;
    }

    public void setLocalAttachFileExist(boolean localAttachFileExist) {
        this.localAttachFileExist = localAttachFileExist;
    }

    public Date getReadDateFrom() {
        return readDateFrom;
    }

    public void setReadDateFrom(Date readDateFrom) {
        this.readDateFrom = readDateFrom;
    }

    public Long getSenderAppUserIdNot() {
        return senderAppUserIdNot;
    }

    public void setSenderAppUserIdNot(Long senderAppUserIdNot) {
        this.senderAppUserIdNot = senderAppUserIdNot;
    }

    public Boolean getCreateNewPvChatGroup() {
        return isCreateNewPvChatGroup;
    }

    public void setCreateNewPvChatGroup(Boolean createNewPvChatGroup) {
        isCreateNewPvChatGroup = createNewPvChatGroup;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (chatGroupId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(chatGroupId);
        }
        parcel.writeString(dateCreation);
        if (attachFileId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(attachFileId);
        }
        if (senderUserId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(senderUserId);
        }
        parcel.writeString(sendDate);
        if (attachFileSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(attachFileSize);
        }
        parcel.writeString(attachFileUserFileName);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(statusText);
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        if (clientMessageId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(clientMessageId);
        }
        if (userCreationId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userCreationId);
        }
        if (serverMessageId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(serverMessageId);
        }
        parcel.writeString(message);
        parcel.writeString(attachFileLocalPath);
        parcel.writeString(attachFileRemoteUrl);
        parcel.writeByte((byte) (deliverIs == null ? 0 : deliverIs ? 1 : 2));
        parcel.writeByte((byte) (readIs == null ? 0 : readIs ? 1 : 2));
        if (sendingStatusEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sendingStatusEn);
        }
        if (attachFileSentSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(attachFileSentSize);
        }
        if (attachFileReceivedSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(attachFileReceivedSize);
        }
        if (senderAppUserId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(senderAppUserId);
        }
        if (receiverAppUserId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(receiverAppUserId);
        }
        parcel.writeByte((byte) (localAttachFileExist ? 1 : 0));
        if (senderAppUserIdNot == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(senderAppUserIdNot);
        }
        parcel.writeByte((byte) (isCreateNewPvChatGroup == null ? 0 : isCreateNewPvChatGroup ? 1 : 2));
    }
}
