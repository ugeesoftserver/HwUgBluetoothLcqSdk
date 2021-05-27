package com.hwug.tikbytebluetoothsdk.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hwug.tikbytebluetoothsdk.UgBleApplication;
import com.hwug.tikbytebluetoothsdk.cms.components.ApplicationComponent;
import com.hwug.tikbytebluetoothsdk.navigation.Navigator;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

  @Inject
  Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getApplicationComponent().inject(this);
  }

  protected ApplicationComponent getApplicationComponent() {
    return ((UgBleApplication) getApplication()).getApplicationComponent();
  }

  protected abstract void initializeInjector();

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

}
