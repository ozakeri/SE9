package com.example.sino.model.complaintreport;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.DateConverter;

import java.util.Date;

@Entity(tableName = "ComplaintReport")
public class ComplaintReport implements Parcelable {

    @PrimaryKey
    private Long id;
    private String identifier;
    private Long entityId;
    private Integer entityNameEn;
    private String displayName;
    private String reportCode;
    private String reportStr;
    private Long userReportId;
    @TypeConverters({DateConverter.class})
    private java.util.Date reportDate;
    private Long serverId;
    private Boolean deliverIs;
    @TypeConverters({DateConverter.class})
    private java.util.Date deliverDate;
    private Integer sendingStatusEn;
    @TypeConverters({DateConverter.class})
    private java.util.Date sendingStatusDate;
    private String latitude;
    private String longitude;

    public ComplaintReport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getEntityNameEn() {
        return entityNameEn;
    }

    public void setEntityNameEn(Integer entityNameEn) {
        this.entityNameEn = entityNameEn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportStr() {
        return reportStr;
    }

    public void setReportStr(String reportStr) {
        this.reportStr = reportStr;
    }

    public Long getUserReportId() {
        return userReportId;
    }

    public void setUserReportId(Long userReportId) {
        this.userReportId = userReportId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    protected ComplaintReport(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        identifier = in.readString();
        if (in.readByte() == 0) {
            entityId = null;
        } else {
            entityId = in.readLong();
        }
        if (in.readByte() == 0) {
            entityNameEn = null;
        } else {
            entityNameEn = in.readInt();
        }
        displayName = in.readString();
        reportCode = in.readString();
        reportStr = in.readString();
        if (in.readByte() == 0) {
            userReportId = null;
        } else {
            userReportId = in.readLong();
        }
        if (in.readByte() == 0) {
            serverId = null;
        } else {
            serverId = in.readLong();
        }
        byte tmpDeliverIs = in.readByte();
        deliverIs = tmpDeliverIs == 0 ? null : tmpDeliverIs == 1;
        if (in.readByte() == 0) {
            sendingStatusEn = null;
        } else {
            sendingStatusEn = in.readInt();
        }
        latitude = in.readString();
        longitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(identifier);
        if (entityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(entityId);
        }
        if (entityNameEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(entityNameEn);
        }
        dest.writeString(displayName);
        dest.writeString(reportCode);
        dest.writeString(reportStr);
        if (userReportId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userReportId);
        }
        if (serverId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverId);
        }
        dest.writeByte((byte) (deliverIs == null ? 0 : deliverIs ? 1 : 2));
        if (sendingStatusEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sendingStatusEn);
        }
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ComplaintReport> CREATOR = new Creator<ComplaintReport>() {
        @Override
        public ComplaintReport createFromParcel(Parcel in) {
            return new ComplaintReport(in);
        }

        @Override
        public ComplaintReport[] newArray(int size) {
            return new ComplaintReport[size];
        }
    };
}
