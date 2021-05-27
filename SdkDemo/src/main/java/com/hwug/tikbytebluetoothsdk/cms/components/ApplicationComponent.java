package com.hwug.tikbytebluetoothsdk.cms.components;

import com.hwug.tikbytebluetoothsdk.cms.modules.ApplicationModule;
import com.hwug.tikbytebluetoothsdk.view.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);
}
