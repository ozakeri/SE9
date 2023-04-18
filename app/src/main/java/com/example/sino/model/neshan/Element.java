
package com.example.sino.model.neshan;

import com.google.gson.annotations.SerializedName;

public class Element {

    @SerializedName("status")
    public String status;
    @SerializedName("duration")
    public Duration duration;
    @SerializedName("distance")
    public Distance distance;

}
