
package com.example.sino.model;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("address")
    public String address;

    @SerializedName("addressType")
    public Integer addressType;

    @SerializedName("mobileNo")
    public String mobileNo;

    @SerializedName("postalCode")
    public String postalCode;

    @SerializedName("telNo")
    public String telNo;



}
