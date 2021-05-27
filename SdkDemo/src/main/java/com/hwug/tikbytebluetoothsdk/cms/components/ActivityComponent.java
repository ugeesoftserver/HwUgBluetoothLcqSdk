package com.hwug.tikbytebluetoothsdk.cms.components;

import android.app.Activity;

import com.hwug.tikbytebluetoothsdk.cms.PerActivity;
import com.hwug.tikbytebluetoothsdk.cms.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
interface ActivityComponent {
  //Exposed to sub-graphs.
  Activity activity();
}
