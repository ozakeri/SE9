
package com.example.sino.model.carinfo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PictureAF {

    @SerializedName("dateCreation")
    @Expose
    public String dateCreation;
    @SerializedName("nameFv")
    @Expose
    public String nameFv;
    @SerializedName("entityNameEn")
    @Expose
    public Integer entityNameEn;
    @SerializedName("typeFV")
    @Expose
    public String typeFV;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("entityNameEn_text")
    @Expose
    public String entityNameEnText;
    @SerializedName("entityId")
    @Expose
    public Integer entityId;
    @SerializedName("userFileName")
    @Expose
    public String userFileName;
    @SerializedName("entityStr")
    @Expose
    public String entityStr;
    @SerializedName("nameFv2")
    @Expose
    public String nameFv2;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("attachSizeB")
    @Expose
    public Integer attachSizeB;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("referenceTable")
    @Expose
    public String referenceTable;
    @SerializedName("pictureBytes")
    @Expose
    public List<Integer> pictureBytes = null;

}
