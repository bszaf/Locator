package com.example.bartoszszafran.locator;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by bartoszszafran on 09/05/2018.
 */

public class Scanner {
    private Context cntx;
    private boolean awaiting;
    private Scanner me;
    final WifiManager mWifiManager;
    List<ScanResult> results;

    Scanner(Context cntx) {
        this.cntx = cntx;
        this.me = this;
        this.mWifiManager = (WifiManager) cntx.getSystemService(WIFI_SERVICE);
    }

    void refreshNetworks() {
        if ( ! mWifiManager.isWifiEnabled() ) {
            handleWifiDisabled();
            return;
        }
        cntx.registerReceiver(br, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (mWifiManager.startScan())
            Log.d("wifi", "started scan");
        else
            Log.w("wifi", "scan failed");
    }

    synchronized List<ScanResult> getResults() {
        refreshNetworks();
        try {
            this.awaiting = true;
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.results;
    };

    private final BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                results = mWifiManager.getScanResults();
                if (awaiting)
                    me.notifyAll();
                Log.d("wifi", "Found " + results.size() + " networks");
                for (ScanResult s : results) {
                    String ssid = s.SSID;
                    int level = s.level;
                    Log.d("wifi", "Found " + ssid + " with strenght " + level);
                }
            }
        };

    private void handleWifiDisabled() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cntx);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Scanning requires WiFi.");
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}
