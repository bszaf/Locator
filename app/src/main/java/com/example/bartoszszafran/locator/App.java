package com.example.bartoszszafran.locator;

import android.app.Application;
import android.content.Context;

/**
 * Created by bartoszszafran on 15/05/2018.
 */

public class App extends Application {

    private static Context context;
    private static Scanner scanner;
    private static final BuildingArchive buildingArchive = new BuildingArchive();

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        App.scanner = new Scanner(context);
    }

    public static Context getAppContext() {
        return App.context;
    }

    public static Scanner getScanner() {
       return scanner;
    }

    public static BuildingArchive getBuildingArchive() { return buildingArchive; }
}
