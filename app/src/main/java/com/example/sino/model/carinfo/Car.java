
package com.example.sino.model.carinfo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.SeProModelConverter;
import com.google.gson.annotations.SerializedName;

public class Car implements Parcelable {

    @SerializedName("dateCreation")
    public String dateCreation;
    @SerializedName("nameFv")
    public String nameFv;
    @SerializedName("processStatus")
    public Integer processStatus;
    @SerializedName("ageFV")
    public Integer ageFV;
    @SerializedName("vinNo")
    public String vinNo;
    @SerializedName("usageTypeEn")
    public Integer usageTypeEn;
    @SerializedName("activityStatusCanBeWorkFS")
    public Boolean activityStatusCanBeWorkFS;
    @SerializedName("engineFuelType")
    public Integer engineFuelType;
    @SerializedName("propertyCode")
    public String propertyCode;
    @SerializedName("carStatusInParkDescFV")
    public String carStatusInParkDescFV;
    @SerializedName("activityStatus")
    public Integer activityStatus;
    @SerializedName("id")
    public Integer id;
    @SerializedName("vehicleTypeEnFV_text")
    public String vehicleTypeEnFVText;
    @SerializedName("carMustDispatchForAdvertisement")
    public Boolean carMustDispatchForAdvertisement;
    @SerializedName("engineNo")
    public String engineNo;
    @SerializedName("entityStrForAttach")
    public String entityStrForAttach;
    @SerializedName("fuelCardNo")
    public String fuelCardNo;
    @SerializedName("engineFuelType_text")
    public String engineFuelTypeText;
    @SerializedName("showTimeRemainingUpForExpire")
    public Boolean showTimeRemainingUpForExpire;
    @SerializedName("chassis")
    public String chassis;
    @SerializedName("carDeliverIs")
    public Integer carDeliverIs;
    @SerializedName("urbanFleetIs")
    public Boolean urbanFleetIs;
    @SerializedName("plateText")
    public String plateText;
    @SerializedName("afcCode")
    public String afcCode;
    @SerializedName("activityStatusIsStopFS")
    public Boolean activityStatusIsStopFS;
    @SerializedName("status_text")
    public String statusText;
    @SerializedName("status")
    public Integer status;
    @SerializedName("seatTypeEn")
    public Integer seatTypeEn;
    @SerializedName("color")
    public Color color;
    @SerializedName("usageTypeEn_text")
    public String usageTypeEnText;
    @SerializedName("description")
    public String description;
    @SerializedName("colorTabLicAttachment")
    public String colorTabLicAttachment;
    @SerializedName("activityStatus_text")
    public String activityStatusText;
    @SerializedName("carStatusInParkNameFV")
    public String carStatusInParkNameFV;
    @SerializedName("enableDeliverPerson")
    public Integer enableDeliverPerson;
    @SerializedName("productionYear")
    public Integer productionYear;
    @SerializedName("carDeliverIs_text")
    public String carDeliverIsText;
    @SerializedName("colorStyleClass")
    public String colorStyleClass;
    @SerializedName("pictureAF")
    public PictureAF pictureAF;
    @SerializedName("vehicle")
    public Vehicle vehicle;
    @SerializedName("enableDeliverPerson_text")
    public String enableDeliverPersonText;
    @SerializedName("seatTypeEn_text")
    public String seatTypeEnText;
    @SerializedName("deliveryDate")
    public String deliveryDate;
    @SerializedName("reopenEtGpsLic")
    public Boolean reopenEtGpsLic;
    @SerializedName("autoCompleteLabel")
    public String autoCompleteLabel;
    @SerializedName("plateNo")
    public String plateNo;
    @SerializedName("carStopReasonFV")
    public String carStopReasonFV;
    @SerializedName("coolerStatus")
    public Integer coolerStatus;
    @SerializedName("changeCarPlateIsPossible")
    public Boolean changeCarPlateIsPossible;
    @SerializedName("processStatus_text")
    public String processStatusText;
    @SerializedName("colorTabCar")
    public String colorTabCar;
    @SerializedName("busFleetIs")
    public Boolean busFleetIs;
    @SerializedName("vehicleTypeEnFV")
    public Integer vehicleTypeEnFV;
    @SerializedName("fuelType")
    public Integer fuelType;
    @SerializedName("nameFv1")
    public String nameFv1;
    @SerializedName("carStatusInParkStrFV")
    public String carStatusInParkStrFV;
    @SerializedName("nameFv2")
    public String nameFv2;
    @SerializedName("colorTabCarProfit")
    public String colorTabCarProfit;
    @SerializedName("nameFvOnCode")
    public String nameFvOnCode;
    @SerializedName("coolerStatus_text")
    public String coolerStatusText;
    @SerializedName("carStopDescFV")
    public String carStopDescFV;
    @SerializedName("fuelType_text")
    public String fuelTypeText;
    @SerializedName("colorTabLicCarUF")
    public String colorTabLicCarUF;
    @SerializedName("seProModel")
    @TypeConverters(SeProModelConverter.class)
    public SeProModel seProModel;
    @SerializedName("licProTypeName")
    public String licProTypeName;
    @SerializedName("seLicPro")
    public SeLicPro seLicPro;
    @SerializedName("carSosIsEditable")
    public Boolean carSosIsEditable;

    @SerializedName("carTypeFa")
    public String carTypeFa;

    @SerializedName("gurActFV")
    public Boolean gurActFV;

    @SerializedName("gurDateEndFV")
    public String gurDateEndFV;

    @SerializedName("gurDayRemFv")
    public String gurDayRemFv;


    public Car() {
    }

    protected Car(Parcel in) {
        dateCreation = in.readString();
        nameFv = in.readString();
        if (in.readByte() == 0) {
            processStatus = null;
        } else {
            processStatus = in.readInt();
        }
        if (in.readByte() == 0) {
            ageFV = null;
        } else {
            ageFV = in.readInt();
        }
        vinNo = in.readString();
        if (in.readByte() == 0) {
            usageTypeEn = null;
        } else {
            usageTypeEn = in.readInt();
        }
        byte tmpActivityStatusCanBeWorkFS = in.readByte();
        activityStatusCanBeWorkFS = tmpActivityStatusCanBeWorkFS == 0 ? null : tmpActivityStatusCanBeWorkFS == 1;
        if (in.readByte() == 0) {
            engineFuelType = null;
        } else {
            engineFuelType = in.readInt();
        }
        propertyCode = in.readString();
        carStatusInParkDescFV = in.readString();
        if (in.readByte() == 0) {
            activityStatus = null;
        } else {
            activityStatus = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        vehicleTypeEnFVText = in.readString();
        byte tmpCarMustDispatchForAdvertisement = in.readByte();
        carMustDispatchForAdvertisement = tmpCarMustDispatchForAdvertisement == 0 ? null : tmpCarMustDispatchForAdvertisement == 1;
        engineNo = in.readString();
        entityStrForAttach = in.readString();
        fuelCardNo = in.readString();
        engineFuelTypeText = in.readString();
        byte tmpShowTimeRemainingUpForExpire = in.readByte();
        showTimeRemainingUpForExpire = tmpShowTimeRemainingUpForExpire == 0 ? null : tmpShowTimeRemainingUpForExpire == 1;
        chassis = in.readString();
        if (in.readByte() == 0) {
            carDeliverIs = null;
        } else {
            carDeliverIs = in.readInt();
        }
        byte tmpUrbanFleetIs = in.readByte();
        urbanFleetIs = tmpUrbanFleetIs == 0 ? null : tmpUrbanFleetIs == 1;
        plateText = in.readString();
        afcCode = in.readString();
        byte tmpActivityStatusIsStopFS = in.readByte();
        activityStatusIsStopFS = tmpActivityStatusIsStopFS == 0 ? null : tmpActivityStatusIsStopFS == 1;
        statusText = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        if (in.readByte() == 0) {
            seatTypeEn = null;
        } else {
            seatTypeEn = in.readInt();
        }
        usageTypeEnText = in.readString();
        description = in.readString();
        colorTabLicAttachment = in.readString();
        activityStatusText = in.readString();
        carStatusInParkNameFV = in.readString();
        if (in.readByte() == 0) {
            enableDeliverPerson = null;
        } else {
            enableDeliverPerson = in.readInt();
        }
        if (in.readByte() == 0) {
            productionYear = null;
        } else {
            productionYear = in.readInt();
        }
        carDeliverIsText = in.readString();
        colorStyleClass = in.readString();
        enableDeliverPersonText = in.readString();
        seatTypeEnText = in.readString();
        deliveryDate = in.readString();
        byte tmpReopenEtGpsLic = in.readByte();
        reopenEtGpsLic = tmpReopenEtGpsLic == 0 ? null : tmpReopenEtGpsLic == 1;
        autoCompleteLabel = in.readString();
        plateNo = in.readString();
        carStopReasonFV = in.readString();
        if (in.readByte() == 0) {
            coolerStatus = null;
        } else {
            coolerStatus = in.readInt();
        }
        byte tmpChangeCarPlateIsPossible = in.readByte();
        changeCarPlateIsPossible = tmpChangeCarPlateIsPossible == 0 ? null : tmpChangeCarPlateIsPossible == 1;
        processStatusText = in.readString();
        colorTabCar = in.readString();
        byte tmpBusFleetIs = in.readByte();
        busFleetIs = tmpBusFleetIs == 0 ? null : tmpBusFleetIs == 1;
        if (in.readByte() == 0) {
            vehicleTypeEnFV = null;
        } else {
            vehicleTypeEnFV = in.readInt();
        }
        if (in.readByte() == 0) {
            fuelType = null;
        } else {
            fuelType = in.readInt();
        }
        nameFv1 = in.readString();
        carStatusInParkStrFV = in.readString();
        nameFv2 = in.readString();
        colorTabCarProfit = in.readString();
        nameFvOnCode = in.readString();
        coolerStatusText = in.readString();
        carStopDescFV = in.readString();
        fuelTypeText = in.readString();
        colorTabLicCarUF = in.readString();
        licProTypeName = in.readString();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(dateCreation);
        parcel.writeString(nameFv);
        if (processStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(processStatus);
        }
        if (ageFV == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(ageFV);
        }
        parcel.writeString(vinNo);
        if (usageTypeEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(usageTypeEn);
        }
        parcel.writeByte((byte) (activityStatusCanBeWorkFS == null ? 0 : activityStatusCanBeWorkFS ? 1 : 2));
        if (engineFuelType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(engineFuelType);
        }
        parcel.writeString(propertyCode);
        parcel.writeString(carStatusInParkDescFV);
        if (activityStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(activityStatus);
        }
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(vehicleTypeEnFVText);
        parcel.writeByte((byte) (carMustDispatchForAdvertisement == null ? 0 : carMustDispatchForAdvertisement ? 1 : 2));
        parcel.writeString(engineNo);
        parcel.writeString(entityStrForAttach);
        parcel.writeString(fuelCardNo);
        parcel.writeString(engineFuelTypeText);
        parcel.writeByte((byte) (showTimeRemainingUpForExpire == null ? 0 : showTimeRemainingUpForExpire ? 1 : 2));
        parcel.writeString(chassis);
        if (carDeliverIs == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(carDeliverIs);
        }
        parcel.writeByte((byte) (urbanFleetIs == null ? 0 : urbanFleetIs ? 1 : 2));
        parcel.writeString(plateText);
        parcel.writeString(afcCode);
        parcel.writeByte((byte) (activityStatusIsStopFS == null ? 0 : activityStatusIsStopFS ? 1 : 2));
        parcel.writeString(statusText);
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        if (seatTypeEn == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(seatTypeEn);
        }
        parcel.writeString(usageTypeEnText);
        parcel.writeString(description);
        parcel.writeString(colorTabLicAttachment);
        parcel.writeString(activityStatusText);
        parcel.writeString(carStatusInParkNameFV);
        if (enableDeliverPerson == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(enableDeliverPerson);
        }
        if (productionYear == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(productionYear);
        }
        parcel.writeString(carDeliverIsText);
        parcel.writeString(colorStyleClass);
        parcel.writeString(enableDeliverPersonText);
        parcel.writeString(seatTypeEnText);
        parcel.writeString(deliveryDate);
        parcel.writeByte((byte) (reopenEtGpsLic == null ? 0 : reopenEtGpsLic ? 1 : 2));
        parcel.writeString(autoCompleteLabel);
        parcel.writeString(plateNo);
        parcel.writeString(carStopReasonFV);
        if (coolerStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(coolerStatus);
        }
        parcel.writeByte((byte) (changeCarPlateIsPossible == null ? 0 : changeCarPlateIsPossible ? 1 : 2));
        parcel.writeString(processStatusText);
        parcel.writeString(colorTabCar);
        parcel.writeByte((byte) (busFleetIs == null ? 0 : busFleetIs ? 1 : 2));
        if (vehicleTypeEnFV == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(vehicleTypeEnFV);
        }
        if (fuelType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(fuelType);
        }
        parcel.writeString(nameFv1);
        parcel.writeString(carStatusInParkStrFV);
        parcel.writeString(nameFv2);
        parcel.writeString(colorTabCarProfit);
        parcel.writeString(nameFvOnCode);
        parcel.writeString(coolerStatusText);
        parcel.writeString(carStopDescFV);
        parcel.writeString(fuelTypeText);
        parcel.writeString(colorTabLicCarUF);
        parcel.writeString(licProTypeName);
    }
}
