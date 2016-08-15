package com.bytesnmaterials.zro.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by mahya on 3/30/2016.
 */
public class ZeroNetUtil {
    public static String getIP(Context context){
        // TODO: 3/30/2016 To try this with 3G or 4G connections

        WifiManager wifiMan = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();

        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
