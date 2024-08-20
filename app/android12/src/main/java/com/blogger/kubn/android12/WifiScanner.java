package com.blogger.kubn.android12;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiScanner {

    private Context context;

    public WifiScanner(Context context) {
        this.context = context;
    }

    public List<ScanResult> scanWifiNetworks() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean scanStarted = wifiManager.startScan();

        if (scanStarted) {
            return wifiManager.getScanResults();
        } else {
            return null;
        }
    }

    public String getSSID(ScanResult scanResult) {
        if (scanResult != null) {
            return scanResult.SSID;
        } else {
            return null;
        }
    }

    public String getBSSID(ScanResult scanResult) {
        if (scanResult != null) {
            return scanResult.BSSID;
        } else {
            return null;
        }
    }

    public int getLevel(ScanResult scanResult) {
        if (scanResult != null) {
            return scanResult.level;
        } else {
            return 0;
        }
    }
}