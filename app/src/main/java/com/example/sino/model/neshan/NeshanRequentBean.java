
package com.example.sino.model.neshan;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NeshanRequentBean {

    @SerializedName("status")
    public String status;
    @SerializedName("rows")
    public List<Row> rows = null;
    @SerializedName("origin_addresses")
    public List<String> originAddresses = null;
    @SerializedName("destination_addresses")
    public List<String> destinationAddresses = null;
    @SerializedName("state")
    public String state;
    @SerializedName("formatted_address")
    public String formatted_address;

}
