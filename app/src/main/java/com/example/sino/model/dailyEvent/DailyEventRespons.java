
package com.example.sino.model.dailyEvent;

import com.example.sino.model.ResultBean;
import com.google.gson.annotations.SerializedName;

public class DailyEventRespons {

    @SerializedName("SUCCESS")
    public String success;
    @SerializedName("RESULT")
    public ResultBean result;
    @SerializedName("ERROR")
    public String ERROR;

}
