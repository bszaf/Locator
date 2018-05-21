package com.example.bartoszszafran.locator;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bartoszszafran on 09/05/2018.
 */

public final class Locator  {

    public static Position locate(HashMap<Position, List<ShortScanResult>> positions, List<ShortScanResult> current) {
        Log.d("Locator", "Locator started");
        Position result = null;
        float min_distance = -1;
        for(Position p : positions.keySet()) {
            List<ShortScanResult> saved = positions.get(p);
            float d = calculateEuclideanDistance(saved, current);
            if (d > min_distance) {
                min_distance = d;
                result = p;
            }
            Log.d("Locator", "Distance to point " + p + " is " + d);
        }
        return result;
    }

    private static float calculateEuclideanDistance(List<ShortScanResult> saved, List<ShortScanResult> observed) {
        float result = 0;
        for (ShortScanResult s1 : observed) {
            for ( ShortScanResult s2: saved) {
                if (s1.BSSID.equals(s2.BSSID)) {
                    float temp = s1.level - s2.level;
                    temp *= temp;
                    result += temp;
                }
            }
        }
        return (float) Math.sqrt(result);


    }

}
