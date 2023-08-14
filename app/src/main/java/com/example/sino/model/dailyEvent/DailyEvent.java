
package com.example.sino.model.dailyEvent;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.sino.model.carinfo.Car;
import com.example.sino.model.reception.SrvReq;
import com.google.gson.annotations.SerializedName;

public class DailyEvent implements Parcelable {

    @SerializedName("carTypeMdlEn")
    public Integer carTypeMdlEn;
    @SerializedName("dateCreation")
    public String dateCreation;
    @SerializedName("processStatus")
    public Integer processStatus;
    @SerializedName("showDateCreationForView")
    public Boolean showDateCreationForView;
    @SerializedName("searchOnCurrentProcessStatus")
    public Boolean searchOnCurrentProcessStatus;
    @SerializedName("nameFvBefore")
    public String nameFvBefore;
    @SerializedName("statusIcon")
    public String statusIcon;
    @SerializedName("description")
    public String description;
    @SerializedName("lastProcessBisDataIsValidForDoNext")
    public Boolean lastProcessBisDataIsValidForDoNext;
    @SerializedName("carTypeMdlEn_text")
    public String carTypeMdlEnText;
    @SerializedName("planIs")
    public Boolean planIs;
    @SerializedName("inputMethodEn_text")
    public String inputMethodEnText;
    @SerializedName("typeGetCarEn")
    public Integer typeGetCarEn;
    @SerializedName("haveErrorN")
    public Boolean haveErrorN;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("statusIsCurrent")
    public Boolean statusIsCurrent;
    @SerializedName("id")
    public Integer id;
    @SerializedName("event")
    public Event event;
    @SerializedName("entityStrForAttach")
    public String entityStrForAttach;
    @SerializedName("objActionEnFP")
    public Integer objActionEnFP;
    @SerializedName("enabledAdvancedSearch")
    public Boolean enabledAdvancedSearch;
    @SerializedName("carPlateNo")
    public String carPlateNo;
    @SerializedName("lineIsRenderedForTariffEvent")
    public Boolean lineIsRenderedForTariffEvent;
    @SerializedName("maxAcceptDateForStartTime")
    public String maxAcceptDateForStartTime;
    @SerializedName("showDailyEventLastFuelForSelectedEvent")
    public Boolean showDailyEventLastFuelForSelectedEvent;
    @SerializedName("showTimeRemainingUpForExpire")
    public Boolean showTimeRemainingUpForExpire;
    @SerializedName("genericTypeClassName")
    public String genericTypeClassName;
    @SerializedName("typePrcEn_text")
    public String typePrcEnText;
    @SerializedName("editableIs")
    public Boolean editableIs;
    @SerializedName("processStatus_text")
    public String processStatusText;
    @SerializedName("typePrcEn")
    public Integer typePrcEn;
    @SerializedName("userCreation")
    public UserCreation userCreation;
    @SerializedName("typeGetCarEn_text")
    public String typeGetCarEnText;
    @SerializedName("statusChangeToInactiveIsPossible")
    public Boolean statusChangeToInactiveIsPossible;
    @SerializedName("showDailyEventLastFuelForNewAssignCarEvent")
    public Boolean showDailyEventLastFuelForNewAssignCarEvent;
    @SerializedName("statusChangeToActiveIsPossible")
    public Boolean statusChangeToActiveIsPossible;
    @SerializedName("inputMethodEn")
    public Integer inputMethodEn;
    @SerializedName("showStartTimeForView")
    public Boolean showStartTimeForView;
    @SerializedName("status_text")
    public String statusText;
    @SerializedName("searchOnCurrentStatus")
    public Boolean searchOnCurrentStatus;
    @SerializedName("status")
    public Integer status;
    @SerializedName("car")
    public Car car;
    @SerializedName("srvReq")
    public SrvReq srvReq;
    @SerializedName("carPlateText")
    public String carPlateText;

    protected DailyEvent(Parcel in) {
        if (in.readByte() == 0) {
            carTypeMdlEn = null;
        } else {
            carTypeMdlEn = in.readInt();
        }
        dateCreation = in.readString();
        if (in.readByte() == 0) {
            processStatus = null;
        } else {
            processStatus = in.readInt();
        }
        byte tmpShowDateCreationForView = in.readByte();
        showDateCreationForView = tmpShowDateCreationForView == 0 ? null : tmpShowDateCreationForView == 1;
        byte tmpSearchOnCurrentProcessStatus = in.readByte();
        searchOnCurrentProcessStatus = tmpSearchOnCurrentProcessStatus == 0 ? null : tmpSearchOnCurrentProcessStatus == 1;
        nameFvBefore = in.readString();
        statusIcon = in.readString();
        description = in.readString();
        byte tmpLastProcessBisDataIsValidForDoNext = in.readByte();
        lastProcessBisDataIsValidForDoNext = tmpLastProcessBisDataIsValidForDoNext == 0 ? null : tmpLastProcessBisDataIsValidForDoNext == 1;
        carTypeMdlEnText = in.readString();
        byte tmpPlanIs = in.readByte();
        planIs = tmpPlanIs == 0 ? null : tmpPlanIs == 1;
        inputMethodEnText = in.readString();
        if (in.readByte() == 0) {
            typeGetCarEn = null;
        } else {
            typeGetCarEn = in.readInt();
        }
        byte tmpHaveErrorN = in.readByte();
        haveErrorN = tmpHaveErrorN == 0 ? null : tmpHaveErrorN == 1;
        startTime = in.readString();
        byte tmpStatusIsCurrent = in.readByte();
        statusIsCurrent = tmpStatusIsCurrent == 0 ? null : tmpStatusIsCurrent == 1;
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        entityStrForAttach = in.readString();
        if (in.readByte() == 0) {
            objActionEnFP = null;
        } else {
            objActionEnFP = in.readInt();
        }
        byte tmpEnabledAdvancedSearch = in.readByte();
        enabledAdvancedSearch = tmpEnabledAdvancedSearch == 0 ? null : tmpEnabledAdvancedSearch == 1;
        carPlateNo = in.readString();
        byte tmpLineIsRenderedForTariffEvent = in.readByte();
        lineIsRenderedForTariffEvent = tmpLineIsRenderedForTariffEvent == 0 ? null : tmpLineIsRenderedForTariffEvent == 1;
        maxAcceptDateForStartTime = in.readString();
        byte tmpShowDailyEventLastFuelForSelectedEvent = in.readByte();
        showDailyEventLastFuelForSelectedEvent = tmpShowDailyEventLastFuelForSelectedEvent == 0 ? null : tmpShowDailyEventLastFuelForSelectedEvent == 1;
        byte tmpShowTimeRemainingUpForExpire = in.readByte();
        showTimeRemainingUpForExpire = tmpShowTimeRemainingUpForExpire == 0 ? null : tmpShowTimeRemainingUpForExpire == 1;
        genericTypeClassName = in.readString();
        typePrcEnText = in.readString();
        byte tmpEditableIs = in.readByte();
        editableIs = tmpEditableIs == 0 ? null : tmpEditableIs == 1;
        processStatusText = in.readString();
        if (in.readByte() == 0) {
            typePrcEn = null;
        } else {
            typePrcEn = in.readInt();
        }
        typeGetCarEnText = in.readString();
        byte tmpStatusChangeToInactiveIsPossible = in.readByte();
        statusChangeToInactiveIsPossible = tmpStatusChangeToInactiveIsPossible == 0 ? null : tmpStatusChangeToInactiveIsPossible == 1;
        byte tmpShowDailyEventLastFuelForNewAssignCarEvent = in.readByte();
        showDailyEventLastFuelForNewAssignCarEvent = tmpShowDailyEventLastFuelForNewAssignCarEvent == 0 ? null : tmpShowDailyEventLastFuelForNewAssignCarEvent == 1;
        byte tmpStatusChangeToActiveIsPossible = in.readByte();
        statusChangeToActiveIsPossible = tmpStatusChangeToActiveIsPossible == 0 ? null : tmpStatusChangeToActiveIsPossible == 1;
        if (in.readByte() == 0) {
            inputMethodEn = null;
        } else {
            inputMethodEn = in.readInt();
        }
        byte tmpShowStartTimeForView = in.readByte();
        showStartTimeForView = tmpShowStartTimeForView == 0 ? null : tmpShowStartTimeForView == 1;
        statusText = in.readString();
        byte tmpSearchOnCurrentStatus = in.readByte();
        searchOnCurrentStatus = tmpSearchOnCurrentStatus == 0 ? null : tmpSearchOnCurrentStatus == 1;
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        car = in.readParcelable(Car.class.getClassLoader());
        srvReq = in.readParcelable(SrvReq.class.getClassLoader());
        carPlateText = in.readString();
    }

    public static final Creator<DailyEvent> CREATOR = new Creator<DailyEvent>() {
        @Override
        public DailyEvent createFromParcel(Parcel in) {
            return new DailyEvent(in);
        }

        @Override
        public DailyEvent[] newArray(int size) {
            return new DailyEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (carTypeMdlEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(carTypeMdlEn);
        }
        parcel.writeString(dateCreation);
        if (processStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(processStatus);
        }
        parcel.writeByte((byte) (showDateCreationForView == null ? 0 : showDateCreationForView ? 1 : 2));
        parcel.writeByte((byte) (searchOnCurrentProcessStatus == null ? 0 : searchOnCurrentProcessStatus ? 1 : 2));
        parcel.writeString(nameFvBefore);
        parcel.writeString(statusIcon);
        parcel.writeString(description);
        parcel.writeByte((byte) (lastProcessBisDataIsValidForDoNext == null ? 0 : lastProcessBisDataIsValidForDoNext ? 1 : 2));
        parcel.writeString(carTypeMdlEnText);
        parcel.writeByte((byte) (planIs == null ? 0 : planIs ? 1 : 2));
        parcel.writeString(inputMethodEnText);
        if (typeGetCarEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(typeGetCarEn);
        }
        parcel.writeByte((byte) (haveErrorN == null ? 0 : haveErrorN ? 1 : 2));
        parcel.writeString(startTime);
        parcel.writeByte((byte) (statusIsCurrent == null ? 0 : statusIsCurrent ? 1 : 2));
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(entityStrForAttach);
        if (objActionEnFP == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(objActionEnFP);
        }
        parcel.writeByte((byte) (enabledAdvancedSearch == null ? 0 : enabledAdvancedSearch ? 1 : 2));
        parcel.writeString(carPlateNo);
        parcel.writeByte((byte) (lineIsRenderedForTariffEvent == null ? 0 : lineIsRenderedForTariffEvent ? 1 : 2));
        parcel.writeString(maxAcceptDateForStartTime);
        parcel.writeByte((byte) (showDailyEventLastFuelForSelectedEvent == null ? 0 : showDailyEventLastFuelForSelectedEvent ? 1 : 2));
        parcel.writeByte((byte) (showTimeRemainingUpForExpire == null ? 0 : showTimeRemainingUpForExpire ? 1 : 2));
        parcel.writeString(genericTypeClassName);
        parcel.writeString(typePrcEnText);
        parcel.writeByte((byte) (editableIs == null ? 0 : editableIs ? 1 : 2));
        parcel.writeString(processStatusText);
        if (typePrcEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(typePrcEn);
        }
        parcel.writeString(typeGetCarEnText);
        parcel.writeByte((byte) (statusChangeToInactiveIsPossible == null ? 0 : statusChangeToInactiveIsPossible ? 1 : 2));
        parcel.writeByte((byte) (showDailyEventLastFuelForNewAssignCarEvent == null ? 0 : showDailyEventLastFuelForNewAssignCarEvent ? 1 : 2));
        parcel.writeByte((byte) (statusChangeToActiveIsPossible == null ? 0 : statusChangeToActiveIsPossible ? 1 : 2));
        if (inputMethodEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(inputMethodEn);
        }
        parcel.writeByte((byte) (showStartTimeForView == null ? 0 : showStartTimeForView ? 1 : 2));
        parcel.writeString(statusText);
        parcel.writeByte((byte) (searchOnCurrentStatus == null ? 0 : searchOnCurrentStatus ? 1 : 2));
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        parcel.writeParcelable(car, i);
        parcel.writeParcelable(srvReq, i);
        parcel.writeString(carPlateText);
    }
}
