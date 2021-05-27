package com.hwug.tikbytebluetoothsdk.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.hwug.hwugbluetoothsdk.UgBleFactory;
import com.ugee.pentabletinterfacelibrary.ITabletDataCallback;

public abstract class BasePresenter implements IBasePresenter , ITabletDataCallback {

    /**
     * {@link BasePresenter#tabletMaxAttrCallback}
     * {@link BasePresenter#pointDataCallback}
     */
    private boolean configFlag = false;
    int dMaxX = 0,dMaxY = 0,dMaxPressure = 0;
    @Override
    public void resume() {

    }

    @Override
    public void pause() {
    }

    //Subsequent revisions may be inaccurate
    public void setScreenType(@NonNull Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //Get the configuration information of the setting
        int ori = mConfiguration.orientation; //Get screen orientation
        if (ori == Configuration.ORIENTATION_LANDSCAPE){
            configFlag = true;
        }else if (ori == Configuration.ORIENTATION_PORTRAIT){
            //Portrait
            configFlag = true;
        }
    }

    @Override
    public void destroy() {
        UgBleFactory.getInstance().closeBleDevice();
    }

    public void connectBle(@NonNull Context context, @NonNull BluetoothDevice bluetoothDevice, @NonNull ITabletDataCallback iTabletDataCallback){
        UgBleFactory.getInstance().disConnect();
        //Errors can be ignored during unit testing
        UgBleFactory.getInstance().connect(context,bluetoothDevice, iTabletDataCallback);
    }

    /**
     * The mapping logic can be modified by yourself,
     * corresponding to the {@link BasePresenter#tabletMaxAttrCallback} callback
     */
    @Override
    public void pointDataCallback(byte bleButton, int bleX, int bleY, short blePressure) {
        //original
//        onGetData(bleButton,dMaxX-bleY,bleX,blePressure);

        //The mapping logic can be modified by yourself,
        //corresponding to the {@link BasePresenter#onGetBleUsbScreenMax} callback
        onGetData(bleButton,bleX,bleY,blePressure);

//        if (!configFlag){ //Horizontal screen
//            onGetData(bleButton,bleX,bleY,blePressure);
//        }else{
////            onGetData(bleButton,bleY,dMaxY-bleX,blePressure);
//            //
//            onGetData(bleButton,dMaxX-bleY,bleX,blePressure);
//        }
    }

    @Override
    public void softKeyDataCallback(byte bleSoftButton, int bleSoftX, int bleSoftY) {
    }

    @Override
    public void hardKeyDataCallback(byte bleHardButton, int bleHardX, int bleHardY) {
    }

    @Override
    public void tabletMaxAttrCallback(int rc, int maxX, int maxY, int maxButton, int maxPressure) {
        onGetMaxXy(maxX,maxY,maxPressure);
        dMaxX = maxX;
        dMaxY = maxY;

        //The screen can be mapped according to the coordinate value
//        if (!configFlag){
//            onGetMaxXy(maxX,maxY,maxPressure);
//            dMaxX = maxX;
//            dMaxY = maxY;
//        } else {
//            onGetMaxXy(maxY,maxX,maxPressure);
//            dMaxX = maxY;
//            dMaxY = maxX;
//        }
    }

    @Override
    public void pointDataCallback(byte pointType, int pointX, int pointY, int pointZ, short pointP) {
        pointDataCallback(pointType,pointX,pointY,pointP);
    }
    abstract void onGetData(byte bleButton, int bleX, int bleY, short blePressure);

    abstract void onGetMaxXy(int maxX, int maxY , int maxPressure);
    
    @Override
    public void tabletConnectTypeCallback(int type) {
    }

    @Override
    public void tabletBatteryLevelCallback(String battery) {
    }

    public boolean getBatteryCallback() {
        return UgBleFactory.getInstance().getBatteryCallback();
    }
}
