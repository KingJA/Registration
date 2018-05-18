package com.tdr.registration.util;

import android.content.Context;
import android.os.SystemClock;


import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.tdr.registration.data.PacketData;
import com.tdr.registration.model.BlackCarModel;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.tdr.registration.util.BleInterface.*;


/**
 * Created by Administrator on 2017/11/21.
 */

public enum BLE_Util {
    instance;
    BLE_Util() {//私有构造方法，防止被实例化
    }

    public static final int BLECODE = 1991;
    private BluetoothClient mClient;
    public final static String ServiceID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public final static String UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private boolean BLEisOpen = false;
    private boolean BLEisConnect = false;
    private PacketData mData;
    private String Name ="";
    private String MAC ="";
    private BleGattProfile BGP;

    public boolean isBLEisConnect() {
        return BLEisConnect;
    }

    public PacketData getmData() {
        return mData;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
        SharedPreferencesUtils.put("BLE_Name",name);
        mLog.e("缓存蓝牙名字:"+name);
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String mac) {
        if(MAC==null||MAC.equals("")){
            mClient.registerConnectStatusListener(mac, new BleConnectStatusListener() {
                @Override
                public void onConnectStatusChanged(String mac, int status) {
                    if(BSL!=null){
                        if(status==16){
                            BSL.onBleConnectStatusListener(true);
                        }else if(status==32){
                            BSL.onBleConnectStatusListener(false);
                        }
                    }
                }
            });
        }
        this.MAC = mac;
        SharedPreferencesUtils.put("BLE_MAC",MAC);
        mLog.e("缓存蓝牙MAC地址:"+mac);
    }

    public BleGattProfile getBGP() {
        return BGP;
    }


    private BleOpenListener BOL;
    private BleBondListener BBL;
    private ConnectBleListener CBL;
    private ConnectStatusListener CSL;
    private BleStatusListener BSL;
    private BleWriteListener BWL;
    /**
     * 初始化蓝牙工具类
     *
     * @param context
     */
    public void BLE_Client(Context context) {
        mClient = new BluetoothClient(context);
        mData = new PacketData();

    }

    //-----------------------------------------------------------------------------------
    public boolean isConnection() {
        if (MAC == null) {
            return false;
        }
        return MAC.equals("") ? false : true;
    }

    /**
     * 扫描蓝牙设备
     */
    public void startScan(final BleScanListener BL) {
        SearchRequest SR = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        mClient.search(SR, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                BL.onSearchStarted();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (BL != null) {
                    BL.onDeviceFounded(device);
                }
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
            }

            @Override
            public void onSearchStopped() {
                BL.onSearchStopped();
            }

            @Override
            public void onSearchCanceled() {
                BL.onSearchCanceled();
            }
        });
    }

    public void stopScan() {
        mClient.stopSearch();
    }

    /**
     * 打开蓝牙
     */
    public void OpenBLE() {
        mClient.openBluetooth();
    }

    /**
     * 关闭蓝牙
     */
    public void CloseBLE() {
        mClient.closeBluetooth();
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return true 蓝牙打开 /false 蓝牙关闭
     */
    public boolean isOpenBLE() {
        BLEisOpen = mClient.isBluetoothOpened();
        return BLEisOpen;
    }


    /**
     * 注册蓝牙开关状态监听
     */
    public void RegisterStateBLE(BleOpenListener BOL) {
        this.BOL = BOL;
        mClient.registerBluetoothStateListener(mBluetoothStateListener);
    }
    /**
     * 注销注册蓝牙开关状态监听
     */
    public void unRegisterStateBLE() {
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    /**
     * 蓝牙开关状态广播
     */
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (BOL != null) {
                BOL.onBleisOpen(openOrClosed);
            }
            BLEisOpen = openOrClosed;
        }
    };

    /**
     * 注册蓝牙设备绑定状态监听
     *
     * @param BBL
     */
    public void RegisterBondBLE(BleBondListener BBL) {
        this.BBL = BBL;
        mClient.registerBluetoothBondListener(mBluetoothBondListener);
    }

    public void unRegisterBondBLE() {
        mClient.unregisterBluetoothBondListener(mBluetoothBondListener);
    }


    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            mLog.e("mac=" + mac + "\nbondState=" + bondState);
//             bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
            if (BBL != null) {
                BBL.onStateChanged(mac, bondState);
            }
        }
    };

    /**
     * 连接设备
     *
     * @param CBL
     */
    public void ConnectBle(ConnectBleListener CBL) {
        ConnectBle(MAC, CBL);
    }

    /**
     * 连接设备
     *
     * @param MAC
     */
    public void ConnectBle(String MAC, ConnectBleListener cbl) {
        this.CBL = cbl;
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();

        mClient.connect(MAC, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (CBL != null) {
                    CBL.onResponse(code, data);
                }
                if (data != null) {
                    mLog.e("code=" + code + "\nBleGattProfile=" + data.toString());
                }
                if (code == 0) {
                    BLEisConnect = true;
                } else {
                    BLEisConnect = false;
                }
                BGP = data;
            }
        });
    }

    public void DisConnectBle(String MAC) {
        BLEisConnect = false;

        mClient.disconnect(MAC);
    }

    /**
     * 监听指定蓝牙设备的链接状态
     */
    public void RegisterConnectStatus(ConnectStatusListener CSL) {
        this.CSL = CSL;
        mClient.registerConnectStatusListener(MAC, mBleConnectStatusListener);
    }

    /**
     * 监听指定蓝牙设备的链接状态
     *
     * @param MAC
     */
    public void RegisterConnectStatus(String MAC, ConnectStatusListener CSL) {
        this.CSL = CSL;
        mClient.registerConnectStatusListener(MAC, mBleConnectStatusListener);
    }

    /**
     * 注销监听指定蓝牙设备的链接状态
     *
     * @param MAC
     */
    public void unRegisterConnectStatus(String MAC) {
        mClient.unregisterConnectStatusListener(MAC, mBleConnectStatusListener);
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            CSL.onConnectStatus(status);
            if (status == 16) {
                BLEisConnect = true;
            } else if (status == 32) {
                BLEisConnect = false;
            }
        }
    };

    /**
     * 读取蓝牙数据
     *
     * @param MAC
     * @param serviceUUID
     * @param characterUUID
     */
    public void Read(String MAC, UUID serviceUUID, UUID characterUUID) {
        mClient.read(MAC, serviceUUID, characterUUID, new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                if (code == REQUEST_SUCCESS) {

                    mLog.e("读取character=" + Utils.bytesToHexString(data));
                }
            }
        });
    }

    public void Write(byte[] bytes,BleGattProfile bgp) {
        BGP=bgp;
        for (BleGattService bleGattService : bgp.getServices()) {
            if (bleGattService.getUUID().toString().equals(ServiceID)) {
                for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                    if(bleGattCharacter.getUuid().toString().equals(UUID)){
                        Write(MAC, bleGattService.getUUID(), bleGattCharacter.getUuid(), bytes);
                    }
                }
            }
        }
    }
    public void Write(byte[] bytes) {
        for (BleGattService bleGattService : BGP.getServices()) {
            if (bleGattService.getUUID().toString().equals(ServiceID)) {
                for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                    if(bleGattCharacter.getUuid().toString().equals(UUID)){
                        Write(MAC, bleGattService.getUUID(), bleGattCharacter.getUuid(), bytes);
                    }
                }
            }
        }
    }

    /**
     * 发送指令
     *
     * @param MAC
     * @param serviceUUID
     * @param characterUUID
     * @param bytes
     */
    public void Write(String MAC, final UUID serviceUUID, final UUID characterUUID, byte[] bytes) {
        mLog.BLE("写入内容=" + Utils.bytesToHexString(bytes));
        mClient.write(MAC, serviceUUID, characterUUID, bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
//                mLog.BLE("serviceID="+serviceUUID);
//                mLog.BLE("characterUUID="+characterUUID);
//                mLog.BLE(code==0?"写入成功":"写入失败");
                if(BWL!=null){
                    BWL.onWriteListener(code);
                }
            }
        });
    }
    public void setWriteListener(BleWriteListener wbl){
        BWL=wbl;
    }
    /**
     * 建立蓝牙广播监听
     *
     * @param mac
     * @param data
     * @param BNL
     */
    public void Notify(String mac, BleGattProfile data, BleNotifyListener BNL) {
        for (BleGattService bleGattService : data.getServices()) {
            mLog.e("bleGattService=" + bleGattService.getUUID().toString());
            if (bleGattService.getUUID().toString().equals(ServiceID)) {
                for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                   if(bleGattCharacter.getUuid().toString().equals(UUID)){
                       Notify(mac, bleGattService.getUUID(), bleGattCharacter.getUuid(), BNL);
                   }
                }
            }
        }
    }

    /**
     * 建立蓝牙广播监听
     *
     * @param MAC
     * @param serviceUUID
     * @param characterUUID
     * @param BNL
     */
    public void Notify(String MAC, UUID serviceUUID, UUID characterUUID, final BleNotifyListener BNL) {
        mLog.e("开启Notify");
        mClient.notify(MAC, serviceUUID, characterUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                mLog.w("onNotify=" + Utils.bytesToHexString(value));
                BNL.onNotify(value);

            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    mLog.e("Notify_onResponse=" + code);
                }
            }
        });
    }
    /**
     * 关闭蓝牙通讯监听
     *

     */
    public void unNotify() {
        if (BLEisConnect) {
            for (BleGattService bleGattService : BGP.getServices()) {
                mLog.e("bleGattService=" + bleGattService.getUUID().toString());
                if (bleGattService.getUUID().toString().equals(ServiceID)) {
                    for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                        if(bleGattCharacter.getUuid().toString().equals(UUID)){
                            unNotify(MAC, bleGattService.getUUID(), bleGattCharacter.getUuid());
                        }
                    }
                }
            }
        }
    }


    /**
     * 关闭蓝牙通讯监听
     *
     * @param mac
     * @param data
     */
    public void unNotify(String mac, BleGattProfile data) {
        if (BLEisConnect) {
            for (BleGattService bleGattService : data.getServices()) {
                mLog.e("bleGattService=" + bleGattService.getUUID().toString());
                for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                    if(bleGattCharacter.getUuid().toString().equals(UUID)){
                        unNotify(mac, bleGattService.getUUID(), bleGattCharacter.getUuid());
                    }
                }
            }
        }
    }

    /**
     * 关闭蓝牙通讯监听
     *
     * @param MAC
     * @param serviceUUID
     * @param characterUUID
     */
    public void unNotify(String MAC, UUID serviceUUID, UUID characterUUID) {
        mClient.unnotify(MAC, serviceUUID, characterUUID, new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    mLog.e("unNotify_onResponse=" + code);
                }
            }
        });
    }

    /**
     * 黑车数据打包
     *
     * @param BCM
     * @return
     */
    public byte[] SendBlackCar(BlackCarModel BCM) {
        byte[] requestData = null;
        String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
        byte[] childContent = null;
        byte[] content = null;
        byte[] plate = new byte[10];

        String plateNumber = BCM.getPLATENUMBER();

        String theftNo = Utils.initNullStr(BCM.getTHEFTNO());//840,920
        String theftNo2 = Utils.initNullStr(BCM.getTHEFTNO2());//2.4G
        while (plateNumber.length() < 10) {
            plateNumber = " " + plateNumber;
        }
        try {
            plate = plateNumber.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] tag1 = null;
        byte[] tagId1 = null;
        if (!theftNo.equals("")) {
            byte[] no1 = Utils.longToByte(Long.valueOf(theftNo));
            tag1 = Utils.GetByteArrayByLength(no1, 4, 2);
            tagId1 = Utils.GetByteArrayByLength(no1, 0, 4);
        }

        byte[] tag2 = null;
        byte[] tagId2 = null;
        if (!theftNo2.equals("")) {
            byte[] no2 = Utils.longToByte(Long.valueOf(theftNo2));
            tag2 = Utils.GetByteArrayByLength(no2, 4, 2);
            tagId2 = Utils.GetByteArrayByLength(no2, 0, 4);
        }
        childContent = Utils.ByteArrayCopy(childContent, Utils.hexStr2Bytes(pcCode));
        childContent = Utils.ByteArrayCopy(childContent, plate);
        childContent = Utils.ByteArrayCopy(childContent, tag1);
        childContent = Utils.ByteArrayCopy(childContent, tagId1);
        childContent = Utils.ByteArrayCopy(childContent, tag2);
        childContent = Utils.ByteArrayCopy(childContent, tagId2);
        byte[] size = Utils.shortToByte((short) childContent.length);
        content = Utils.ByteArrayCopy(content, mData.controlCommand);
        content = Utils.ByteArrayCopy(content, size);
        content = Utils.ByteArrayCopy(content, childContent);
        requestData = Utils.ByteArrayCopy(requestData, content);
        mLog.e("黑车下发：" + Utils.bytesToHexString(requestData));
        return requestData;
    }




    /**
     *注册蓝牙开关与连接状态监听
     */
    public void RegisterBLEState(BleStatusListener bsl) {
        Name=(String)SharedPreferencesUtils.get("BLE_Name","");
        MAC=(String)SharedPreferencesUtils.get("BLE_MAC","");
        BSL=bsl;
        mClient.registerBluetoothStateListener(bsl2);
        if(MAC!=null&&!MAC.equals("")){
            mClient.registerConnectStatusListener(MAC,bsl1);
        }
    }

    private final BleConnectStatusListener bsl1 = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if(BSL!=null){
                if(status==16){
                    BSL.onBleConnectStatusListener(true);
                }else if(status==32){
                    BSL.onBleConnectStatusListener(false);
                }

            }
        }
    };
    private final BluetoothStateListener bsl2 = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if(BSL!=null){
                if(openOrClosed){
                    BSL.onBleStatusListener(true);
                }else{
                    BSL.onBleStatusListener(false);
                }
            }
        }
    };
}
