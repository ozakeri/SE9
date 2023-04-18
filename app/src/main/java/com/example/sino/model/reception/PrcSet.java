
package com.example.sino.model.reception;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PrcSet implements Parcelable {

    @SerializedName("prcSetVO")
    public PrcSetVO prcSetVO;
    @SerializedName("permissionId")
    public Integer permissionId;
    @SerializedName("appPermission")
    public String appPermission;
    @SerializedName("permissionName")
    public String permissionName;
    @SerializedName("prcSetId")
    public String prcSetId;

    protected PrcSet(Parcel in) {
        if (in.readByte() == 0) {
            permissionId = null;
        } else {
            permissionId = in.readInt();
        }
        appPermission = in.readString();
        permissionName = in.readString();
        prcSetId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (permissionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(permissionId);
        }
        dest.writeString(appPermission);
        dest.writeString(permissionName);
        dest.writeString(prcSetId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PrcSet> CREATOR = new Creator<PrcSet>() {
        @Override
        public PrcSet createFromParcel(Parcel in) {
            return new PrcSet(in);
        }

        @Override
        public PrcSet[] newArray(int size) {
            return new PrcSet[size];
        }
    };
}
