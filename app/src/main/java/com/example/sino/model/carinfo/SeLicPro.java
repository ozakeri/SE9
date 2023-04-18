package com.example.sino.model.carinfo;

import com.google.gson.annotations.SerializedName;

public class SeLicPro {
    @SerializedName("startDate")
    public String startDate;
    @SerializedName("endDate")
    public String endDate;
    @SerializedName("endDatePlan")
    public String endDatePlan;
    @SerializedName("licTypeEn_text")
    public String licTypeEn_text;
    @SerializedName("licTypeEn")
    public Integer licTypeEn;
}
