
package com.example.sino.model.carinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle {

    @SerializedName("dateCreation")
    @Expose
    public String dateCreation;
    @SerializedName("autoCompleteLabel")
    @Expose
    public String autoCompleteLabel;
    @SerializedName("nameFv")
    @Expose
    public String nameFv;
    @SerializedName("processStatus")
    @Expose
    public Integer processStatus;
    @SerializedName("dateLastChange")
    @Expose
    public String dateLastChange;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("aveFuelUsage")
    @Expose
    public Integer aveFuelUsage;
    @SerializedName("processStatus_text")
    @Expose
    public String processStatusText;
    @SerializedName("vehicleType_text")
    @Expose
    public String vehicleTypeText;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("groupLevelEn")
    @Expose
    public Integer groupLevelEn;
    @SerializedName("groupLevelEn_text")
    @Expose
    public String groupLevelEnText;
    @SerializedName("minFuelUsage")
    @Expose
    public Integer minFuelUsage;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("maxFuelUsage")
    @Expose
    public Integer maxFuelUsage;
    @SerializedName("vehicleType")
    @Expose
    public Integer vehicleType;
    @SerializedName("status")
    @Expose
    public Integer status;

}
