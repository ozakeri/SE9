
package com.example.sino.model.documentversion;



import com.google.gson.annotations.SerializedName;

public class Document {

    @SerializedName("entityStrForAttach")
    
    public String entityStrForAttach;
    @SerializedName("documentTypeEn_text")
    
    public String documentTypeEnText;
    @SerializedName("lastDocumentVersion")
    
    public LastDocumentVersion lastDocumentVersion;
    @SerializedName("autoCompleteLabel")
    
    public String autoCompleteLabel;
    @SerializedName("nameFv")
    
    public String nameFv;
    @SerializedName("documentTypeEn")
    
    public Integer documentTypeEn;
    @SerializedName("name")
    
    public String name;
    @SerializedName("companyTypeEn")
    
    public Integer companyTypeEn;
    @SerializedName("companyTypeEn_text")
    
    public String companyTypeEnText;
    @SerializedName("docDate")
    
    public String docDate;
    @SerializedName("nameFvApp")
    
    public String nameFvApp;

}
