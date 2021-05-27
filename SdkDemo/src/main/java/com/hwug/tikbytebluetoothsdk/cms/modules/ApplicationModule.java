package com.hwug.tikbytebluetoothsdk.cms.modules;

import android.content.Context;

import com.hwug.tikbytebluetoothsdk.UgBleApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
  private final UgBleApplication application;

  public ApplicationModule(UgBleApplication application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return this.application;
  }

}
