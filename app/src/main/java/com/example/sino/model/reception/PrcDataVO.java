
package com.example.sino.model.reception;

import com.google.gson.annotations.SerializedName;

public class PrcDataVO {

    @SerializedName("prcSet")
    public PrcSet__1 prcSet;
    @SerializedName("appPermission")
    public String appPermission;
    @SerializedName("employeeDo")
    public String employeeDo;
    @SerializedName("prcDataId")
    public String prcDataId;
    @SerializedName("entityId")
    public String entityId;
    @SerializedName("entityNameEn")
    public String entityNameEn;
    @SerializedName("nameFv")
    public String nameFv;
    @SerializedName("startDate")
    public String startDate;
    @SerializedName("ProcessStrucSettingId")
    public String ProcessStrucSettingId;
    @SerializedName("description")
    public String description;
    @SerializedName("doSuccess")
    public Boolean doSuccess;

    @SerializedName("dateCreation")
    public String dateCreation;

    @SerializedName("processStatus")
    public int processStatus;

}
