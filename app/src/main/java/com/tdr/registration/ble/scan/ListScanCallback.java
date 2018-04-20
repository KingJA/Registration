package com.tdr.registration.ble.scan;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linus_Xie on 2016/9/2.
 * 一段限制时间内搜索所有设备
 */
public abstract class ListScanCallback extends PeriodScanCallback {

    /**
     * 所有被发现的设备集合
     */
    private List<BluetoothDevice> deviceList = new ArrayList<>();

    public ListScanCallback(long timeoutMillis) {
        super(timeoutMillis);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device == null)
            return;

        if (!deviceList.contains(device)) {
            deviceList.add(device);
        }
    }

    @Override
    public void onScanTimeout() {
        BluetoothDevice[] devices = new BluetoothDevice[deviceList.size()];
        for (int i = 0; i < devices.length; i++) {
            devices[i] = deviceList.get(i);
        }
        onDeviceFound(devices);
    }

    public abstract void onDeviceFound(BluetoothDevice[] devices);
}
