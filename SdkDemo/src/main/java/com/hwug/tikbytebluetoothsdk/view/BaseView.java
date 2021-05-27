package com.hwug.tikbytebluetoothsdk.view;

import android.content.Context;

public interface BaseView {
    void showError(String message);
    /**
     * Get a {@link Context}.
     */
    Context context();
    void showLoading();
    void hideLoading();
}
