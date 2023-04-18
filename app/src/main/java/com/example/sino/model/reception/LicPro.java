
package com.example.sino.model.reception;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sino.model.carinfo.Car;
import com.example.sino.model.company.Company;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LicPro implements Parcelable {

    @SerializedName("licTypeEn_text")
    public String licTypeEn_text;

    @SerializedName("licNo")
    public String licNo;

    @SerializedName("endDatePlan")
    public String endDatePlan;

    protected LicPro(Parcel in) {
        licTypeEn_text = in.readString();
        licNo = in.readString();
        endDatePlan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(licTypeEn_text);
        dest.writeString(licNo);
        dest.writeString(endDatePlan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LicPro> CREATOR = new Creator<LicPro>() {
        @Override
        public LicPro createFromParcel(Parcel in) {
            return new LicPro(in);
        }

        @Override
        public LicPro[] newArray(int size) {
            return new LicPro[size];
        }
    };
}
