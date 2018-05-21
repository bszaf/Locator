package com.example.bartoszszafran.locator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bartoszszafran on 10/05/2018.
 */

public class BuildingTopology implements Serializable {

    private static final long serialVersionUID = -6298516694275121291L;

    public HashMap<Position, List<ShortScanResult>> positionToRouterSignalStrength;
    transient public Bitmap buildingScheme;

    Position findNearest() {
        return null;
    };


    private void writeObject(ObjectOutputStream oos) throws IOException {
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if(buildingScheme!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = buildingScheme.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] image = (byte[]) ois.readObject();
        if(image != null && image.length > 0){
            buildingScheme = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }



}
