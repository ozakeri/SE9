package com.example.sino.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AppUser")
public class AppUser {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public Integer id;
    @SerializedName("family")
    @Expose
    public String family;

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
