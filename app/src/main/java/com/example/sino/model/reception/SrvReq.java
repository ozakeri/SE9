
package com.example.sino.model.reception;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sino.model.carinfo.Car;
import com.example.sino.model.company.Company;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SrvReq implements Parcelable {
    @SerializedName("dateCreation")
    public String dateCreation;
    @SerializedName("kmCar")
    public Integer kmCar;
    @SerializedName("processStatus")
    public Integer processStatus;
    @SerializedName("searchOnCurrentProcessStatus")
    public Boolean searchOnCurrentProcessStatus;
    @SerializedName("statusIcon")
    public String statusIcon;
    @SerializedName("lastProcessBisDataIsValidForDoNext")
    public Boolean lastProcessBisDataIsValidForDoNext;
    @SerializedName("description")
    public String description;
    @SerializedName("serviceTypeEn")
    public Integer serviceTypeEn;
    @SerializedName("locateEn_text")
    public String locateEnText;
    @SerializedName("locateEn")
    public Integer locateEn;
    @SerializedName("dateConfAgent")
    public String dateConfAgent;
    @SerializedName("dateReq")
    public String dateReq;
    @SerializedName("rezTypeEn_text")
    public String rezTypeEnText;
    @SerializedName("statusIsCurrent")
    public Boolean statusIsCurrent;
    @SerializedName("id")
    public Integer id;
    @SerializedName("identifier")
    public String identifier;
    @SerializedName("enabledAdvancedSearch")
    public Boolean enabledAdvancedSearch;
    @SerializedName("srvReasonEn")
    public Integer srvReasonEn;
    @SerializedName("srvReasonEn_text")
    public String srvReasonEnText;
    @SerializedName("processStatusForSrvReqOpen")
    public String processStatusForSrvReqOpen;
    @SerializedName("genericTypeClassName")
    public String genericTypeClassName;
    @SerializedName("cancelIsPossible")
    public Boolean cancelIsPossible;
    @SerializedName("dateConfCust")
    public String dateConfCust;
    @SerializedName("showObjForCust")
    public Boolean showObjForCust;
    @SerializedName("serviceTypeEn_text")
    public String serviceTypeEnText;
    @SerializedName("editableIs")
    public Boolean editableIs;
    @SerializedName("processStatus_text")
    public String processStatusText;
    @SerializedName("rezTypeEn")
    public Integer rezTypeEn;
    @SerializedName("status_text")
    public String statusText;
    @SerializedName("searchOnCurrentStatus")
    public Boolean searchOnCurrentStatus;
    @SerializedName("dayReqCanBefMin")
    public Boolean dayReqCanBefMin;
    @SerializedName("status")
    public Integer status;

    protected SrvReq(Parcel in) {
        dateCreation = in.readString();
        if (in.readByte() == 0) {
            kmCar = null;
        } else {
            kmCar = in.readInt();
        }
        if (in.readByte() == 0) {
            processStatus = null;
        } else {
            processStatus = in.readInt();
        }
        byte tmpSearchOnCurrentProcessStatus = in.readByte();
        searchOnCurrentProcessStatus = tmpSearchOnCurrentProcessStatus == 0 ? null : tmpSearchOnCurrentProcessStatus == 1;
        statusIcon = in.readString();
        byte tmpLastProcessBisDataIsValidForDoNext = in.readByte();
        lastProcessBisDataIsValidForDoNext = tmpLastProcessBisDataIsValidForDoNext == 0 ? null : tmpLastProcessBisDataIsValidForDoNext == 1;
        description = in.readString();
        if (in.readByte() == 0) {
            serviceTypeEn = null;
        } else {
            serviceTypeEn = in.readInt();
        }
        locateEnText = in.readString();
        if (in.readByte() == 0) {
            locateEn = null;
        } else {
            locateEn = in.readInt();
        }
        dateConfAgent = in.readString();
        dateReq = in.readString();
        rezTypeEnText = in.readString();
        byte tmpStatusIsCurrent = in.readByte();
        statusIsCurrent = tmpStatusIsCurrent == 0 ? null : tmpStatusIsCurrent == 1;
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        identifier = in.readString();
        byte tmpEnabledAdvancedSearch = in.readByte();
        enabledAdvancedSearch = tmpEnabledAdvancedSearch == 0 ? null : tmpEnabledAdvancedSearch == 1;
        if (in.readByte() == 0) {
            srvReasonEn = null;
        } else {
            srvReasonEn = in.readInt();
        }
        srvReasonEnText = in.readString();
        processStatusForSrvReqOpen = in.readString();
        genericTypeClassName = in.readString();
        byte tmpCancelIsPossible = in.readByte();
        cancelIsPossible = tmpCancelIsPossible == 0 ? null : tmpCancelIsPossible == 1;
        dateConfCust = in.readString();
        byte tmpShowObjForCust = in.readByte();
        showObjForCust = tmpShowObjForCust == 0 ? null : tmpShowObjForCust == 1;
        serviceTypeEnText = in.readString();
        byte tmpEditableIs = in.readByte();
        editableIs = tmpEditableIs == 0 ? null : tmpEditableIs == 1;
        processStatusText = in.readString();
        if (in.readByte() == 0) {
            rezTypeEn = null;
        } else {
            rezTypeEn = in.readInt();
        }
        statusText = in.readString();
        byte tmpSearchOnCurrentStatus = in.readByte();
        searchOnCurrentStatus = tmpSearchOnCurrentStatus == 0 ? null : tmpSearchOnCurrentStatus == 1;
        byte tmpDayReqCanBefMin = in.readByte();
        dayReqCanBefMin = tmpDayReqCanBefMin == 0 ? null : tmpDayReqCanBefMin == 1;
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateCreation);
        if (kmCar == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(kmCar);
        }
        if (processStatus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(processStatus);
        }
        dest.writeByte((byte) (searchOnCurrentProcessStatus == null ? 0 : searchOnCurrentProcessStatus ? 1 : 2));
        dest.writeString(statusIcon);
        dest.writeByte((byte) (lastProcessBisDataIsValidForDoNext == null ? 0 : lastProcessBisDataIsValidForDoNext ? 1 : 2));
        dest.writeString(description);
        if (serviceTypeEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serviceTypeEn);
        }
        dest.writeString(locateEnText);
        if (locateEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(locateEn);
        }
        dest.writeString(dateConfAgent);
        dest.writeString(dateReq);
        dest.writeString(rezTypeEnText);
        dest.writeByte((byte) (statusIsCurrent == null ? 0 : statusIsCurrent ? 1 : 2));
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(identifier);
        dest.writeByte((byte) (enabledAdvancedSearch == null ? 0 : enabledAdvancedSearch ? 1 : 2));
        if (srvReasonEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(srvReasonEn);
        }
        dest.writeString(srvReasonEnText);
        dest.writeString(processStatusForSrvReqOpen);
        dest.writeString(genericTypeClassName);
        dest.writeByte((byte) (cancelIsPossible == null ? 0 : cancelIsPossible ? 1 : 2));
        dest.writeString(dateConfCust);
        dest.writeByte((byte) (showObjForCust == null ? 0 : showObjForCust ? 1 : 2));
        dest.writeString(serviceTypeEnText);
        dest.writeByte((byte) (editableIs == null ? 0 : editableIs ? 1 : 2));
        dest.writeString(processStatusText);
        if (rezTypeEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rezTypeEn);
        }
        dest.writeString(statusText);
        dest.writeByte((byte) (searchOnCurrentStatus == null ? 0 : searchOnCurrentStatus ? 1 : 2));
        dest.writeByte((byte) (dayReqCanBefMin == null ? 0 : dayReqCanBefMin ? 1 : 2));
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SrvReq> CREATOR = new Creator<SrvReq>() {
        @Override
        public SrvReq createFromParcel(Parcel in) {
            return new SrvReq(in);
        }

        @Override
        public SrvReq[] newArray(int size) {
            return new SrvReq[size];
        }
    };
}
