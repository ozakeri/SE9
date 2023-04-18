
package com.example.sino.model.reception;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class JsonArrayAttach {

    @SerializedName("attachFileJsonArray")
    public List<Integer> attachFileJsonArray = null;
    @SerializedName("attachFileId")
    public Integer attachFileId;

}
