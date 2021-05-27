package com.hwug.tikbytebluetoothsdk.view;

public interface MainView extends BaseView{
    void onDebugNotifyDataSetChanged();
    void onGetXYDataCallback(byte bleButton, int bleX, int bleY, short blePressure);
    void onGetXYMax(int maxX, int maxY, int maxPressure);

    void onConnectType(int type);
}
