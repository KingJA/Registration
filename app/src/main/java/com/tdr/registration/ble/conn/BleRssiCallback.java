package com.tdr.registration.ble.conn;


public abstract class BleRssiCallback extends BleCallback {
    public abstract void onSuccess(int rssi);
}