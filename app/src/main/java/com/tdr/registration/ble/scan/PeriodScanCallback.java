package com.tdr.registration.ble.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

import com.tdr.registration.ble.bluetooth.BleBluetooth;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class PeriodScanCallback implements BluetoothAdapter.LeScanCallback {

    protected Handler handler = new Handler(Looper.getMainLooper());
    protected long timeoutMillis = 10000;
    protected BleBluetooth bleBluetooth;

    public PeriodScanCallback(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public abstract void onScanTimeout();

    public void notifyScanStarted() {
        if (timeoutMillis > 0) {
            removeHandlerMsg();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bleBluetooth.stopScan(PeriodScanCallback.this);
                    onScanTimeout();
                }
            }, timeoutMillis);
        }
    }

    public void removeHandlerMsg() {
        handler.removeCallbacksAndMessages(null);
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public PeriodScanCallback setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        return this;
    }

    public BleBluetooth getBleBluetooth() {
        return bleBluetooth;
    }

    public PeriodScanCallback setBleBluetooth(BleBluetooth bluetooth) {
        this.bleBluetooth = bluetooth;
        return this;
    }
}
