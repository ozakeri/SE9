
package com.example.sino.model.form;
import com.google.gson.annotations.SerializedName;


public class SurveyFormQuestion {

    @SerializedName("surveyQuestions")
    public SurveyQuestions surveyQuestions;
    @SerializedName("groupName")
    public String groupName;
    @SerializedName("autoCompleteLabel")
    public String autoCompleteLabel;
    @SerializedName("nameFv")
    public String nameFv;
    @SerializedName("processStatus")
    public Integer processStatus;
    @SerializedName("groupId")
    public Long groupId;
    @SerializedName("coefficient")
    public Integer coefficient;
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public Long id;
    @SerializedName("status_text")
    public String statusText;
    @SerializedName("processStatus_text")
    public String processStatusText;
    @SerializedName("status")
    public Integer status;

}
