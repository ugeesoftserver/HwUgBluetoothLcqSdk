package com.hwug.tikbytebluetoothsdk.cms.components;

import com.hwug.tikbytebluetoothsdk.cms.modules.MainModule;
import com.hwug.tikbytebluetoothsdk.view.activity.MainActivity;
import com.hwug.tikbytebluetoothsdk.view.activity.ScreenMainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Liu.Mr
 * on 2019/1/14.
 */
@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
    void inject(ScreenMainActivity screenMainActivity);

}
