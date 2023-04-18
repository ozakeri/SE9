package com.example.sino.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.Date;

public class AttachFileCopy implements Parcelable {
    private Long id;
    private String attachFileLocalPath;
    private String attachFileUserFileName;
    private String attachFileRemoteUrl;
    private Date dateCreation;
    private Integer sendingStatusEn;
    private Date sendingStatusDate;
    private Integer attachFileSize;
    private Integer attachFileSentSize;
    private Integer attachFileReceivedSize;
    private Integer entityNameEn;//183
    private Long entityId;//complaint report id
    private Long serverAttachFileId;
    private Long serverEntityId;//server complaint report id
    private Long serverAttachFileSettingId;
    private Long attachFileSettingId;
    private JSONArray attachmentBytes;
    private String attachmentChecksum;
    private Long serverId;

    public AttachFileCopy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachFileLocalPath() {
        return attachFileLocalPath;
    }

    public void setAttachFileLocalPath(String attachFileLocalPath) {
        this.attachFileLocalPath = attachFileLocalPath;
    }

    public String getAttachFileUserFileName() {
        return attachFileUserFileName;
    }

    public void setAttachFileUserFileName(String attachFileUserFileName) {
        this.attachFileUserFileName = attachFileUserFileName;
    }

    public String getAttachFileRemoteUrl() {
        return attachFileRemoteUrl;
    }

    public void setAttachFileRemoteUrl(String attachFileRemoteUrl) {
        this.attachFileRemoteUrl = attachFileRemoteUrl;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
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

    public Integer getAttachFileSize() {
        return attachFileSize;
    }

    public void setAttachFileSize(Integer attachFileSize) {
        this.attachFileSize = attachFileSize;
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

    public Integer getEntityNameEn() {
        return entityNameEn;
    }

    public void setEntityNameEn(Integer entityNameEn) {
        this.entityNameEn = entityNameEn;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getServerAttachFileId() {
        return serverAttachFileId;
    }

    public void setServerAttachFileId(Long serverAttachFileId) {
        this.serverAttachFileId = serverAttachFileId;
    }

    public Long getServerEntityId() {
        return serverEntityId;
    }

    public void setServerEntityId(Long serverEntityId) {
        this.serverEntityId = serverEntityId;
    }

    public Long getServerAttachFileSettingId() {
        return serverAttachFileSettingId;
    }

    public void setServerAttachFileSettingId(Long serverAttachFileSettingId) {
        this.serverAttachFileSettingId = serverAttachFileSettingId;
    }

    public Long getAttachFileSettingId() {
        return attachFileSettingId;
    }

    public void setAttachFileSettingId(Long attachFileSettingId) {
        this.attachFileSettingId = attachFileSettingId;
    }

    public JSONArray getAttachmentBytes() {
        return attachmentBytes;
    }

    public void setAttachmentBytes(JSONArray attachmentBytes) {
        this.attachmentBytes = attachmentBytes;
    }

    public String getAttachmentChecksum() {
        return attachmentChecksum;
    }

    public void setAttachmentChecksum(String attachmentChecksum) {
        this.attachmentChecksum = attachmentChecksum;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    protected AttachFileCopy(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        attachFileLocalPath = in.readString();
        attachFileUserFileName = in.readString();
        attachFileRemoteUrl = in.readString();
        if (in.readByte() == 0) {
            sendingStatusEn = null;
        } else {
            sendingStatusEn = in.readInt();
        }
        if (in.readByte() == 0) {
            attachFileSize = null;
        } else {
            attachFileSize = in.readInt();
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
            entityNameEn = null;
        } else {
            entityNameEn = in.readInt();
        }
        if (in.readByte() == 0) {
            entityId = null;
        } else {
            entityId = in.readLong();
        }
        if (in.readByte() == 0) {
            serverAttachFileId = null;
        } else {
            serverAttachFileId = in.readLong();
        }
        if (in.readByte() == 0) {
            serverEntityId = null;
        } else {
            serverEntityId = in.readLong();
        }
        if (in.readByte() == 0) {
            serverAttachFileSettingId = null;
        } else {
            serverAttachFileSettingId = in.readLong();
        }
        if (in.readByte() == 0) {
            attachFileSettingId = null;
        } else {
            attachFileSettingId = in.readLong();
        }
        attachmentChecksum = in.readString();
        if (in.readByte() == 0) {
            serverId = null;
        } else {
            serverId = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(attachFileLocalPath);
        dest.writeString(attachFileUserFileName);
        dest.writeString(attachFileRemoteUrl);
        if (sendingStatusEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sendingStatusEn);
        }
        if (attachFileSize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(attachFileSize);
        }
        if (attachFileSentSize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(attachFileSentSize);
        }
        if (attachFileReceivedSize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(attachFileReceivedSize);
        }
        if (entityNameEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(entityNameEn);
        }
        if (entityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(entityId);
        }
        if (serverAttachFileId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverAttachFileId);
        }
        if (serverEntityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverEntityId);
        }
        if (serverAttachFileSettingId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverAttachFileSettingId);
        }
        if (attachFileSettingId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(attachFileSettingId);
        }
        dest.writeString(attachmentChecksum);
        if (serverId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttachFileCopy> CREATOR = new Creator<AttachFileCopy>() {
        @Override
        public AttachFileCopy createFromParcel(Parcel in) {
            return new AttachFileCopy(in);
        }

        @Override
        public AttachFileCopy[] newArray(int size) {
            return new AttachFileCopy[size];
        }
    };
}
