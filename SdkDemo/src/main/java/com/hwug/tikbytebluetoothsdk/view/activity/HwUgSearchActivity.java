package com.hwug.tikbytebluetoothsdk.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hwug.hwugbluetoothsdk.UgBleFactory;
import com.hwug.tikbytebluetoothsdk.R;
import com.hwug.tikbytebluetoothsdk.databinding.ActivitySearchBinding;
import com.lcq.control.LogConMgrUtil;
import com.ugee.pentabletinterfacelibrary.IHwUgBluetoothSearchCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Liu.Mr
 */
public class HwUgSearchActivity extends BaseActivity implements IHwUgBluetoothSearchCallback {

    private static final int SEARCH_DEVICE_TIME = 5000;//5S scan time

    //true automatically connect to the specified device false otherwise
    public static final boolean isAutoConnectionFlag = true;

    List<BluetoothDevice> deviceList;
    List<String> itemList;
    ArrayAdapter<String> myAdapter;
    BluetoothDevice lastDevice = null;

    ActivitySearchBinding activitySearchBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(activitySearchBinding.getRoot());
        initializeInjector();
        activitySearchBinding.btnSearch.setOnClickListener(v -> onClickBtnSearch());
        LogConMgrUtil.getInstance().i("isAutoConnectionFlag : "+isAutoConnectionFlag);
    }

    @Override
    protected void initializeInjector() {
        initData();
        initListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        if (isAutoConnectionFlag){
//            //AutoConnect
//            startNewThread();
//        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void initData(){
        itemList = new ArrayList<>();
        deviceList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(HwUgSearchActivity.this,
                android.R.layout.simple_list_item_1, itemList);
        activitySearchBinding.lvDevice.setAdapter(myAdapter);
    }

    private final boolean flag = false;
    private void initListener(){
        activitySearchBinding.lvDevice.setOnItemClickListener((adapterView,view,i,l)->
            lvOnClick(i)
        );
    }

    // Custom automatic connection related, temporarily abandoned
    @Deprecated
    public void startNewThread(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(800);
                    runOnUiThread(() -> btnOnClick());
                    sleep(5000);
                    if (!flag){
                        runOnUiThread(() -> btnOnClick());
                        // Add at startup
//                        startNewThread();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void onClickBtnSearch(){
        btnOnClicks();
    }

    private void btnOnClicks(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }
        if (!isLocationEnable(this)){
            setLocationService();
        }else{
            btnOnClick();
            searchLocal();
        }
    }


    private void searchLocal(){
        LogConMgrUtil.getInstance().e("---------searchLocal----------");
        //BluetoothAdapter，android 2.0
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        LogConMgrUtil.getInstance().e("searchLocal : There is no Bluetooth device in this machine!");
        if(adapter != null){
            if(!adapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            if(devices.size()>0){
                for (BluetoothDevice device : devices) {
                    LogConMgrUtil.getInstance().e("searchLocal local address : " +
                            device.getAddress() + ",device name : " + device.getName());
                    deviceList.add(device);
                    itemList.add(device.getName()+" ,address : "+device.getAddress()+"---->Locally paired");
                }
            }else{
                LogConMgrUtil.getInstance().e("searchLocal : There is no paired remote Bluetooth device yet!");
            }
        }
    }

    /**
     * Location service if enable
     *
     * @param context c
     * @return location is enable if return true, otherwise disable.
     */
    public static boolean isLocationEnable(@NotNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return networkProvider || gpsProvider;
    }

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                btnOnClick();
            }else{
                Toast.makeText(this,"Need location permission to scan and use " +
                        "Bluetooth！",Toast.LENGTH_LONG).show();
            }

        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void lvOnClick(int position){
        navigator.navigateToMainCanvas(this,deviceList.get(position));
    }

    private ProgressDialog mProgressDialog;
    private void showDialog(Context context){
        disMis();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.searchBleDevice));
        mProgressDialog.show();
    }

    private void disMis(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    private void btnOnClick(){
        dataClear();
        UgBleFactory.getInstance().closeBleDevice();
        UgBleFactory.getInstance().startScanAndTime(this,this,SEARCH_DEVICE_TIME);
    }

    private void dataClear(){
        if (deviceList!=null){
            deviceList.clear();
        }
        if (itemList!=null){
            itemList.clear();
        }
        if (myAdapter!=null){
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetBleDevice(final BluetoothDevice devices, int rssi, byte [] scanRecord) {
        runOnUiThread(()-> {
            deviceList.add(devices);
            String name = devices.getName();
            itemList.add(name+" ,address : "+devices.getAddress());
            lastDevice = devices;
            myAdapter.notifyDataSetChanged();
//            if (isAutoConnectionFlag){
//                //Automatically detect connection related content
//                if (name.startsWith(deviceName)){
//                    flag = true;
//                    navigator.navigateToMainCanvas(this,devices);
//                }
//            }
        });
    }

    @Override
    public void onGetBleStorage(String bleFlashData) {
        final String data = bleFlashData;
        runOnUiThread(()->
            Toast.makeText(HwUgSearchActivity.this, "bleFlashData : "+data, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onGetBleFlashFlag(boolean bleFlashFlag) {
        // True indicates successful unlocking , False indicates that unlocking failed
        final boolean flag = bleFlashFlag;
        runOnUiThread(()->
            Toast.makeText(HwUgSearchActivity.this, "bleFlashFlag : "+flag, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void scanEnd() {
        disMis();
    }

    @Override
    public void scanStart() {
        showDialog(this);
    }
}
