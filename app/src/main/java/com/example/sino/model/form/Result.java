
package com.example.sino.model.form;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("surveyFormList")
    public List<SurveyForm> surveyFormList = null;

    @SerializedName("savedComplaintReport")
    public SavedComplaintReport savedComplaintReport = null;

    @SerializedName("proServiceVOId")
    public Long proServiceVOId = null;

}
