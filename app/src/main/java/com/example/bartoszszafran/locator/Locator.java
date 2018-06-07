package com.example.bartoszszafran.locator;

import android.util.Log;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bartoszszafran on 09/05/2018.
 */

public final class Locator {


    public static Position locate(HashMap<Position, List<ShortScanResult>> positions, List<ShortScanResult> current, int K) {

        class Pair {
            float dist;
            Position pos;

            public Pair(float dist, Position pos) {
                this.dist = dist;
                this.pos = pos;
            }
        }

        //K is too big
        K = K > positions.size() ? positions.size() : K;

        Pair[] closest = new Pair[K];
        boolean isClosestFull = false;

        //fill closest with closest neighbours
        for (Position p : positions.keySet()) {
            List<ShortScanResult> saved = positions.get(p);
            float d = calculateEuclideanDistance(saved, current);
//            Log.d("Locator", "Distance to point " + p + " is " + d);
            if (d == 0) return p;

            if (!isClosestFull){
                for (int i = 0; i < K; i++) {
                    if (closest[i] == null) {
                        closest[i] = new Pair(d, p);
                        isClosestFull = i == K - 1;
                        break;
                    }
                }
            } else {
                Pair furthestPair = closest[0];
                for (Pair pair : closest){
                    if (pair.dist > furthestPair.dist) {
                        furthestPair = pair;
                    }
                }

                if (furthestPair.dist > d){
                    furthestPair.dist = d;
                    furthestPair.pos = p;
                }
            }

        }

        Position result = new Position(0, 0);
        float weightSum =0;
        for (Pair p : closest) {
            result.x += p.pos.x/p.dist;
            result.y += p.pos.y/p.dist;
            weightSum+= 1/p.dist;
        }

        result.x/=weightSum;
        result.y/=weightSum;

        return result;

    }

    public static Position locate(HashMap<Position, List<ShortScanResult>> positions, List<ShortScanResult> current) {
        Log.d("Locator", "Locator started");
        Position result = null;
        float min_distance = Float.MAX_VALUE;
        for (Position p : positions.keySet()) {
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
            for (ShortScanResult s2 : saved) {
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
