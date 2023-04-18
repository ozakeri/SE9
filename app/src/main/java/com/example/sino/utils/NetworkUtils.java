package com.example.sino.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtils {

    public static boolean isNetworkAvailable(@Nullable ConnectivityManager connectivityManager) {
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://www.google.com/").openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            }
        } catch (IOException e) {
            // Does nothing.
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
    public static boolean VpnConnectionCheck2()
    {

        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex)
        {
            Log.e("MAIN","isVpnUsing Network List didn't received");
        }

        return networkList.contains("tun0");

    }
}
