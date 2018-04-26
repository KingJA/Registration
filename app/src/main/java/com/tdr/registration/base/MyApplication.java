package com.tdr.registration.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.RegisterData;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.BLE_Util;
import com.tdr.registration.util.BleInterface;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.mLog;
import com.tdr.registration.util.onBleStateListener;

import cn.jpush.android.api.JPushInterface;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linus_Xie on 2016/9/9.
 */
public class MyApplication extends Application implements BleInterface.BleStatusListener {

    public static Context context;
    private static SharedPreferences mSharedPreferences;
    public String CarType = "";
    public String TitleType = "";
    public String InType = "";
    public String IsPRE = "";
    private onBleStateListener BSL;
    private BLE_Util BLE;
    private boolean BleStatus;
    private boolean BleConnectStatus;
    public String DistrainCarListID = "";
    private RegisterData RD;
    private List<Activity>  ACList;
    List<UploadInsuranceModel> uploadInsuranceModels;
    ConfirmInsuranceModel CIM;

    public List<Activity> getACList() {
        return ACList;
    }

    public void setACList(List<Activity> ACList) {
        this.ACList = ACList;
    }

    public List<UploadInsuranceModel> getUploadInsuranceModels() {
        return uploadInsuranceModels;
    }

    public void setUploadInsuranceModels(List<UploadInsuranceModel> uploadInsuranceModels) {
        this.uploadInsuranceModels = uploadInsuranceModels;
    }

    public ConfirmInsuranceModel getCIM() {
        return CIM;
    }

    public void setCIM(ConfirmInsuranceModel CIM) {
        this.CIM = CIM;
    }

    public String getIsPRE() {
        return IsPRE;
    }

    public void setIsPRE(String isPRE) {
        IsPRE = isPRE;
    }

    public String getInType() {
        return InType;
    }

    public void setInType(String inType) {
        InType = inType;
    }

    public RegisterData getRD() {
        return RD;
    }
    public void setRD(RegisterData RD) {
        this.RD = RD;
    }

    public String getDistrainCarListID() {
        return DistrainCarListID;
    }

    public void setDistrainCarListID(String distrainCarListID) {
        DistrainCarListID = distrainCarListID;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
//        Logger.addLogAdapter(new AndroidLogAdapter());
        context = this;
        mSharedPreferences = getSharedPreferences(Constants.APPLICATION_NAME, MODE_PRIVATE);
        JPushInterface.setDebugMode(false);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        BLE_Util.instance.BLE_Client(context);
        BLE= BLE_Util.instance;
        BLE.RegisterBLEState(this);
        BleStatus= BLE.isOpenBLE();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);/*64K说拜拜*/
    }
    public String getTitleType() {
        return TitleType;
    }

    public void setTitleType(String titleType) {
        TitleType = titleType;
    }

    public onBleStateListener getBSL() {
        return BSL;
    }

    public void setBSL(onBleStateListener BSL) {
        this.BSL = BSL;
    }
    public boolean isBleStatus() {
        return BleStatus;
    }

    public boolean isBleConnectStatus() {
        return BleConnectStatus;
    }
//    public static final Map<String, String> getUserInfo(Context context);

    public String getCarType() {
        mLog.e("获取CarType=" + CarType);
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
        mLog.e("缓存CarType=" + CarType);
    }

    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getSP() {
        return mSharedPreferences;
    }


    @Override
    public void onBleStatusListener(boolean Status) {
        mLog.e(Status?"蓝牙开启":"蓝牙关闭");
        BleStatus=Status;
        if(BSL!=null){
            BSL.BleState(Status);
        }
    }

    @Override
    public void onBleConnectStatusListener(boolean Status) {
        mLog.e(Status?"设备已连接":"设备已断开");
        BleConnectStatus=Status;
        if(BSL!=null){
            BSL.BleConnectState(Status);
        }
    }
}
