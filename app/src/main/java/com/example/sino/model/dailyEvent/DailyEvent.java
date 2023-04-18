
package com.example.sino.model.dailyEvent;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.sino.model.carinfo.Car;
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
    @SerializedName("carPlateText")
    public String carPlateText;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
