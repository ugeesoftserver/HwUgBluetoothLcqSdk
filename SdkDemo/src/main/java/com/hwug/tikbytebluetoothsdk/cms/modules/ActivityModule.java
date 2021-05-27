package com.hwug.tikbytebluetoothsdk.cms.modules;

import android.app.Activity;

import com.hwug.tikbytebluetoothsdk.cms.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  @PerActivity
  Activity activity() {
    return this.activity;
  }
}
