package com.example.bartoszszafran.locator;

import java.io.Serializable;

/**
 * Created by bartoszszafran on 10/05/2018.
 */

public class Position implements Serializable {

    float x;
    float y;

    Position(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
