
package com.example.sino.model.reception;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sino.model.carinfo.Car;
import com.example.sino.model.company.Company;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProSrv implements Parcelable {

    @SerializedName("proSrvId")
    public String proSrvId;
    @SerializedName("entityStrForAttach")
    public String entityStrForAttach;
    @SerializedName("kmCar")
    public Integer kmCar;
    @SerializedName("serBackNo")
    public Integer serBackNo;
    @SerializedName("chassis")
    public String chassis;
    @SerializedName("serviceTypeEn")
    public Integer serviceTypeEn;
    @SerializedName("serviceTypeEn_text")
    public String serviceTypeEnText;
    @SerializedName("yLongitude")
    public String yLongitude;
    @SerializedName("inputMethodEn_text")
    public String inputMethodEnText;
    @SerializedName("car")
    public Car car;
    @SerializedName("inputMethodEn")
    public Integer inputMethodEn;
    @SerializedName("company")
    public Company company;
    @SerializedName("startDate")
    public String startDate;
    @SerializedName("dateCreation")
    public String dateCreation;
    @SerializedName("xLatitude")
    public String xLatitude;
    @SerializedName("prcSetId")
    public String prcSetId;
    @SerializedName("prcSetList")
    public List<PrcSet> prcSetList = null;
    @SerializedName("prcDataList")
    public List<PrcData> prcDataList = null;

    @SerializedName("licPro")
    public LicPro licPro = null;

    @SerializedName("srvReq")
    public SrvReq srvReq = null;

    protected ProSrv(Parcel in) {
        proSrvId = in.readString();
        entityStrForAttach = in.readString();
        if (in.readByte() == 0) {
            kmCar = null;
        } else {
            kmCar = in.readInt();
        }
        if (in.readByte() == 0) {
            serBackNo = null;
        } else {
            serBackNo = in.readInt();
        }
        chassis = in.readString();
        if (in.readByte() == 0) {
            serviceTypeEn = null;
        } else {
            serviceTypeEn = in.readInt();
        }
        serviceTypeEnText = in.readString();
        yLongitude = in.readString();
        inputMethodEnText = in.readString();
        car = in.readParcelable(Car.class.getClassLoader());
        if (in.readByte() == 0) {
            inputMethodEn = null;
        } else {
            inputMethodEn = in.readInt();
        }
        startDate = in.readString();
        xLatitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proSrvId);
        dest.writeString(entityStrForAttach);
        if (kmCar == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(kmCar);
        }
        if (serBackNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serBackNo);
        }
        dest.writeString(chassis);
        if (serviceTypeEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serviceTypeEn);
        }
        dest.writeString(serviceTypeEnText);
        dest.writeString(yLongitude);
        dest.writeString(inputMethodEnText);
        dest.writeParcelable(car, flags);
        if (inputMethodEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(inputMethodEn);
        }
        dest.writeString(startDate);
        dest.writeString(xLatitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProSrv> CREATOR = new Creator<ProSrv>() {
        @Override
        public ProSrv createFromParcel(Parcel in) {
            return new ProSrv(in);
        }

        @Override
        public ProSrv[] newArray(int size) {
            return new ProSrv[size];
        }
    };
}
