package com.tdr.registration.ble.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tdr.registration.ble.exception.BleException;

/**
 * Created by Linus_Xie on 2016/10/24.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BleGattCallback extends BluetoothGattCallback {

    public abstract void onConnectSuccess(BluetoothGatt gatt, int status);

    @Override
    public abstract void onServicesDiscovered(BluetoothGatt gatt, int status);

    public abstract void onConnectFailure(BleException exception);

}