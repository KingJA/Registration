package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.tdr.registration.R;
import com.tdr.registration.util.BleInterface;
import com.tdr.registration.util.BLE_Util;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙设备列表
 */
@ContentView(R.layout.activity_select_ble)
public class BleListActivity extends Activity implements BleInterface.BleScanListener {
    @ViewInject(R.id.image_back)
    private ImageView image_back;
    @ViewInject(R.id.text_title)
    private TextView text_title;
    @ViewInject(R.id.swipe_ble)
    private SwipeRefreshLayout swipe_ble;
    @ViewInject(R.id.list_ble)
    private ListView list_ble;

    private BLE_Util BLE = BLE_Util.instance;
    private List<SearchResult> SRlist = new ArrayList<>();
    private MyAdapter MA;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity = this;
        text_title.setText("设备列表");
        swipe_ble.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BLE.startScan(BleListActivity.this);
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MA = new MyAdapter();
        list_ble.setAdapter(MA);
        list_ble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BLE.unRegisterStateBLE();
//                Bundle bundle = new Bundle();
//                bundle.putString("Nane", SRlist.get(position).getName());
//                bundle.putString("MAC", SRlist.get(position).getAddress());
//                ActivityUtil.goActivityWithBundle(BleListActivity.this, AerialSignalActivity.class, bundle);

                Intent intent = new Intent();
                intent.putExtra("BleName", SRlist.get(position).getName());
                intent.putExtra("BleMAC", SRlist.get(position).getAddress());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        if (BLE.isOpenBLE()) {
            BLE.startScan(this);
        } else {
            BLE.OpenBLE();
            BLE.RegisterStateBLE(new BleInterface.BleOpenListener() {
                @Override
                public void onBleisOpen(boolean isOpen) {
                    if(isOpen){
                        BLE.startScan(BleListActivity.this);
                    }
                }
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        BLE_Util.instance.stopScan();
    }

    @Override
    public void onSearchStarted() {
        SRlist.clear();
    }

    @Override
    public void onDeviceFounded(SearchResult device) {
        if (!SRlist.contains(device)) {
            if (!device.getName().equals("NULL")) {
                SRlist.add(device);
                MA.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSearchStopped() {

    }

    @Override
    public void onSearchCanceled() {

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return SRlist.size();
        }

        @Override
        public Object getItem(int position) {
            return SRlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View V = LayoutInflater.from(mActivity).inflate(R.layout.ble_item, null);
            TextView textBleName = (TextView) V.findViewById(R.id.text_bleName);
            TextView textBleAddress = (TextView) V.findViewById(R.id.text_bleAddress);
            SearchResult result = SRlist.get(position);
            textBleName.setText(result.getName());
            textBleAddress.setText(result.getAddress());
            return V;
        }
    }
}
