package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Entity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ShowPhotoAdapter;
import com.tdr.registration.data.PacketData;

import com.tdr.registration.fragment.CommandFragment;
import com.tdr.registration.fragment.NoticeFragment;
import com.tdr.registration.util.BLE_Util;
import com.tdr.registration.util.BleInterface;

import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ArcProgress;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙空中信号
 */
@ContentView(R.layout.activity_aerial_signal)
public class AerialSignalActivity extends Activity implements
        Handler.Callback, BleInterface.ConnectBleListener, BleInterface.ConnectStatusListener, BleInterface.BleOpenListener, BleInterface.BleNotifyListener {
    @ViewInject(R.id.IV_back)
    private ImageView IV_back;

//    @ViewInject(R.id.V_BKG)
    private View V_BKG;

//    @ViewInject(R.id.TV_SignalIntensity)
    private TextView TV_SignalIntensity;

    @ViewInject(R.id.TV_MSG)
    private TextView TV_MSG;

//    @ViewInject(R.id.AP_SignalIntensity)
    private ArcProgress AP_SignalIntensity;
    private LineChart LC_line;


    @ViewInject(R.id.VP_show)
    private ViewPager VP_show;

    @ViewInject(R.id.TV_Start)
    private TextView TV_Start;

    private BLE_Util BLE = BLE_Util.instance;
    private PacketData mData = BLE_Util.instance.getmData();
    private String Nane = "";
    private String MAC = "";
    private BleGattProfile blegattprofile;
    boolean isStart = false;
    private Handler mHandler;
    private View SC;
    private View SL;
    private ArrayList<View> list;
    private ShowPhotoAdapter SPA;
    private ArrayList<Entry> values;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        x.view().inject(this);
        mHandler = new Handler(this);
        initview();
        try {
            mLog.e("111111111111");
            Nane = getIntent().getExtras().getString("Nane");
            MAC = getIntent().getExtras().getString("MAC");
        } catch (NullPointerException e) {
            mLog.e("222222222222");
            Nane = (String) SharedPreferencesUtils.get("bleName", "");
            MAC = (String) SharedPreferencesUtils.get("bleMacAddress", "");
        }
        mLog.e("Nane=" + Nane);
        mLog.e("MAC=" + MAC);
        isBle();
//        BLE.RegisterGattUpdate(this,this);
        BLE.ConnectBle(MAC, this);
        BLE.RegisterConnectStatus(MAC, this);
        BLE.RegisterStateBLE(this);


    }


    private void initview() {

        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        TV_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BLE.isOpenBLE() && blegattprofile != null) {
                    setStatus();
                } else {

                    Utils.showToast("正在连接蓝牙，请稍候。");
                }
            }
        });
        SC = this.getLayoutInflater().inflate(R.layout.signal_circular, null);
        AP_SignalIntensity =(ArcProgress)SC.findViewById(R.id.AP_SignalIntensity);
        TV_SignalIntensity =(TextView)SC.findViewById(R.id.TV_SignalIntensity);
        V_BKG=(View) SC.findViewById(R.id.V_BKG);

        SL = this.getLayoutInflater().inflate(R.layout.signal_line, null);
        LC_line=(LineChart)SL.findViewById(R.id.LC_line);
        setLC();
        values =new ArrayList<>();

        list=new ArrayList<>();
        list.add(SC);
        list.add(SL);
        SPA =new ShowPhotoAdapter(list);
        VP_show.setAdapter(SPA);

    }
    private void setLC(){
        //后台绘制
        LC_line.setDrawGridBackground(false);
        //设置描述文本
        LC_line.getDescription().setEnabled(false);
        //设置支持触控手势
        LC_line.setTouchEnabled(false);
        //设置缩放
        LC_line.setDragEnabled(false);
        //设置Y轴左侧标值颜色
        LC_line.getAxisLeft().setTextColor(Color.WHITE);
        // 不显示y轴右边的值
        LC_line.getAxisRight().setEnabled(false);
        //X轴标值不显示
//        LC_line.getXAxis().setEnabled(false);
        //设置X轴标值颜色
        LC_line.getXAxis().setTextColor(Color.WHITE);
        //设置是否绘制chart边框的线
        LC_line.setDrawBorders(true);
        //设置chart边框线颜色
        LC_line.setBorderColor(Color.WHITE);
        //设置推动
        LC_line.setScaleEnabled(false);

        // 没有数据的时候，显示“暂无数据”
        LC_line.setNoDataText("暂无数据");
        LC_line.setNoDataTextColor(Color.WHITE);
        //设置铭文不显示
        LC_line.getLegend().setForm(Legend.LegendForm.NONE);

        XAxis xAxis = LC_line.getXAxis();
//        xAxis.setDrawGridLines(false);
        //设置x轴的最大值
        xAxis.setAxisMaximum(30f);
        //设置x轴的最小值
        xAxis.setAxisMinimum(0f);
        xAxis.setGridColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        YAxis leftAxis = LC_line.getAxisLeft();
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setAxisMaximum(100f);
        //y轴最小
        leftAxis.setAxisMinimum(0f);
        //设置Y轴分段线颜色
//        leftAxis.setAxisLineColor(Color.WHITE);




    }
    private void setStatus() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if (isStart) {
                    TV_Start.setText("开启信号检测");
                    TV_Start.setBackgroundResource(R.drawable.shape_button_bkg);
                    TV_Start.setTextColor(Color.parseColor("#4B9DF9"));
                    TV_MSG.setText("蓝牙已连接");
                    if(VP_show.getCurrentItem()==0){
                      AP_SignalIntensity.setProgress(0);
                      TV_SignalIntensity.setText("0");
                    }
                    isStart = false;
                } else {
                    TV_Start.setText("停止信号检测");
                    TV_Start.setBackgroundResource(R.drawable.shape_button_bkg0);
                    TV_Start.setTextColor(Color.parseColor("#ffffff"));
                    TV_MSG.setText("信号测速中...");
                    isStart = true;
                }
                mLog.e("isStart=" + isStart);
                for (BleGattService bleGattService : blegattprofile.getServices()) {
                    if (bleGattService.getUUID().toString().equals(BLE_Util.ServiceID)) {
                        for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                            if (bleGattCharacter.getUuid().toString().equals(BLE_Util.UUID)) {
                                byte[] data = mData.AerialSignal(isStart);
                                BLE.Write(MAC, bleGattService.getUUID(), bleGattCharacter.getUuid(), data);
                            }
                        }
                    }
                }
            }
        });
    }
    private void notData(List<Entry> list){
        LineDataSet set1;
        if (LC_line.getData() != null &&LC_line.getData().getDataSetCount() > 0) {
            set1  = (LineDataSet) LC_line.getData().getDataSetByIndex(0);
            set1.setValues(list);
            LC_line.getData().notifyDataChanged();
            LC_line.notifyDataSetChanged();
            LC_line.invalidate();
        }else{
//            list.add(new Entry(0,y));
            set1 = new LineDataSet(list,"");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);
            // create a data object with the datasets
            LineData data = new LineData(set1);

            // set data
            LC_line.setData(data);
        }
    }
    private void isBle() {
        //手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.showToast("该设备不支持BLE，即将离开改页面");
            try {
                Thread.sleep(2000);
                onBackPressed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResponse(int code, BleGattProfile data) {
        mLog.e("设备连接进度=" + code);
        if (code == 0) {//创建蓝牙链路成功
            TV_MSG.setText("蓝牙连接成功");
            blegattprofile = data;
            BLE.Notify(MAC, data, this);
//            BLE.Read(MAC,data.getServices().get(0).getUUID(),data.getServices().get(0).getCharacters().get(0).getUuid());
           if(VP_show.getCurrentItem()==0){
               V_BKG.setBackgroundResource(R.drawable.shape_circular0);
           }
        } else {
            TV_MSG.setText("蓝牙已断开");
            if(VP_show.getCurrentItem()==0) {
                V_BKG.setBackgroundResource(R.drawable.shape_circular1);
            }
        }
    }

    @Override
    public void onConnectStatus(int Status) {
        mLog.e("蓝牙连接状态=" + Status);
        if (Status == 16) {//连接成功
            TV_MSG.setText("蓝牙连接成功");
            if(VP_show.getCurrentItem()==0) {
                V_BKG.setBackgroundResource(R.drawable.shape_circular0);
            }
        } else if (Status == 32) {//断开连接
            TV_MSG.setText("蓝牙已断开");
            if(VP_show.getCurrentItem()==0) {
                V_BKG.setBackgroundResource(R.drawable.shape_circular1);
            }
        }
    }

    @Override
    public void onBleisOpen(boolean isOpen) {
        mLog.e("监听蓝牙开关状态=" + isOpen);
        if (isOpen) {
            BLE.ConnectBle(MAC, this);
        } else {
            TV_MSG.setText("蓝牙尚未开启");
            if(VP_show.getCurrentItem()==0) {
                V_BKG.setBackgroundResource(R.drawable.shape_circular1);
            }
        }
    }

    @Override
    public void onNotify(final byte[] value) {//蓝牙通讯数据
        byte[] commandType = Utils.GetByteArrayByLength(value, 1, 1);
        if (Utils.checkByte(commandType, mData.uploadHeartBeat)) {
//            LOG.e("onNotify=" + Utils.bytesToHexString(value));
        } else if (Utils.checkByte(commandType, mData.uploadaerialSignal)) {
            mLog.e("onNotify=" + Utils.bytesToHexString(value));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isStart) {

                        float index=0;
                        if(values.size()>30){
                            values.remove(0);
                            for (Entry entry : values) {
                                entry.setX(entry.getX()-1);
                                index=entry.getX()+1;
                            }
                        }else{
                            index+=values.size();
                        }
                        values.add(new Entry(index,mData.AerialSignal_Analysis(value)));



                        if(VP_show.getCurrentItem()==0){
                            AP_SignalIntensity.setProgress(mData.AerialSignal_Analysis(value));
                            TV_SignalIntensity.setText(mData.AerialSignal_Analysis(value) + "");
                        }else{
                            notData(values);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            for (BleGattService bleGattService : blegattprofile.getServices()) {
                if (bleGattService.getUUID().toString().equals(BLE_Util.ServiceID)) {
                    for (BleGattCharacter bleGattCharacter : bleGattService.getCharacters()) {
                        if (bleGattCharacter.getUuid().toString().equals(BLE_Util.UUID)) {
                            byte[] data = mData.AerialSignal(false);
                            BLE.Write(MAC, bleGattService.getUUID(), bleGattCharacter.getUuid(), data);
                            mLog.e("关闭空中信号检测");
                            SystemClock.sleep(500);
                            finish();
                        }
                    }
                }
            }
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        BLE.DisConnectBle(MAC);
        BLE.unRegisterConnectStatus(MAC);
        BLE.unRegisterStateBLE();
        if (blegattprofile != null) {
            BLE.unNotify(MAC, blegattprofile);
        }
        super.onDestroy();
    }

     class ShowPhotoAdapter extends PagerAdapter {
        List<View> mList;

        public ShowPhotoAdapter(List<View> localList) {
            mList = localList;
        }

        public void Update(List<View> paramList) {
            mList = paramList;
            notifyDataSetChanged();
        }

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            paramViewGroup.removeView(mList.get(paramInt));
        }

        @Override
        public int getCount() {

            return mList.size();
        }

        public Object instantiateItem(ViewGroup paramViewGroup, final int paramInt) {


            paramViewGroup.addView(mList.get(paramInt));
            return mList.get(paramInt);
        }

        @Override
        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }
    }

}
