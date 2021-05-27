package com.hwug.tikbytebluetoothsdk.presenter;

import androidx.annotation.NonNull;

import com.hwug.tikbytebluetoothsdk.R;
import com.hwug.tikbytebluetoothsdk.cms.PerActivity;
import com.hwug.tikbytebluetoothsdk.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public final class MainPresenter extends BasePresenter{

    private MainView mainView;
    // {final} can be deleted
    private final List<String> itemList;
    @Inject
    public MainPresenter(){
        itemList = new ArrayList<>();
    }

    public void setView(@NonNull MainView mainView){
        this.mainView = mainView;
    }

    public List<String> getItemList(){
        return itemList;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void destroy() {
        super.destroy();
        mainView = null;
    }

    public void clearBleData(){
        if (itemList.size()>0) {
            itemList.clear();
        }
        notifyData();
    }

    @Override
    public void tabletConnectTypeCallback(int type) {
        switch(type){
            case 0:
                itemListAdd("type : "+type +"-->connect success");
                break;
            case 3:
            case 4:
                itemListAdd("type : "+type +"-->disconnect");
                break;
            case -3:
                itemListAdd("type : "+type +"-->close");
                break;
            case -4:
                itemListAdd("type : "+type +"-->close device");
                break;
            case 9:
                itemListAdd("type : "+type +"-->Adapted device");
                break;
        }
        mainView.onConnectType(type);
        notifyData();
    }

    @Override
    public void tabletBatteryLevelCallback(String battery) {
        itemListAdd("debug : "+battery);
    }

    @Override
    void onGetData(byte bleButton, int bleX, int bleY, short blePressure) {
        if (mainView!=null){
            mainView.onGetXYDataCallback(bleButton,bleX,bleY,blePressure);
        }
        itemListAdd(" bleButton : " + bleButton + ", bleX : " + bleX+ ", bleY : " + bleY + ", blePressure : " + blePressure );
    }

    @Override
    void onGetMaxXy(int maxX, int maxY, int maxPressure) {
        if (mainView!=null){
            mainView.onGetXYMax(maxX,maxY,maxPressure);
        }
        itemListAdd("maxX : " + maxX + ",dMaxY : " + maxY + ",dMaxPressure : " + maxPressure );
    }

    @Override
    public void hardKeyDataCallback(byte bleHardButton, int bleHardX, int bleHardY){
        itemListAdd("bleHardButton : " + bleHardButton + ",bleHardX : " + bleHardX + ",bleHardY : " + bleHardY );
        if (mainView == null
            ||
            mainView.context() == null){
            return;
        }
        if(bleHardX!=0){
            itemListAdd(mainView.context().getString(R.string.PressTheButton)+" bleHardX : " + bleHardX);
        }else{
            itemListAdd(mainView.context().getString(R.string.LiftButton) );
        }
    }

    @Override
    public void softKeyDataCallback(byte usbSoftButton, int usbSoftX, int usbSoftY) {
        itemListAdd("SoftButton : " + usbSoftButton + ",SoftX : " + usbSoftX + ",SoftY : " + usbSoftY );
    }

    public void itemListAdd(String msg){
        if(itemList!=null){
            itemList.add(msg);
        }
        notifyData();
    }

    private void notifyData(){
        if (mainView!=null){
            mainView.onDebugNotifyDataSetChanged();
        }
    }


    @Override
    public void pointDataCallback(byte pointType, int pointX, int pointY, int tiltX, int tiltY, short pointP) {

    }
}
