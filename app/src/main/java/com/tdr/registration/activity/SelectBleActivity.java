package com.tdr.registration.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 蓝牙列表选择
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SelectBleActivity extends BaseActivity {
    public static final String TAG = SelectBleActivity.class.getSimpleName();
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_deal)
    TextView textDeal;
    @BindView(R.id.search_progress)
    ProgressBar searchProgress;
    @BindView(R.id.list_ble)
    ListView listBle;

    private Context mContext;
    // 蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 蓝牙信号强度
    private ArrayList<Integer> rssis;
    //自定义adapter
    private LeDeviceListAdapter mLeDevices;

    // 描述扫描蓝牙的状态
    private boolean mScanning;
    private boolean scanFlag;
    private Handler mHandler;
    int REQUEST_ENABLE_BT = 1;
    // 蓝牙扫描时间
    private static final long SCAN_PERIOD = 15000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ble);
        ButterKnife.bind(this);
        mContext = this;
        mHandler = new Handler();
        initView();
        initBle();
        scanFlag = true;
        //自定义适配器
        mLeDevices = new LeDeviceListAdapter();
        listBle.setAdapter(mLeDevices);
        listBle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = mLeDevices.getDevice(position);
                if (device == null) {
                    return;
                }
                /*final Intent intent = new Intent(SelectBleActivity.this, AuditingActivity.class);
                intent.putExtra(Constants.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(Constants.EXTRAS_DEVICE_ADDRESS, device.getAddress());*/
                final Intent intent = new Intent(SelectBleActivity.this, AuditingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("bleDevice", device);
                intent.putExtras(bundle);
                if (mScanning) {
                    //停止扫描设备
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }

                try {
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        textTitle.setText("蓝牙列表");
        textDeal.setVisibility(View.VISIBLE);
        textDeal.setText("搜索");
    }

    private void initBle() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @OnClick({R.id.image_back, R.id.text_deal})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                ActivityUtil.goActivityAndFinish(SelectBleActivity.this, AuditingActivity.class);
                break;

            case R.id.text_deal:
                if (scanFlag) {
                    mLeDevices = new LeDeviceListAdapter();
                    listBle.setAdapter(mLeDevices);
                    scanLeDevice(true);
                    searchProgress.setVisibility(View.VISIBLE);
                } else {
                    scanLeDevice(false);
                    searchProgress.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scanFlag = true;
                    searchProgress.setVisibility(View.VISIBLE);
                    mLog.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            scanFlag = false;
            searchProgress.setVisibility(View.GONE);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scanFlag = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
     **/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            // TODO Auto-generated method stub

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* 讲扫描到设备的信息输出到listview的适配器 */
                    mLeDevices.addDevice(device, rssi);
                    mLeDevices.notifyDataSetChanged();
                }
            });

            System.out.println("Address:" + device.getAddress());
            System.out.println("Name:" + device.getName());
            System.out.println("rssi:" + rssi);

        }
    };

    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            rssis = new ArrayList<>();
            mLeDevices = new ArrayList<>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            String deviceName = Utils.initNullStr(device.getName());
            if (!mLeDevices.contains(device)  && deviceName.contains("TDR")){
                mLeDevices.add(device);
                rssis.add(rssi);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            rssis.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = mInflator.inflate(R.layout.ble_item, null);
            TextView textBleName = (TextView) view.findViewById(R.id.text_bleName);
            TextView textBleAddress = (TextView) view.findViewById(R.id.text_bleAddress);

            BluetoothDevice device = mLeDevices.get(i);
            textBleAddress.setText(device.getAddress());
            textBleName.setText(device.getName());

            return view;
        }
    }
}
