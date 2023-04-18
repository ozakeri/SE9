package com.example.sino;

import android.app.Application;
import android.content.pm.PackageManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sino.model.db.User;
import com.example.sino.viewmodel.MainViewModel;

import java.util.List;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SinoApplication extends Application {

    private static SinoApplication mInstance;
    private User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized SinoApplication getInstance() {
        return mInstance;
    }

    public String getVersionName() {

        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0.0";
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
