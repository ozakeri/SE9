
package com.example.sino.model.carinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Color {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("status")
    @Expose
    public Integer status;

}
