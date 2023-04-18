
package com.example.sino.model.form;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class SurveyForm {

    @SerializedName("nameFv")
    
    public String nameFv;
    @SerializedName("processStatus")
    
    public Integer processStatus;
    @SerializedName("surveyFormQuestionList")
    
    public List<SurveyFormQuestion> surveyFormQuestionList = null;
    @SerializedName("endDate")
    
    public String endDate;
    @SerializedName("description")
    
    public String description;
    @SerializedName("resultMax")
    
    public Integer resultMax;
    @SerializedName("formTypeEN_text")
    
    public String formTypeENText;
    @SerializedName("accessForAllEn_text")
    
    public String accessForAllEnText;
    @SerializedName("id")
    
    public Long id;
    @SerializedName("yearTarget")
    
    public Integer yearTarget;
    @SerializedName("entityStrForAttach")
    
    public String entityStrForAttach;
    @SerializedName("monthTarget")
    
    public Integer monthTarget;
    @SerializedName("autoCompleteLabel")
    
    public String autoCompleteLabel;
    @SerializedName("minScore")
    
    public Integer minScore;
    @SerializedName("inputValuesDefault")
    
    public String inputValuesDefault;
    @SerializedName("maxScore")
    
    public Integer maxScore;
    @SerializedName("processStatus_text")
    
    public String processStatusText;
    @SerializedName("formTypeEN")
    
    public Integer formTypeEN;
    @SerializedName("accessForAllEn")
    
    public Integer accessForAllEn;
    @SerializedName("name")
    
    public String name;
    @SerializedName("endDateIsRequired")
    
    public Boolean endDateIsRequired;
    @SerializedName("status_text")
    
    public String statusText;
    @SerializedName("startDate")
    
    public String startDate;
    @SerializedName("resultAvg")
    
    public Integer resultAvg;
    @SerializedName("resultMin")
    
    public Integer resultMin;
    @SerializedName("status")
    
    public Integer status;

}
