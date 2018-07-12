package com.tdr.kingja.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tdr.kingja.adapter.BatteryCountAdapter;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.registration.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * Description:TODO
 * Create Time:2018/6/7 11:15
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryCountSelectActivity extends BaseTitleActivity {
    @BindView(R.id.lv_batteryCount)
    ListView lvBatteryCount;

    @Override
    public void initVariable() {

    }

    @Override
    protected String getContentTitle() {
        return "选择电池节数";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_select_battery_count;
    }

    @Override
    protected void initView() {
        BatteryCountAdapter batteryCountAdapter = new BatteryCountAdapter(this, Arrays.asList("1", "2", "3", "4",
                "5", "6", "7", "8", "9", "10"));
        lvBatteryCount.setAdapter(batteryCountAdapter);
    }

    @Override
    protected void initData() {
    }

    @OnItemClick({R.id.lv_batteryCount})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String count = (String) parent.getItemAtPosition(position);
        Intent intent = new Intent();
        intent.putExtra("count", count);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initNet() {

    }

}
