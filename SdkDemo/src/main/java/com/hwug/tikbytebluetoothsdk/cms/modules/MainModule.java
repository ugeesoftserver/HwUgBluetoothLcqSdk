package com.hwug.tikbytebluetoothsdk.cms.modules;

import android.content.Context;

import com.hwug.tikbytebluetoothsdk.presenter.MainPresenter;
import com.hwug.tikbytebluetoothsdk.view.adapter.main.DebugDataAdapterCallback;

import org.jetbrains.annotations.NotNull;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final Context context;
    public MainModule(@NotNull Context context){
        this.context = context.getApplicationContext();
    }
    @Named("release")
    @Provides
    MainPresenter mainPresenter(){
        return new MainPresenter();
    }

    @Named("release")
    @Provides
    DebugDataAdapterCallback debugDataAdapter(){
        return new DebugDataAdapterCallback(context);
    }
}
