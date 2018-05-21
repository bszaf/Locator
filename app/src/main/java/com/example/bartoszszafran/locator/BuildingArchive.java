package com.example.bartoszszafran.locator;

import android.util.Log;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by bartoszszafran on 15/05/2018.
 */

public class BuildingArchive {

    HashMap<String, BuildingTopology> ledger = new HashMap<>();


    public void addBuilding(String name, BuildingTopology buildingTopology) {
        Log.d("building archive", "added " + name);
        ledger.put(name, buildingTopology);
    }

    public void removeBuilding(String name) {
        ledger.remove(name);
    }

    public BuildingTopology getBuidling(String name) { return ledger.get(name); }

    public Set<String> listBuildings() {
        return ledger.keySet();
    }
}
