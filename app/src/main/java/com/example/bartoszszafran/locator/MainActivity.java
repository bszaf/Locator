package com.example.bartoszszafran.locator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Locator locator;
    private Scanner scanner;

    final private String[] permissions = {
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, 1);
        }

        locator = new Locator();
        scanner = App.getScanner();

        try {
            FileInputStream fis = openFileInput("state");
            ObjectInputStream is = new ObjectInputStream(fis);
            HashMap restored = (HashMap<String, BuildingTopology>) is.readObject();
            App.getBuildingArchive().ledger = restored;
            Log.d("debug", "restored " + restored);
            is.close();
            fis.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        File file = new File(App.getAppContext().getFilesDir(), "state");
        file.delete();
        String filename = "state";

        HashMap<String, BuildingTopology> ledger = App.getBuildingArchive().ledger;

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(ledger);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        return true;
    }

    public void refreshNetowrk(View v) {
        scanner.refreshNetworks();
    }

    public void goToPlaces(View view) {
        Log.d("debug", " " + App.getBuildingArchive().listBuildings());
    }

    public void showBuildings(View view) {
        Intent intent = new Intent(this, BuildingsActivity.class);
        startActivity(intent);
    }

    public void deleteOldState(View view) {
        File file = new File(App.getAppContext().getFilesDir(), "state");
        file.delete();
        for(String s : App.getBuildingArchive().listBuildings()) {
            App.getBuildingArchive().removeBuilding(s);
        }
    }
}
