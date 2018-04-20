package com.tdr.registration.activity;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.util.mLog;

import org.w3c.dom.Text;

/**
 * 搜索到的蓝牙设备列表
 */

public class BluetoothDeviceActivity extends BaseActivity {
    // Debugging
    private static final String DEBUG_TAG = "DeviceListActivity";
    public static LinearLayout deviceNamelinearLayout;
    // Member fields
    private ListView lvPairedDevice = null, lvNewDevice = null;
    private TextView tvPairedDevice = null;
    private RelativeLayout tvNewDevice = null;
    ///private Button btDeviceScan = null;
    private ImageView imagBack;
    private TextView textTitle;
    private TextView textDeal;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private PortParameters mPortParam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_bluetooth_device);
        mPortParam = new PortParameters();
        mPortParam.setPortType(PortParameters.BLUETOOTH);
       // tvPairedDevice = (TextView) findViewById(R.id.tvPairedDevices);
        lvPairedDevice = (ListView) findViewById(R.id.lvPairedDevices);
        tvNewDevice = (RelativeLayout) findViewById(R.id.tvNewDevices);
        lvNewDevice = (ListView) findViewById(R.id.lvNewDevices);
       /* btDeviceScan = (Button) findViewById(R.id.btBluetoothScan);
        btDeviceScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.setVisibility(View.GONE);
                discoveryDevice();
            }
        });*/
        imagBack = (ImageView) findViewById(R.id.image_back);
        imagBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textTitle = (TextView) findViewById(R.id.text_title);
        textTitle.setText("蓝牙列表");
        textDeal = (TextView) findViewById(R.id.text_deal);
        textDeal.setVisibility(View.VISIBLE);
        textDeal.setText("搜索");
        textDeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                discoveryDevice();
            }
        });
        getDeviceList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        if (mFindBlueToothReceiver != null)
            this.unregisterReceiver(mFindBlueToothReceiver);
    }

    protected void getDeviceList() {
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device_name_item);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device_name_item);
        lvPairedDevice.setAdapter(mPairedDevicesArrayAdapter);
        lvPairedDevice.setOnItemClickListener(mDeviceClickListener);
        lvNewDevice.setAdapter(mNewDevicesArrayAdapter);
        lvNewDevice.setOnItemClickListener(mDeviceClickListener);
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            //tvPairedDevice.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    // changes the title when discovery is finished
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_bluetooth_device);
                mLog.i("tag", "finish discovery" + mNewDevicesArrayAdapter.getCount());
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_bluetooth_device_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    private void discoveryDevice() {
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scaning);
        // Turn on sub-title for new devices
        tvNewDevice.setVisibility(View.VISIBLE);

        lvNewDevice.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the
            // View
            String info = ((TextView) v).getText().toString();
            String noDevices = getResources().getText(R.string.none_paired).toString();
            String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
            mLog.i("tag", info);
            if (!info.equals(noDevices) && !info.equals(noNewDevice)) {
                String address = info.substring(info.length() - 17);
                // Create the result Intent and include the MAC address
                Intent intent = new Intent(BluetoothDeviceActivity.this, BlueListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(GpPrintService.PORT_TYPE, mPortParam.getPortType());
                bundle.putString(GpPrintService.IP_ADDR, mPortParam.getIpAddr());
                bundle.putInt(GpPrintService.PORT_NUMBER, mPortParam.getPortNumber());
                bundle.putString(GpPrintService.BLUETOOT_ADDR, info.substring(info.length() - 17));
                bundle.putString(GpPrintService.USB_DEVICE_NAME, mPortParam.getUsbDeviceName());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };
}
