package com.example.sino.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.sino.model.company.Company;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AppUser")
public class AppUser {

    @SerializedName("name")
    public String name;
    @SerializedName("id")
    @PrimaryKey
    public Integer id;
    @SerializedName("family")
    public String family;
    @SerializedName("company")
    public Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
