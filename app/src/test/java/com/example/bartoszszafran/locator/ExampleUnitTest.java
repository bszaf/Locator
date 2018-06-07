package com.example.bartoszszafran.locator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HashMap<Position, List<ShortScanResult>> h = new HashMap<>();

        List<ShortScanResult> l44 = new ArrayList<>();
        ShortScanResult res11 = new ShortScanResult();
        res11.BSSID = "11";
        res11.level = 4000;
        ShortScanResult res18 = new ShortScanResult();
        res18.BSSID = "18";
        res18.level = 4000;
        ShortScanResult res81 = new ShortScanResult();
        res81.BSSID = "81";
        res81.level = 8944;
        ShortScanResult res88 = new ShortScanResult();
        res88.BSSID = "88";
        res88.level = 8944;
        l44.add(res11);
        l44.add(res18);
        l44.add(res81);
        l44.add(res88);
//        h.put(new Position(5,5), l44);

        List<ShortScanResult> l14 = new ArrayList<>();
        ShortScanResult res112 = new ShortScanResult();
        res112.BSSID = "11";
        res112.level = 4000;
        ShortScanResult res182 = new ShortScanResult();
        res182.BSSID = "18";
        res182.level = 4000;
        ShortScanResult res882 = new ShortScanResult();
        res882.BSSID = "88";
        res882.level = 8944;
        ShortScanResult res812 = new ShortScanResult();
        res812.BSSID = "81";
        res812.level = 8944;
        l14.add(res112);
        l14.add(res182);
        l14.add(res882);
        l14.add(res812);
        h.put(new Position(1,5), l14);


        List<ShortScanResult> l41 = new ArrayList<>();
        ShortScanResult res113 = new ShortScanResult();
        res113.BSSID = "11";
        res113.level = 4000;
        ShortScanResult res183 = new ShortScanResult();
        res183.BSSID = "18";
        res183.level = 8944;
        ShortScanResult res883 = new ShortScanResult();
        res883.BSSID = "88";
        res883.level = 8944;
        ShortScanResult res813 = new ShortScanResult();
        res813.BSSID = "81";
        res813.level = 4000;

        l41.add(res113);
        l41.add(res183);
        l41.add(res883);
        l41.add(res813);
        h.put(new Position(5,1), l41);

        List<ShortScanResult> l84 = new ArrayList<>();
        ShortScanResult res114 = new ShortScanResult();
        res114.BSSID = "11";
        res114.level = 8944;
        ShortScanResult res184 = new ShortScanResult();
        res184.BSSID = "18";
        res184.level = 8944;
        ShortScanResult res814 = new ShortScanResult();
        res814.BSSID = "81";
        res814.level = 4000;
        ShortScanResult res884 = new ShortScanResult();
        res884.BSSID = "88";
        res884.level = 4000;
        l84.add(res184);
        l84.add(res114);
        l84.add(res814);
        l84.add(res884);
        h.put(new Position(9,5), l84);


        List<ShortScanResult> l48 = new ArrayList<>();
        ShortScanResult res115 = new ShortScanResult();
        res115.BSSID = "11";
        res115.level = 8944;
        ShortScanResult res185 = new ShortScanResult();
        res185.BSSID = "18";
        res185.level = 4000;
        ShortScanResult res815 = new ShortScanResult();
        res815.BSSID = "81";
        res815.level = 8944;
        ShortScanResult res885 = new ShortScanResult();
        res885.BSSID = "88";
        res885.level = 4000;
        l48.add(res184);
        l48.add(res114);
        l48.add(res814);
        l48.add(res884);
        h.put(new Position(5,9), l48);



//        List<ShortScanResult> l88 = new ArrayList<>();
//        ShortScanResult res114 = new ShortScanResult();
//        res114.BSSID = "11";
//        res114.level = 5656*2;
//        ShortScanResult res184 = new ShortScanResult();
//        res184.BSSID = "18";
//        res184.level = 8000;
//        ShortScanResult res814 = new ShortScanResult();
//        res814.BSSID = "81";
//        res814.level = 8000;
//        ShortScanResult res884 = new ShortScanResult();
//        res884.BSSID = "88";
//        res884.level = 1;
//        l88.add(res184);
//        l88.add(res114);
//        l88.add(res814);
//        l88.add(res884);
//        h.put(new Position(9,9), l88);


//        List<ShortScanResult> lr = new ArrayList<>();
//        ShortScanResult resr1 = new ShortScanResult();
//        resr1.BSSID = "1";
//        resr1.level = 22;
//        ShortScanResult resr2 = new ShortScanResult();
//        resr2.BSSID = "2";
//        resr2.level = 14;
//        lr.add(resr1);
//        lr.add(resr2);

        Position p = Locator.locate(h, l44, 20 );


        assertEquals(4, 2 + 2);
    }
}