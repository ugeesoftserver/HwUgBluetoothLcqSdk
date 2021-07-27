package com.hwug.sdk;

import android.content.Context;

import androidx.annotation.Keep;

import com.hwug.hwugbluetoothsdk.UgBleFactory;
import com.hwug.hwugbluetoothsdk.entity.BleDevice;
import com.ugee.pentabletinterfacelibrary.ITabletDataCallback;

@Keep
public final class HwUgBleSdkEntrance {
    // ℃ 名称改变 --
    public void connect(Context context, BleDevice bleDevice, ITabletDataCallback iTabletDataCallback){
        UgBleFactory.getInstance().connect(context,bleDevice,iTabletDataCallback);
    }

    //
    public void disconnect(){
        UgBleFactory.getInstance().disConnect();
    }

}
