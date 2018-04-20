package com.tdr.registration.util;

import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by Administrator on 2017/11/21.
 */

public class BleInterface {

    /**
     * 蓝牙扫描监听
     */
    public  interface BleScanListener {
        /**
         * 开始扫描
         */
        void onSearchStarted();

        /**
         * 扫描到设备
         *
         * @param device 蓝牙设备数据
         */
        void onDeviceFounded(SearchResult device);

        /**
         * 暂停扫描
         */
        void onSearchStopped();

        /**
         * 结束扫描
         */
        void onSearchCanceled();
    }

    /**
     * 蓝牙开启状态监听
     */
    public interface BleOpenListener {
        /**
         * 是否开启
         *
         * @param isOpen true 开启 false 关闭
         */
        void onBleisOpen(boolean isOpen);
    }

    /**
     * 蓝牙设备配对状态监听
     */
    public interface BleBondListener {
        /**
         * 配对状态变化监听
         *
         * @param mac       MAC地址
         * @param bondState 状态值
         */
        void onStateChanged(String mac, int bondState);
    }

    /**
     * 蓝牙设备连接监听
     */
    public interface ConnectBleListener {
        /**
         * 状态变化监听
         *
         * @param code 链接状态返回码
         * @param data 链接状态
         */
        void onResponse(int code, BleGattProfile data);
    }

    /**
     * 蓝牙设备连接状态监听
     */
    public interface ConnectStatusListener {
        /**
         * 状态变化监听
         *
         * @param Status 链接状态
         */
        void onConnectStatus(int Status);
    }

    /**
     * 蓝牙通讯监听
     */
    public interface BleNotifyListener {
        void onNotify(byte[] value);
    }

    /**
     * 蓝牙状态监听
     */
    public interface BleStatusListener {
        void onBleStatusListener(boolean Status);
        void onBleConnectStatusListener(boolean Status);
    }

    /**
     * 写入状态监听
     */
    public interface BleWriteListener {
        void onWriteListener(int code);
    }



}
