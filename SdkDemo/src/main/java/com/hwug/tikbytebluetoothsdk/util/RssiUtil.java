package com.hwug.tikbytebluetoothsdk.util;

/**
 * Created by LCQ
 * on 2018/4/14.
 * Notes may be inaccurate
 */
public class RssiUtil {
    // The values of A and n need to be tested according to the actual environment
    private static final double A_Value = 62;
    /**
     * A - Signal strength when the transmitter and receiver are separated by 1 meter
     */
    private static final double n_Value = 2.5;/* n - Environmental attenuation factor*/

    /**
     * Get the returned distance according to Rssi, and the returned data unit is m
     */
    public static double getDistance(int rssi) {
        int iRssi = Math.abs(rssi);
        double power = (iRssi - A_Value) / (10 * n_Value);
        return Math.pow(10, power);
    }
}