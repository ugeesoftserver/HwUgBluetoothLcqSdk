package com.hwug.tikbytebluetoothsdk;

import android.app.Application;

import com.hwug.hwugbluetoothsdk.BleApplication;
import com.hwug.tikbytebluetoothsdk.cms.components.ApplicationComponent;
import com.hwug.tikbytebluetoothsdk.cms.components.DaggerApplicationComponent;
import com.hwug.tikbytebluetoothsdk.cms.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;
import com.ugee.pentabletinterfacelibrary.anr.ANRWatchDog;

public class UgBleApplication extends Application {

    private ApplicationComponent applicationComponent;
    @Override
    public void onCreate(){
        //Position ANR
        new ANRWatchDog().start();
        super.onCreate();
        // To initialize Bluetooth, you must
        BleApplication.init(this);
        initializeInjector();
        initializeLeakDetection();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
//        if (false) {
//            初始化LeakCanary 是
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }
    }
}
