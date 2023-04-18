package com.example.sino.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "permission")
public class UserPermission implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String permissionName;
    private Long userId;

    public UserPermission() {
    }

    protected UserPermission(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        permissionName = in.readString();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(permissionName);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserPermission> CREATOR = new Creator<UserPermission>() {
        @Override
        public UserPermission createFromParcel(Parcel in) {
            return new UserPermission(in);
        }

        @Override
        public UserPermission[] newArray(int size) {
            return new UserPermission[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
