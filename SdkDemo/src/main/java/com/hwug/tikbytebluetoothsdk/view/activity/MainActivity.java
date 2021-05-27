package com.hwug.tikbytebluetoothsdk.view.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hwug.hwugbluetoothsdk.UgBleFactory;
import com.hwug.tikbytebluetoothsdk.R;
import com.hwug.tikbytebluetoothsdk.cms.components.DaggerMainComponent;
import com.hwug.tikbytebluetoothsdk.cms.modules.MainModule;
import com.hwug.tikbytebluetoothsdk.databinding.ActivityMainBinding;
import com.hwug.tikbytebluetoothsdk.presenter.MainPresenter;
import com.hwug.tikbytebluetoothsdk.view.MainView;
import com.hwug.tikbytebluetoothsdk.view.adapter.main.DebugDataAdapterCallback;
import com.lcq.control.LogConMgrUtil;
import com.ugee.pentabletinterfacelibrary.IInitCustomViewCallBack;
import com.ugee.ugcustomsketchpadlib.component.enumeration.TypesOfPensEnum;
import com.ugee.ugcustomsketchpadlib.component.repository.IBookPagerCallback;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends BaseActivity implements MainView,
        IInitCustomViewCallBack, IBookPagerCallback,View.OnClickListener {

    @Named("release")
    @Inject
    MainPresenter mainPresenter;

    @Named("release")
    @Inject
    DebugDataAdapterCallback debugDataAdapter;

    private ProgressDialog progressDialog;
    private int sendOrderCount = 0;
    private BluetoothDevice bleDevice;
    private String bleType = "not connected";

    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        initializeInjector();
        initCustomSketch();
        initView();
        initData();
        initListener();
        mainPresenter.setScreenType(this);
        connectLogic();
        activityMainBinding.customView.onPointAndLine();
        Log.e("data","MainActivity onCreate");
    }

    private void connectLogic(){
        connectShow();
    }

    private void initWindow(){
        getWindow().setBackgroundDrawable(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initCustomSketch(){
        activityMainBinding.customView.onInit(this);
        activityMainBinding.customView.setIInitCustomViewCallBack(this);
        activityMainBinding.customView.setBookPagerCallback(null);
        activityMainBinding.customView.setIsOpenUndoAddStep(true);
        activityMainBinding.customView.setPaintWidth(15);
        activityMainBinding.customView.setOpenOrCloseFlipPages(true);
    }

    @NotNull
    public static Intent getCallingIntent(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context,MainActivity.class);
        return intent;
    }

    private void initListener(){
        activityMainBinding.btnConnectDevice.setOnClickListener(this);
        activityMainBinding.clearCanvas.setOnClickListener(this);
        activityMainBinding.clearDeviceData.setOnClickListener(this);
        activityMainBinding.pointCanvas.setOnClickListener(this);
        activityMainBinding.btnSendOrder.setOnClickListener(this);

        activityMainBinding.btnPenAdd.setOnClickListener(this);
        activityMainBinding.btnPenDelete.setOnClickListener(this);

        activityMainBinding.tvSelectBattery.setOnClickListener(this);
    }

    protected void initializeInjector() {
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build()
                .inject(MainActivity.this);
        mainPresenter.setView(this);
    }

    private void initView(){
        initRecycleView();
        activityMainBinding.lvLvBleData.setVisibility(View.GONE);
        activityMainBinding.relativeHide.setVisibility(View.VISIBLE);
    }
    
    private void initData(){
        Intent intent = getIntent();
        if (intent!=null){
            bleDevice = intent.getParcelableExtra("deviceName");
            if (bleDevice!=null){
                bleStrName = bleDevice.getName();
            }else{
                Toast.makeText(this, "bleDevice : null", Toast.LENGTH_SHORT).show();
            }
            String str = getString(R.string.deviceName)+bleStrName;
            activityMainBinding.tvShowDeviceName.setText(str);
        }else{
            Toast.makeText(this, "intent : null", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecycleView(){
        mainPresenter.itemListAdd("end");
        debugDataAdapter.setStringList(mainPresenter.getItemList());
        activityMainBinding.lvLvBleData.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        activityMainBinding.lvLvBleData.setLayoutManager(new LinearLayoutManager(context()));
        activityMainBinding.lvLvBleData.setAdapter(debugDataAdapter);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainPresenter.setScreenType(context());
    }

    private float paintWidth = 15;
    public void onClick(@NotNull View view) {
        int id = view.getId();
        if (mainPresenter==null){
            throw new NullPointerException("error 155");
        }
        if (id == R.id.btn_pen_add) {
            paintWidth+=5;
            activityMainBinding.customView.setPaintWidth(paintWidth);
        }else if (id == R.id.btn_pen_delete) {
            if (paintWidth>15){
                paintWidth-=5;
                activityMainBinding.customView.setPaintWidth(paintWidth);
            }
        }else
        if (id == R.id.btn_connectDevice) {
            connectShow();
        } else if (id == R.id.clearDeviceData) {
            mainPresenter.clearBleData();
        } else if (id == R.id.clearCanvas) {
            activityMainBinding.customView.onCleanCanvas();
        } else if (id == R.id.pointCanvas) {
            activityMainBinding.customView.onPointAndLine();
        } else if (id == R.id.tvSelectBattery) {
            getBattery();
        } else if (id == R.id.btn_sendOrder) {
            showListOrCanvas();
        }
    }

    private void showListOrCanvas(){
        sendOrderCount++;
        if (sendOrderCount % 2 == 0) {
            activityMainBinding.lvLvBleData.setVisibility(View.GONE);
            activityMainBinding.relativeHide.setVisibility(View.VISIBLE);
        } else {
            activityMainBinding.lvLvBleData.setVisibility(View.VISIBLE);
            activityMainBinding.relativeHide.setVisibility(View.GONE);
        }
    }

    private void getBattery(){
        boolean flag = mainPresenter.getBatteryCallback();
        LogConMgrUtil.getInstance().e("getBatteryCallback flag : "+flag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityMainBinding.customView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityMainBinding.customView.onResume();
        activityMainBinding.customView.setPaintType(TypesOfPensEnum.SteelPen);
        activityMainBinding.customView.setPaintWidth(10);
    }

    private void connectShow(){
        if (bleDevice!=null){
            showLoading();
            bleStrName = bleDevice.getName();
            mainPresenter.connectBle(context(),bleDevice,mainPresenter);
            runOnUiThread(()-> mainPresenter.itemListAdd(bleStrName));
        }else{
            hideLoading();
            runOnUiThread(()->Toast.makeText(this,
                    "bleDevice : null --- 【"+getString(R.string.ConnectionFailed)+"】",
                    Toast.LENGTH_SHORT).show());
        }
    }

    private String bleStrName = "";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bleStrName = "";
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            UgBleFactory.getInstance().disConnect();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityMainBinding.customView.onDestroy();
    }

    @Override
    public void onDebugNotifyDataSetChanged(){
        runOnUiThread(()->{
            if (debugDataAdapter!=null){
                debugDataAdapter.notifyDataSetChanged();
                activityMainBinding.lvLvBleData.scrollToPosition(debugDataAdapter.getItemCount()-1);
            }
        });
    }

    @Override
    public void onGetXYDataCallback(byte bleButton, int bleX, int bleY, short blePressure) {
        setShowPressure(String.valueOf(blePressure));
        activityMainBinding.customView.onXYEvent(bleButton,bleX,bleY,blePressure);
    }

    @Override
    public void onGetXYMax(int maxX, int maxY, int maxPressure) {
        activityMainBinding.customView.setMaxXy(maxX,maxY,maxPressure);
    }

    private void setShowPressure(String pressure){
        runOnUiThread(()->{
            Log.i("pressure","pressure : "+pressure);
            // The text can be refreshed by using a custom view, please note here first
//            if (tv_show_pressure!=null){
//                String str = getString(R.string.pressureValue)+pressure;
//                tv_show_pressure.setText(str);
//            }
        });
    }

    @Override
    public void onConnectType(int type){
        runOnUiThread(()->{
            hideLoading();
            switch (type){
                case 0:
                    bleType = getString(R.string.connectSuccessBle);
                    break;
                case 3:
                case -3:
                case -4:
                    bleType = getString(R.string.connectFailBle);
                    break;
                case 4:
                case 5:
                case 6:
                    bleType = "Parameter exception or error.";
                    break;
                case -1:
                    bleType = getString(R.string.bleInit);
                    break;
            }
            String strType = getString(R.string.connectType)+bleType;
            activityMainBinding.tvShowConnectType.setText(strType);
        });
    }

    @Override
    public void showError(String message) {
        Log.e("AndroidRuntime",message);
    }

    @Override
    public Context context(){
        return MainActivity.this;
    }

    @Override
    public void showLoading(){
        runOnUiThread(()->{
            hideLoading();
            progressDialog = new ProgressDialog(context());
            progressDialog.setCancelable(true);
            progressDialog.setMessage(getString(R.string.onStartConnectDevice));
            progressDialog.show();
        });
    }

    @Override
    public void hideLoading(){
        runOnUiThread(()->{
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onInitViewStart() {
        showViewHint("init start",Color.BLUE);
        Log.i("data","init start");
    }

    @Override
    public void onInitViewSuccess() {
        showViewHint("init success",Color.BLACK);
//        LogConMgrUtil.getInstance().e("init success");
    }

    @Override
    public void onInitViewFail(String failMessage) {
//        LogConMgrUtil.getInstance().e("failMessage : "+failMessage);
        showViewHint(failMessage,Color.RED);
    }

    private void showViewHint(String msg,int color){
        runOnUiThread(()->{
            activityMainBinding.tvShowInitView.setText(msg);
            activityMainBinding.tvShowInitView.setTextColor(color);
        });
    }


    @Override
    public void onPagerCallback(int nowPage, int pageCount, String showPagerMsg) {

    }
}
