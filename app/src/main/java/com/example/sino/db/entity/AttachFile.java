package com.example.sino.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.DateConverter;

import java.util.Date;

@Entity(tableName = "AttachFile")
public class AttachFile implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String attachFileLocalPath;
    private String attachFileUserFileName;
    private String attachFileRemoteUrl;
    @TypeConverters({DateConverter.class})
    private java.util.Date dateCreation;
    private Integer sendingStatusEn;
    @TypeConverters({DateConverter.class})
    private java.util.Date sendingStatusDate;
    private Integer attachFileSize;
    private Integer attachFileSentSize;
    private Integer attachFileReceivedSize;
    private Integer entityNameEn;//183
    private Long entityId;//complaint report id
    private Long serverAttachFileId;
    private Long serverEntityId;//server complaint report id
    private Long serverAttachFileSettingId;

    public AttachFile() {
    }

    protected AttachFile(Parcel in) {
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
    }

    public static final Creator<AttachFile> CREATOR = new Creator<AttachFile>() {
        @Override
        public AttachFile createFromParcel(Parcel in) {
            return new AttachFile(in);
        }

        @Override
        public AttachFile[] newArray(int size) {
            return new AttachFile[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(attachFileLocalPath);
        parcel.writeString(attachFileUserFileName);
        parcel.writeString(attachFileRemoteUrl);
        if (sendingStatusEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sendingStatusEn);
        }
        if (attachFileSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(attachFileSize);
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
        if (entityNameEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(entityNameEn);
        }
        if (entityId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(entityId);
        }
        if (serverAttachFileId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(serverAttachFileId);
        }
        if (serverEntityId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(serverEntityId);
        }
        if (serverAttachFileSettingId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(serverAttachFileSettingId);
        }
    }
}
