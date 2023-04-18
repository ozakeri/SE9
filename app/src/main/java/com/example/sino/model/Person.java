
package com.example.sino.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Person implements Parcelable {

    @SerializedName("liveStatus_text")
    public String liveStatusText;
    @SerializedName("fatherName")
    public String fatherName;
    @SerializedName("personTypeEn_text")
    public String personTypeEnText;
    @SerializedName("gender_text")
    public String genderText;
    @SerializedName("nationalCode")
    public String nationalCode;
    @SerializedName("gender")
    public Integer gender;
    @SerializedName("nameFv")
    public String nameFv;
    @SerializedName("processEnForAttach")
    public Integer processEnForAttach;
    @SerializedName("infoIsSec")
    public Boolean infoIsSec;
    @SerializedName("nationalCodeLabelFV")
    public String nationalCodeLabelFV;
    @SerializedName("idNo")
    public String idNo;
    @SerializedName("nationalCodeTitleStrFV")
    public String nationalCodeTitleStrFV;
    @SerializedName("lastEducationField")
    public String lastEducationField;
    @SerializedName("personTypeEn")
    public Integer personTypeEn;
    @SerializedName("lastEducationUni")
    public String lastEducationUni;
    @SerializedName("nameFvCompany")
    public String nameFvCompany;
    @SerializedName("creationProcessEnIsValid")
    public Boolean creationProcessEnIsValid;
    @SerializedName("lastEducationDate")
    public String lastEducationDate;
    @SerializedName("liveStatus")
    public Integer liveStatus;
    @SerializedName("idSerial")
    public String idSerial;
    @SerializedName("entityStrForAttach")
    public String entityStrForAttach;
    @SerializedName("autoCompleteLabel")
    public String autoCompleteLabel;
    @SerializedName("educationStrFV")
    public String educationStrFV;
    @SerializedName("maritalStatusEn_text")
    public String maritalStatusEnText;
    @SerializedName("birthDateLabelFV")
    public String birthDateLabelFV;
    @SerializedName("birthdayDate")
    public String birthdayDate;
    @SerializedName("nameFv2")
    public String nameFv2;
    @SerializedName("editIsPossible")
    public Boolean editIsPossible;
    @SerializedName("processEnForAttach_text")
    public String processEnForAttachText;
    @SerializedName("lastEducationLevel_text")
    public String lastEducationLevelText;
    @SerializedName("insuranceNo")
    public String insuranceNo;
    @SerializedName("name")
    public String name;
    @SerializedName("lastEducationLevel")
    public Integer lastEducationLevel;
    @SerializedName("ecoCodeIsReq")
    public Boolean ecoCodeIsReq;
    @SerializedName("maritalStatusEn")
    public Integer maritalStatusEn;
    @SerializedName("family")
    public String family;
    @SerializedName("address")
    public Address address;

    protected Person(Parcel in) {
        liveStatusText = in.readString();
        fatherName = in.readString();
        personTypeEnText = in.readString();
        genderText = in.readString();
        nationalCode = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        nameFv = in.readString();
        if (in.readByte() == 0) {
            processEnForAttach = null;
        } else {
            processEnForAttach = in.readInt();
        }
        byte tmpInfoIsSec = in.readByte();
        infoIsSec = tmpInfoIsSec == 0 ? null : tmpInfoIsSec == 1;
        nationalCodeLabelFV = in.readString();
        idNo = in.readString();
        nationalCodeTitleStrFV = in.readString();
        lastEducationField = in.readString();
        if (in.readByte() == 0) {
            personTypeEn = null;
        } else {
            personTypeEn = in.readInt();
        }
        lastEducationUni = in.readString();
        nameFvCompany = in.readString();
        byte tmpCreationProcessEnIsValid = in.readByte();
        creationProcessEnIsValid = tmpCreationProcessEnIsValid == 0 ? null : tmpCreationProcessEnIsValid == 1;
        lastEducationDate = in.readString();
        if (in.readByte() == 0) {
            liveStatus = null;
        } else {
            liveStatus = in.readInt();
        }
        idSerial = in.readString();
        entityStrForAttach = in.readString();
        autoCompleteLabel = in.readString();
        educationStrFV = in.readString();
        maritalStatusEnText = in.readString();
        birthDateLabelFV = in.readString();
        birthdayDate = in.readString();
        nameFv2 = in.readString();
        byte tmpEditIsPossible = in.readByte();
        editIsPossible = tmpEditIsPossible == 0 ? null : tmpEditIsPossible == 1;
        processEnForAttachText = in.readString();
        lastEducationLevelText = in.readString();
        insuranceNo = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            lastEducationLevel = null;
        } else {
            lastEducationLevel = in.readInt();
        }
        byte tmpEcoCodeIsReq = in.readByte();
        ecoCodeIsReq = tmpEcoCodeIsReq == 0 ? null : tmpEcoCodeIsReq == 1;
        if (in.readByte() == 0) {
            maritalStatusEn = null;
        } else {
            maritalStatusEn = in.readInt();
        }
        family = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(liveStatusText);
        dest.writeString(fatherName);
        dest.writeString(personTypeEnText);
        dest.writeString(genderText);
        dest.writeString(nationalCode);
        if (gender == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gender);
        }
        dest.writeString(nameFv);
        if (processEnForAttach == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(processEnForAttach);
        }
        dest.writeByte((byte) (infoIsSec == null ? 0 : infoIsSec ? 1 : 2));
        dest.writeString(nationalCodeLabelFV);
        dest.writeString(idNo);
        dest.writeString(nationalCodeTitleStrFV);
        dest.writeString(lastEducationField);
        if (personTypeEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(personTypeEn);
        }
        dest.writeString(lastEducationUni);
        dest.writeString(nameFvCompany);
        dest.writeByte((byte) (creationProcessEnIsValid == null ? 0 : creationProcessEnIsValid ? 1 : 2));
        dest.writeString(lastEducationDate);
        if (liveStatus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(liveStatus);
        }
        dest.writeString(idSerial);
        dest.writeString(entityStrForAttach);
        dest.writeString(autoCompleteLabel);
        dest.writeString(educationStrFV);
        dest.writeString(maritalStatusEnText);
        dest.writeString(birthDateLabelFV);
        dest.writeString(birthdayDate);
        dest.writeString(nameFv2);
        dest.writeByte((byte) (editIsPossible == null ? 0 : editIsPossible ? 1 : 2));
        dest.writeString(processEnForAttachText);
        dest.writeString(lastEducationLevelText);
        dest.writeString(insuranceNo);
        dest.writeString(name);
        if (lastEducationLevel == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(lastEducationLevel);
        }
        dest.writeByte((byte) (ecoCodeIsReq == null ? 0 : ecoCodeIsReq ? 1 : 2));
        if (maritalStatusEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maritalStatusEn);
        }
        dest.writeString(family);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
