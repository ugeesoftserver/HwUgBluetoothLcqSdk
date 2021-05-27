package com.hwug.tikbytebluetoothsdk.navigation;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.hwug.tikbytebluetoothsdk.view.activity.MainActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

  @Inject
  Navigator() {
    //empty
  }
  /**
   * main canvas
   * @param context A Context needed to open the destiny activity.
   */
  public void navigateToMainCanvas (Context context,BluetoothDevice device) {
    if (context != null) {
      Intent intentToLaunch = MainActivity.getCallingIntent(context);
      intentToLaunch.putExtra("deviceName",device);
      context.startActivity(intentToLaunch);
    }
  }
}
